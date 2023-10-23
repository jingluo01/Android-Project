package com.example.myapplication.AccountDao

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import com.example.myapplication.Account.Account
import com.example.myapplication.Util.DBhelper

class AccountDao(var context: Context) {
    var dBhelper: DBhelper
    var accountlist:MutableList<Account>
    init {
        dBhelper = DBhelper(context)
        accountlist = mutableListOf()
    }
    fun insert(account: Account): Long {
        var db: SQLiteDatabase = dBhelper.getWritableDatabase()
        var values = ContentValues()
        values.put("name", account.name)
        values.put("price", account.price)
        account.id = db.insert("account", null, values)
        db.close()
        return account.id
    }

    fun delete(account: Account): Int {
        var db: SQLiteDatabase = dBhelper.getWritableDatabase()
        var count: Int = db.delete("account", "id = ?",
            arrayOf<String>(java.lang.String.valueOf(account.id))
        )
        db.close()
        return count
    }

    fun update(account: Account): Int {
        var db: SQLiteDatabase = dBhelper.getWritableDatabase()
        var values = ContentValues()
        values.put("name", account.name)
        values.put("price", account.price)
        var count: Int = db.update("account", values, "id = ?",
            arrayOf<String>(java.lang.String.valueOf(account.id))
        )
        db.close()
        return count
    }
    @SuppressLint("Range")
    fun queryAll(): MutableList<Account> {
        var database: SQLiteDatabase = dBhelper.getReadableDatabase()
        var cursor: Cursor = database.query("account", null, null, null, null, null, "price DESC")
        while (cursor.moveToNext()) {
            var id:Long = cursor.getLong(cursor.getColumnIndex("id"))
            var name:String = cursor.getString(cursor.getColumnIndex("name"))
            var price:Int = cursor.getInt(cursor.getColumnIndex("price"))
            var account: Account = Account(id, name, price)
            accountlist.add(account)
        }
        database.close()
        cursor.close()
        return accountlist
    }
    @SuppressLint("Range")
    fun lookAccountByName(accountName: String): MutableList<Account> {
        var database: SQLiteDatabase = dBhelper.getReadableDatabase()
        var cursor: Cursor = database.query("account", null, "name like ?", arrayOf("%$accountName%"),
            null, null, null, null
        )
        accountlist.clear()
        while (cursor.moveToNext()) {
            var id:Long = cursor.getLong(cursor.getColumnIndex("id"))
            var name:String = cursor.getString(cursor.getColumnIndex("name"))
            var price:Int = cursor.getInt(cursor.getColumnIndex("price"))
            var account: Account = Account(id, name, price)
            accountlist.add(account)
        }
        database.close()
        cursor.close()
        return accountlist
    }
}