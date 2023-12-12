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
        private const val KEY_ID = "userId"
        private const val KEY_EMAIL = "email"
        private const val KEY_USERNAME = "username"
        private const val KEY_PASSWORD = "password"

        //Projects Table
        private const val KEY_PROJECT_ID = "project_id"
        private const val KEY_PROJECT_TITLE = "project_title"
        private const val KEY_PROJECT_STATUS = "project_status"
        private const val KEY_USER_ID = "userId" // Foreign key column referencing users_id

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
            cursor.close()
            null
        }
    }

}
