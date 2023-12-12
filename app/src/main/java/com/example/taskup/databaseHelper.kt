package com.example.taskup

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DatabaseHelper(context: Context) : SQLiteOpenHelper(
    context,
    DATABASE_NAME,
    null,
    DATABASE_VERSION
) {
    companion object {
        private const val DATABASE_VERSION = 1
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
        private const val KEY_PROJECT_ID = "project_id"
        private const val KEY_PROJECT_TITLE = "project_title"
        private const val KEY_PROJECT_STATUS = "project_status"
        private const val KEY_USER_ID = "user_id" // Foreign key column referencing users_id

        // Tasks Table - Column names
        private const val KEY_TASK_ID = "task_id"
        private const val KEY_TASK_TITLE = "task_title"
        private const val KEY_TASK_DUE = "task_due"
        private const val KEY_TASK_TIME = "task_time"
        private const val KEY_TASK_DESC = "task_desc"
        private const val KEY_TASK_STATUS = "task_status"
        private const val KEY_TASK_PRIORITY = "task_priority"
        private const val KEY_TASK_PROJECT_ID = "project_id" // Foreign key column referencing projects_project_id
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
    fun getUser(username: String, password: String): User? {
        val db = this.readableDatabase
        val cursor: Cursor = db.rawQuery("SELECT * FROM $TABLE_USERS WHERE $KEY_USERNAME=? AND $KEY_PASSWORD=?", arrayOf(username, password))

        return if (cursor.moveToFirst()) {
            val user = User(
                cursor.getInt(cursor.getColumnIndex(KEY_ID)),
                cursor.getString(cursor.getColumnIndex(KEY_EMAIL)),
                cursor.getString(cursor.getColumnIndex(KEY_USERNAME)),
                cursor.getString(cursor.getColumnIndex(KEY_PASSWORD))
            )
            cursor.close()
            user
        } else {
            cursor?.close()
            null
        }
    }

    fun addProject(title: String, status: String, userId: Int): Long {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put(KEY_PROJECT_TITLE, title)
            put(KEY_PROJECT_STATUS, status)
            put(KEY_USER_ID, userId)
        }
        val id = db.insert(TABLE_PROJECTS, null, values)
        db.close()
        return id
    }
    @SuppressLint("Range")
    fun getProjects(userId: Int): List<Project> {
        val projects = mutableListOf<Project>()
        val db = this.readableDatabase
        val query = "SELECT * FROM $TABLE_PROJECTS WHERE $KEY_USER_ID = ?"
        val cursor = db.rawQuery(query, arrayOf(userId.toString()))

        cursor.use { cursor ->
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
    fun getTasks(projectId: Int): List<Task> {
        val tasks = mutableListOf<Task>()
        val db = this.readableDatabase
        val query = "SELECT * FROM $TABLE_TASKS WHERE $KEY_TASK_PROJECT_ID = ?"
        val cursor = db.rawQuery(query, arrayOf(projectId.toString()))

        cursor.use { cursor ->
            while (cursor.moveToNext()) {
                val taskId = cursor.getInt(cursor.getColumnIndex(KEY_TASK_ID))
                val title = cursor.getString(cursor.getColumnIndex(KEY_TASK_TITLE))
                val due = cursor.getString(cursor.getColumnIndex(KEY_TASK_DUE))
                val time = cursor.getString(cursor.getColumnIndex(KEY_TASK_TIME))
                val description = cursor.getString(cursor.getColumnIndex(KEY_TASK_DESC))
                val status = cursor.getString(cursor.getColumnIndex(KEY_TASK_STATUS))
                val priority = cursor.getString(cursor.getColumnIndex(KEY_TASK_PRIORITY))

                val task = Task(taskId, title, due, time, description, status, priority, projectId)
                tasks.add(task)
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

}
