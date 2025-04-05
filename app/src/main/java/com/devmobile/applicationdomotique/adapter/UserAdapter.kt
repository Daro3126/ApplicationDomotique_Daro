package com.devmobile.applicationdomotique.adapter

import android.content.Context
import android.content.Intent
import android.service.autofill.UserData
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.devmobile.applicationdomotique.R
import com.devmobile.applicationdomotique.activity.DeleteAcessHouseActivity
import com.devmobile.applicationdomotique.activity.ListHouseAccessUserActivity
import com.devmobile.applicationdomotique.data.UserAccessData

class UserAdapter(
    private val context: Context,
    private val dataSource: ArrayList<UserAccessData>
) : BaseAdapter() {

    private val inflater: LayoutInflater =
        context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

    override fun getCount(): Int {
        return dataSource.size
    }

    override fun getItem(position: Int): Any {
        return dataSource[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val rowView = convertView ?: inflater.inflate(R.layout.user, parent, false)
        val user = rowView.findViewById<TextView>(R.id.txtUser)
        val userAccessData = getItem(position) as UserAccessData
        val btdelete= rowView.findViewById<ImageView>(R.id.btnDelete)



        if (userAccessData.owner == 0) {
            user.text = userAccessData.userLogin +" a acess a ta maison"
            btdelete.visibility= View.VISIBLE
        }
        else{
            user.text = userAccessData.userLogin+" tu  es propriètaire"
            btdelete.visibility= View.GONE
        }
        btdelete.setOnClickListener {
            val intent = Intent(context, DeleteAcessHouseActivity::class.java)
            intent.putExtra("USER_LOGIN", userAccessData.userLogin)

            if (context is ListHouseAccessUserActivity) {
                val token = context.intent.getStringExtra("USER_TOKEN")
                val houseId = context.intent.getStringExtra("HOUSE_ID")
                intent.putExtra("USER_TOKEN", token)
                intent.putExtra("HOUSE_ID", houseId)
            }

            context.startActivity(intent)
        }


        return rowView
    }
}