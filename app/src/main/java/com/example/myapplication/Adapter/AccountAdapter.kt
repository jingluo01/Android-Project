package com.example.myapplication.Adapter

import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.example.myapplication.Account.Account
import com.example.myapplication.AccountDao.AccountDao
import com.example.myapplication.R
import java.lang.String

class AccountAdapter(var context: Context,var accoutlist:MutableList<Account>) :BaseAdapter(){
    private var accountList: MutableList<Account>
    private var dao: AccountDao
    init {
        this.accountList = accoutlist
        this.dao = AccountDao(context)
    }

    override fun getCount(): Int {
        return accountList.size
    }

    override fun getItem(position: Int): Any {
        return accountList[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        var view = convertView
        var holder: ViewHolder
        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.list_item, null)
            holder = ViewHolder()
            holder.idTextView = view.findViewById(R.id.tv_list_id)
            holder.nameTextView = view.findViewById(R.id.tv_list_name)
            holder.priceTextView = view.findViewById(R.id.tv_list_price)
            holder.upImageView = view.findViewById(R.id.image_list_up)
            holder.downImageView = view.findViewById(R.id.image_list_down)
            holder.delImageView = view.findViewById(R.id.image_list_delete)
            view.tag = holder
        } else {
            holder = view.tag as ViewHolder
        }
        var account: Account = accountList[position]
        holder.idTextView.text = String.valueOf(account.id)
        holder.nameTextView.text = account.name
        holder.priceTextView.text = String.valueOf(account.price)
//        设置ImageView的点击事件监听器
        holder.upImageView.setOnClickListener {
            account.price = account.price + 1// 数字加1
            dao.update(account)
            notifyDataSetChanged()
            Toast.makeText(context, "+1", Toast.LENGTH_SHORT).show()
        }
        holder.downImageView.setOnClickListener {
            account.price = account.price - 1 // 数字减1
            dao.update(account)
            notifyDataSetChanged()
            Toast.makeText(context, "-1", Toast.LENGTH_SHORT).show()
        }
        holder.delImageView.setOnClickListener { deleted(account) }
        return view!!
    }
    fun deleted(account: Account) {
        var dialog: AlertDialog.Builder = AlertDialog.Builder(context).setTitle("确认删除").setMessage("确定要删除该产品吗？")
        dialog.setPositiveButton("确定") { dialogInterface, i ->
            if (dao.delete(account) > 0) {
                Toast.makeText(context, "del", Toast.LENGTH_SHORT).show()
                accountList.remove(account)
                notifyDataSetChanged()
            }
            dialog.setNegativeButton("取消") { dialogInterface, i -> }
            dialog.show()
        }
    }
    private  class ViewHolder {
        lateinit var idTextView: TextView
        lateinit var nameTextView: TextView
        lateinit var priceTextView: TextView
        lateinit var upImageView: ImageView
        lateinit var downImageView: ImageView
        lateinit var delImageView: ImageView
    }
}