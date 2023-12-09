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
        private const val TABLE_USERS = "users"
        private const val KEY_ID = "id"
        private const val KEY_EMAIL = "email"
        private const val KEY_USERNAME = "username"
        private const val KEY_PASSWORD = "password"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        val createUserTableQuery = ("CREATE TABLE IF NOT EXISTS $TABLE_USERS ("
                + "$KEY_ID INTEGER PRIMARY KEY,"
                + "$KEY_EMAIL TEXT,"
                + "$KEY_USERNAME TEXT,"
                + "$KEY_PASSWORD TEXT)")

        db?.execSQL(createUserTableQuery)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_USERS")
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

    fun updateUserPassword(username: String, newPassword: String): Int {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put(KEY_PASSWORD, newPassword)
        }
        val affectedRows = db.update(TABLE_USERS, values, "$KEY_USERNAME=?", arrayOf(username))
        db.close()
        return affectedRows
    }

    fun deleteUser(username: String): Int {
        val db = this.writableDatabase
        val affectedRows = db.delete(TABLE_USERS, "$KEY_USERNAME=?", arrayOf(username))
        db.close()
        return affectedRows
    }
}
