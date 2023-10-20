package com.example.myapplication.Util

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DBhelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
    override fun onCreate(db: SQLiteDatabase) {
        val createTableQuery =
            "CREATE TABLE account (id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT, price INTEGER)"
        db.execSQL(createTableQuery)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        // 如果需要升级数据库时的操作，可以在这里实现
    }

    companion object {
        private const val DATABASE_NAME = "product.db"
        private const val DATABASE_VERSION = 1
    }
}
