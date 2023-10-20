package com.example.myapplication.Activity

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.ContextMenu
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.EditText
import android.widget.ListView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.Account.Account
import com.example.myapplication.AccountDao.AccountDao
import com.example.myapplication.R
import com.example.myapplication.adapter.AccountAdapter

class MainActivity : AppCompatActivity() {
    private lateinit var et_name: EditText
    private lateinit var et_price: EditText
    private lateinit var listView: ListView
    private lateinit var accountList: MutableList<Account>
    private lateinit var accountAdapter: AccountAdapter
    private  val accountDao: AccountDao = AccountDao(this)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        et_name = findViewById(R.id.et_name)
        et_price = findViewById(R.id.et_pirce)
        listView = findViewById(R.id.list_item)
        registerForContextMenu(listView)
        accountList = mutableListOf()
        accountList = accountDao.queryAll()
        accountAdapter = AccountAdapter(this, accountList, accountDao)
        listView.adapter = accountAdapter
    }

    override fun onCreateContextMenu(menu: ContextMenu, v: View?, menuInfo: ContextMenu.ContextMenuInfo?) {
        super.onCreateContextMenu(menu, v, menuInfo)
        menu.setHeaderTitle("提示")
        menu.setHeaderIcon(R.mipmap.ic_launcher)
        menu.add(0, 1, 100, "删除")
        menu.add(0, 2, 200, "修改")
        menu.add(0, 3, 300, "查找")
    }

    override fun onContextItemSelected(item: MenuItem): Boolean {
        var menuInfo: AdapterView.AdapterContextMenuInfo? = item.menuInfo as AdapterView.AdapterContextMenuInfo
        var id: Int = menuInfo!!.id.toInt()
        var account: Account = accountList[id]
        when (item.itemId) {
            1 -> deleteAccount(account)
            2 -> updataAccount(account)
            3 -> selectAccount()
        }
        return super.onContextItemSelected(item)
    }

    fun deleteAccount(account: Account) {
            accountAdapter.deleted(account)
    }

    @SuppressLint("SuspiciousIndentation")
    fun updataAccount(account: Account) {
        var view: View = LayoutInflater.from(this).inflate(R.layout.updata_item, null)
        var nameEditText: EditText = view.findViewById<EditText>(R.id.updata_name)
        var priceEditText: EditText = view.findViewById<EditText>(R.id.updata_price)
        nameEditText.setText(account.name)
        priceEditText.setText(java.lang.String.valueOf(account.price))
        var dialog:AlertDialog.Builder = AlertDialog.Builder(this).setTitle("修改商品信息").setView(view)
            dialog.setPositiveButton("保存"){dialogInterface, i ->
                val name: String = nameEditText.getText().toString()
                val price: Int = priceEditText.getText().toString().toInt()
                account.name = name
                account.price = price
                if (accountDao.update(account) > 0) {
                    Toast.makeText(this@MainActivity, "updata", Toast.LENGTH_SHORT).show()
                }
                accountAdapter.notifyDataSetChanged()

            }
            dialog.setNegativeButton("取消") {dialogInterface, i ->}
            dialog.show()
    }

    @SuppressLint("SuspiciousIndentation")
    fun selectAccount() {
        val view: View = LayoutInflater.from(this).inflate(R.layout.look_dialog, null)
        var dialog:AlertDialog.Builder = AlertDialog.Builder(this).setTitle("查找商品").setView(view)
            dialog.setPositiveButton("确定"){dialogInterface, i ->
                val nameEditText: EditText = view.findViewById<EditText>(R.id.et_look_name)
                accountList = accountDao.lookAccountByName(nameEditText.getText().toString())
                accountAdapter.notifyDataSetChanged()
            }
        dialog.setNegativeButton("取消"){dialogInterface, i ->}
        dialog.show()
    }

    fun clear() {
        et_name.text = null
        et_price.text = null
    }

    fun add(view: View?) {
        var name: String = et_name.getText().toString()
        var price: Int = et_price.getText().toString().toInt()
        var account = Account(name, price)
        if (accountDao.insert(account) > 0) {
            Toast.makeText(this@MainActivity, "add", Toast.LENGTH_SHORT).show()
        }
        accountList.add(account)
        accountAdapter.notifyDataSetChanged()
        clear()
    }

}