package com.example.taskup

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log

class DatabaseHelper(context: Context) : SQLiteOpenHelper(
    context,
    DATABASE_NAME,
    null,
    DATABASE_VERSION
) {
    companion object {
        private const val DATABASE_VERSION = 3
        private const val DATABASE_NAME = "TaskUpDB"

        //Table Names
        private const val TABLE_USERS = "users"
        private const val TABLE_PROJECTS = "projects"
        private const val TABLE_TASKS = "tasks"

        //Users Table
        private const val KEY_ID = "id"
        private const val KEY_EMAIL = "email"
        private const val KEY_USERNAME = "username"
        private const val KEY_PASSWORD = "password"

        //Projects Table
        private const val KEY_PROJECT_ID = "projectId"
        private const val KEY_PROJECT_TITLE = "projectTitle"
        private const val KEY_PROJECT_STATUS = "projectStatus"
        private const val KEY_USER_ID = "userId" // Foreign key

        // Tasks Table
        private const val KEY_TASK_ID = "taskId"
        private const val KEY_TASK_TITLE = "taskTitle"
        private const val KEY_TASK_DUE = "taskDue"
        private const val KEY_TASK_TIME = "taskTime"
        private const val KEY_TASK_DESC = "taskDesc"
        private const val KEY_TASK_STATUS = "taskStatus"
        private const val KEY_TASK_PRIORITY = "taskPriority"
        private const val KEY_TASK_PROJECT_ID = "projectId" // Foreign key
    }
    override fun onCreate(db: SQLiteDatabase?) {
        // Create users table
        val createUserTableQuery = ("CREATE TABLE IF NOT EXISTS $TABLE_USERS ("
                + "$KEY_ID INTEGER PRIMARY KEY,"
                + "$KEY_EMAIL TEXT,"
                + "$KEY_USERNAME TEXT,"
                + "$KEY_PASSWORD TEXT)")
        db?.execSQL(createUserTableQuery)

        // Create projects table with user_id as foreign key
        val createProjectsTableQuery = ("CREATE TABLE IF NOT EXISTS $TABLE_PROJECTS ("
                + "$KEY_PROJECT_ID INTEGER PRIMARY KEY,"
                + "$KEY_PROJECT_TITLE TEXT,"
                + "$KEY_PROJECT_STATUS TEXT,"
                + "$KEY_USER_ID INTEGER," // Foreign key column
                + "FOREIGN KEY($KEY_USER_ID) REFERENCES $TABLE_USERS($KEY_ID))")
        db?.execSQL(createProjectsTableQuery)

        // Create tasks table with project_id as foreign key
        val createTasksTableQuery = ("CREATE TABLE IF NOT EXISTS $TABLE_TASKS ("
                + "$KEY_TASK_ID INTEGER PRIMARY KEY,"
                + "$KEY_TASK_TITLE TEXT,"
                + "$KEY_TASK_DUE TEXT,"
                + "$KEY_TASK_TIME TEXT,"
                + "$KEY_TASK_DESC TEXT,"
                + "$KEY_TASK_STATUS TEXT,"
                + "$KEY_TASK_PRIORITY TEXT,"
                + "$KEY_TASK_PROJECT_ID INTEGER," // Foreign key column
                + "FOREIGN KEY($KEY_TASK_PROJECT_ID) REFERENCES $TABLE_PROJECTS($KEY_PROJECT_ID))")
        db?.execSQL(createTasksTableQuery)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        // Drop older table if it exists
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_USERS")
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_PROJECTS")
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_TASKS")

        // Create tables again
        onCreate(db)
    }

    // CRUD operations

    fun addUser(email: String, username: String, password: String): Long {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put(KEY_EMAIL, email)
            put(KEY_USERNAME, username)
            put(KEY_PASSWORD, password)
        }
        val id = db.insert(TABLE_USERS, null, values)
        db.close()
        return id
    }

    @SuppressLint("Range")
    fun getUser(username: String, password: String): Pair<User?, Int?> {
        val db = this.readableDatabase
        val cursor = db.rawQuery("SELECT * FROM $TABLE_USERS WHERE $KEY_USERNAME=? AND $KEY_PASSWORD=?", arrayOf(username, password))

        val userId = if (cursor.moveToFirst()) {
            cursor.getInt(cursor.getColumnIndex(KEY_ID))
        } else {
            null
        }

        val user = if (userId != null) {
            User(
                cursor.getInt(cursor.getColumnIndex(KEY_ID)),
                cursor.getString(cursor.getColumnIndex(KEY_EMAIL)),
                cursor.getString(cursor.getColumnIndex(KEY_USERNAME)),
                cursor.getString(cursor.getColumnIndex(KEY_PASSWORD))
            )
        } else {
            null
        }

        cursor.close()
        db.close()

        return Pair(user, userId)
    }
    @SuppressLint("Range")
    fun getUserId(username: String): Int? {
        val db = this.readableDatabase
        val query = "SELECT $KEY_ID FROM $TABLE_USERS WHERE $KEY_USERNAME = ?"
        val cursor = db.rawQuery(query, arrayOf(username))

        var userId: Int? = null
        cursor.use { cursor ->
            if (cursor.moveToNext()) {
                userId = cursor.getInt(cursor.getColumnIndex(KEY_ID))
            }
        }
        return userId
    }
    fun addProject(projectTitle: String, projectStatus: String, userId: Int): Long {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put(KEY_PROJECT_TITLE, projectTitle)
            put(KEY_PROJECT_STATUS, projectStatus)
            put(KEY_USER_ID, userId)
        }
        val projectId = db.insert(TABLE_PROJECTS, null, values)
        db.close()
        return projectId
    }
    @SuppressLint("Range")
    fun getProjects(userId: Int?): List<Project> {
        val projects = mutableListOf<Project>()
        val db = this.readableDatabase
        val query = "SELECT * FROM $TABLE_PROJECTS WHERE $KEY_USER_ID = ?"
        val cursor = db.rawQuery(query, arrayOf(userId.toString()))

        Log.i("DatabaseDebug", "Query: $query, UserId: $userId")

        cursor.use { cursor ->
            Log.i("DatabaseDebug", "Number of rows returned: ${cursor.count}")
            while (cursor.moveToNext()) {
                val projectId = cursor.getInt(cursor.getColumnIndex(KEY_PROJECT_ID))
                val title = cursor.getString(cursor.getColumnIndex(KEY_PROJECT_TITLE))
                val status = cursor.getString(cursor.getColumnIndex(KEY_PROJECT_STATUS))

                val project = Project(projectId, title, status, userId)
                projects.add(project)
            }
        }
        return projects
    }



    fun updateProject(projectId: Int, title: String, status: String): Boolean {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put(KEY_PROJECT_TITLE, title)
            put(KEY_PROJECT_STATUS, status)
        }
        val result = db.update(TABLE_PROJECTS, values, "$KEY_PROJECT_ID = ?", arrayOf(projectId.toString()))
        db.close()
        return result != -1
    }

    fun deleteProject(projectId: Int): Boolean {
        val db = this.writableDatabase
        val result = db.delete(TABLE_PROJECTS, "$KEY_PROJECT_ID = ?", arrayOf(projectId.toString()))
        db.close()
        return result != -1
    }
    fun addTask(
        title: String,
        due: String,
        time: String,
        description: String,
        status: String,
        priority: String,
        projectId: Int
    ): Long {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put(KEY_TASK_TITLE, title)
            put(KEY_TASK_DUE, due)
            put(KEY_TASK_TIME, time)
            put(KEY_TASK_DESC, description)
            put(KEY_TASK_STATUS, status)
            put(KEY_TASK_PRIORITY, priority)
            put(KEY_TASK_PROJECT_ID, projectId)
        }
        val id = db.insert(TABLE_TASKS, null, values)
        db.close()
        return id
    }
    @SuppressLint("Range")
    fun getTasks(userId: Int): List<Task> {
        val tasks = mutableListOf<Task>()
        val db = this.readableDatabase
        val query = """
    SELECT tasks.*, projects.${KEY_USER_ID} AS user_id
    FROM $TABLE_TASKS AS tasks
    JOIN $TABLE_PROJECTS AS projects ON tasks.${KEY_TASK_PROJECT_ID} = projects.${KEY_PROJECT_ID}
    WHERE projects.${KEY_USER_ID} = ?
""".trimIndent()

        val cursor = db.rawQuery(query, arrayOf(userId.toString()))

        cursor.use { cursor ->
            while (cursor.moveToNext()) {
                val taskId = cursor.getInt(cursor.getColumnIndex(KEY_TASK_ID))
                val title = cursor.getString(cursor.getColumnIndex(KEY_TASK_TITLE))
                val due = cursor.getString(cursor.getColumnIndex(KEY_TASK_DUE))
                val time = cursor.getString(cursor.getColumnIndex(KEY_TASK_TIME))
                val description = cursor.getString(cursor.getColumnIndex(KEY_TASK_DESC))
                val status = cursor.getString(cursor.getColumnIndex(KEY_TASK_STATUS))
                val priority = cursor.getString(cursor.getColumnIndex(KEY_TASK_PRIORITY))
                val projectId = cursor.getInt(cursor.getColumnIndex(KEY_TASK_PROJECT_ID))

                // Assuming you have a "user_id" column in the result set
                val userIdFromProject = cursor.getInt(cursor.getColumnIndex("user_id"))

                // Verify if userId matches
                if (userId == userIdFromProject) {
                    val task = Task(taskId, title, due, time, description, status, priority, projectId)
                    tasks.add(task)
                }
            }
        }
        return tasks
    }
    fun updateTask(taskId: Int, title: String, due: String, time: String, desc: String, status: String, priority: String): Boolean {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put(KEY_TASK_TITLE, title)
            put(KEY_TASK_DUE, due)
            put(KEY_TASK_TIME, time)
            put(KEY_TASK_DESC, desc)
            put(KEY_TASK_STATUS, status)
            put(KEY_TASK_PRIORITY, priority)
        }
        val result = db.update(TABLE_TASKS, values, "$KEY_TASK_ID = ?", arrayOf(taskId.toString()))
        db.close()
        return result != -1
    }
    fun deleteTask(taskId: Int): Boolean {
        val db = this.writableDatabase
        val result = db.delete(TABLE_TASKS, "$KEY_TASK_ID = ?", arrayOf(taskId.toString()))
        db.close()
        return result != -1
    }

    @SuppressLint("Range")
    fun getUserIdFromUsername(username: String): Int? {
        val db = this.readableDatabase
        val query = "SELECT $KEY_USER_ID FROM $TABLE_USERS WHERE $KEY_USERNAME = ?"
        val cursor = db.rawQuery(query, arrayOf(username))

        var userId: Int? = null
        cursor.use { cursor ->
            if (cursor.moveToNext()) {
                userId = cursor.getInt(cursor.getColumnIndex(KEY_USER_ID))
            }
        }
        return userId
    }

    @SuppressLint("Range")
    fun getEmailFromUsername(username: String): String? {
        val db = this.readableDatabase
        val query = "SELECT $KEY_EMAIL FROM $TABLE_USERS WHERE $KEY_USERNAME = ?"
        val cursor = db.rawQuery(query, arrayOf(username))

        return cursor.use {
            if (it.moveToFirst()) {
                it.getString(it.getColumnIndex(KEY_EMAIL))
            } else {
                null
            }
        }
    }

    fun getTotalTaskCount(): Int {
        val db = this.readableDatabase
        val query = "SELECT COUNT(*) FROM $TABLE_TASKS"
        val cursor = db.rawQuery(query, null)
        cursor.moveToFirst()
        val count = cursor.getInt(0)
        cursor.close()
        return count
    }

    fun getTotalProjectCount(): Int {
        val db = this.readableDatabase
        val query = "SELECT COUNT(*) FROM $TABLE_PROJECTS"
        val cursor = db.rawQuery(query, null)
        cursor.moveToFirst()
        val count = cursor.getInt(0)
        cursor.close()
        return count
    }

}
