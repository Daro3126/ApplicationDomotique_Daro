package com.devmobile.applicationdomotique.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.Button
import android.widget.TextView
import com.devmobile.applicationdomotique.R
import com.devmobile.applicationdomotique.activity.AccessHouseActivity
import com.devmobile.applicationdomotique.activity.ListDevicesActivity
import com.devmobile.applicationdomotique.activity.ListHouseAccessUserActivity
import com.devmobile.applicationdomotique.activity.ListHousesActivity
import com.devmobile.applicationdomotique.data.HouseData

class HouseAdapter (
    private val  context : Context,
    private val dataSource : ArrayList<HouseData>

): BaseAdapter(){

    private val inflater : LayoutInflater =
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
        val rowView = convertView ?: inflater.inflate(R.layout.house, parent, false)
        val houseId = rowView.findViewById<TextView>(R.id.txtHouseId)
        val owner = rowView.findViewById<TextView>(R.id.txtOwner)
        val houseData = getItem(position) as HouseData
        val buttonAccess = rowView.findViewById<Button>(R.id.btAcess)
        val buttonList = rowView.findViewById<Button>(R.id.btListUser)
        val buttonListPeri = rowView.findViewById<Button>(R.id.btListPeri)


        houseId.text= "Maison "+houseData.houseId.toString()
        if (houseData.owner==true){
            owner.text= "Vous êtes propriétaire de la maison "+houseData.houseId.toString()
            buttonAccess.visibility = View.VISIBLE
            buttonList.visibility = View.VISIBLE
            buttonListPeri.visibility = View.VISIBLE

            buttonAccess.setOnClickListener {
                val intent = Intent(context, AccessHouseActivity::class.java)
                intent.putExtra("HOUSE_ID", houseData.houseId.toString())
                if (context is ListHousesActivity) {
                    val token = context.intent.getStringExtra("USER_TOKEN")
                    val login = context.intent.getStringExtra("USER_LOGIN")

                    intent.putExtra("USER_TOKEN", token)
                    intent.putExtra("USER_LOGIN", login)
                }

                context.startActivity(intent)
            }

            buttonList.setOnClickListener {
                val intent = Intent(context, ListHouseAccessUserActivity::class.java)
                intent.putExtra("HOUSE_ID", houseData.houseId.toString())
                if (context is ListHousesActivity) {
                    val token = context.intent.getStringExtra("USER_TOKEN")
                    val login = context.intent.getStringExtra("USER_LOGIN")
                    intent.putExtra("USER_TOKEN", token)
                    intent.putExtra("USER_LOGIN", login)

                }

                context.startActivity(intent)
            }

            buttonListPeri.setOnClickListener {
                val intent = Intent(context, ListDevicesActivity::class.java)
                intent.putExtra("HOUSE_ID", houseData.houseId.toString())
                if (context is ListHousesActivity) {
                    val token = context.intent.getStringExtra("USER_TOKEN")
                    val login = context.intent.getStringExtra("USER_LOGIN")
                    intent.putExtra("USER_TOKEN", token)
                    intent.putExtra("USER_LOGIN", login)

                }

                context.startActivity(intent)
            }



        }
        else{
            owner.text= "Vous avez access à  la maison  "+houseData.houseId.toString()
            buttonAccess.visibility = View.GONE
            buttonList.visibility = View.GONE
            buttonListPeri.visibility = View.GONE
        }





        return rowView
    }
}