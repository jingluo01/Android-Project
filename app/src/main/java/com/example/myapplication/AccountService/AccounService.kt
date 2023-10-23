package com.example.myapplication.AccountService

import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.widget.EditText
import android.widget.ListView
import android.widget.Toast
import com.example.myapplication.Account.Account
import com.example.myapplication.AccountDao.AccountDao
import com.example.myapplication.Adapter.AccountAdapter
import com.example.myapplication.R

class AccounService(var context: Context):AccountAdapter.Handle {
    var dao:AccountDao
    var accountList:MutableList<Account>
    lateinit var accountAdapter: AccountAdapter
    init {
        dao = AccountDao(context)
        accountList = mutableListOf()

    }
    fun setAdapter(listView:ListView){
        accountList = dao.queryAll()
        accountAdapter = AccountAdapter(context,accountList,this)
        listView.adapter = accountAdapter
    }

    fun addAccount(account: Account){
        dao.insert(account)
        accountList.add(0,account)
        accountAdapter.notifyDataSetChanged()
    }
    override fun deleteAccount(index:Int){
        var account:Account = accountList.get(index)
        var dialog: AlertDialog.Builder = AlertDialog.Builder(context).setTitle("确认删除").setMessage("确定要删除该产品吗？")
        dialog.setPositiveButton("确定") { dialogInterface, i ->
            if (dao.delete(account) > 0) {
                accountList.remove(account)
                accountAdapter.notifyDataSetChanged()
            }
        }
        dialog.setNegativeButton("取消") { dialogInterface, i -> }
        dialog.show()
    }
    override fun raiseAccount(index:Int){
        var account:Account = accountList.get(index)
        account.price = account.price + 1// 数字加1
        dao.update(account)
        accountAdapter.notifyDataSetChanged()
    }
    override fun downAccount(index:Int){
        var account:Account = accountList.get(index)
        account.price = account.price - 1// 数字加1
        dao.update(account)
        accountAdapter.notifyDataSetChanged()
    }
    fun updataAccount(index:Int) {
        var account:Account = accountList.get(index)
        var view: View = LayoutInflater.from(context).inflate(R.layout.updata_item, null)
        var nameEditText: EditText = view.findViewById<EditText>(R.id.updata_name)
        var priceEditText: EditText = view.findViewById<EditText>(R.id.updata_price)
        nameEditText.setText(account.name)
        priceEditText.setText(java.lang.String.valueOf(account.price))
        var dialog: androidx.appcompat.app.AlertDialog.Builder = androidx.appcompat.app.AlertDialog.Builder(context).setTitle("修改商品信息").setView(view)
        dialog.setPositiveButton("保存"){dialogInterface, i ->
            var name: String = nameEditText.getText().toString()
            var price: Int = priceEditText.getText().toString().toInt()
            account.name = name
            account.price = price
            if (dao.update(account) > 0) {
                Toast.makeText(context, "updata", Toast.LENGTH_SHORT).show()
            }
            accountAdapter.notifyDataSetChanged()

        }
        dialog.setNegativeButton("取消") {dialogInterface, i ->}
        dialog.show()
    }
    fun selectAccount() {
        var view: View = LayoutInflater.from(context).inflate(R.layout.look_dialog, null)
        var dialog:AlertDialog.Builder = AlertDialog.Builder(context).setTitle("查找商品").setView(view)
        dialog.setPositiveButton("确定"){dialogInterface, i ->
            val nameEditText: EditText = view.findViewById<EditText>(R.id.et_look_name)
            accountList = dao.lookAccountByName(nameEditText.getText().toString())
            accountAdapter.notifyDataSetChanged()
        }
        dialog.setNegativeButton("取消"){dialogInterface, i ->}
        dialog.show()
    }
}