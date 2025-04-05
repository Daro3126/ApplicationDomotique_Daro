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
import com.devmobile.applicationdomotique.activity.CommandActivity
import com.devmobile.applicationdomotique.activity.ListDevicesActivity
import com.devmobile.applicationdomotique.activity.ListHousesActivity
import com.devmobile.applicationdomotique.data.DeviceData


data class DeviceAdapter(
    private val context: Context,
    private val dataSource: ArrayList<DeviceData>
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
        val rowView = convertView ?: inflater.inflate(R.layout.device, parent, false)
        val id = rowView.findViewById<TextView>(R.id.id)
        val type = rowView.findViewById<TextView>(R.id.type)
        val etat = rowView.findViewById<TextView>(R.id.etat)
        val btEteinde = rowView.findViewById<Button>(R.id.btnEteindre)
        val btAllumer = rowView.findViewById<Button>(R.id.btnAllumer)
        val btOuvrir = rowView.findViewById<Button>(R.id.btnOuvrir)
        val btFermer = rowView.findViewById<Button>(R.id.btnFermer)
        val btStopper = rowView.findViewById<Button>(R.id.btnStopper)
        val deviceData = getItem(position) as DeviceData

        id.text = "ID : "+deviceData.id


        if(deviceData.type=="light"){
            type.text = "TYPE : Lumière"
            if(deviceData.power==1){
                etat.text ="ETAT : Lumière allumée "
                btEteinde.visibility = View.VISIBLE
                btFermer.visibility = View.GONE
                btAllumer.visibility = View.GONE
                btOuvrir.visibility = View.GONE
                btStopper.visibility = View.GONE
            }
            if(deviceData.power==0){
                etat.text ="ETAT : Lumière éteinte"
                btEteinde.visibility = View.GONE
                btFermer.visibility = View.GONE
                btAllumer.visibility = View.VISIBLE
                btOuvrir.visibility = View.GONE
                btStopper.visibility = View.GONE
            }

        }
        if(deviceData.type=="rolling shutter"){
            type.text = "TYPE : Volet Roulant"
            if(deviceData.openingMode==0){
                etat.text ="ETAT : Volet ouvert "
                btEteinde.visibility = View.GONE
                btFermer.visibility = View.VISIBLE
                btAllumer.visibility = View.GONE
                btOuvrir.visibility = View.GONE
                btStopper.visibility = View.VISIBLE
            }
            if(deviceData.openingMode==1){
                etat.text ="ETAT : Volet fermé "
                btEteinde.visibility = View.GONE
                btFermer.visibility = View.GONE
                btAllumer.visibility = View.GONE
                btOuvrir.visibility = View.VISIBLE
                btStopper.visibility = View.VISIBLE
            }
            if(deviceData.openingMode==2){
                etat.text ="ETAT : Volet stopé "
                btEteinde.visibility = View.GONE
                btFermer.visibility = View.VISIBLE
                btAllumer.visibility = View.GONE
                btOuvrir.visibility = View.VISIBLE
                btStopper.visibility = View.GONE
            }
        }
        if(deviceData.type=="sliding shutter"){
            type.text = "TYPE : Volet Coulissant"
            if(deviceData.openingMode==0){
                etat.text ="ETAT : Volet ouvert "
                btEteinde.visibility = View.GONE
                btFermer.visibility = View.VISIBLE
                btAllumer.visibility = View.GONE
                btOuvrir.visibility = View.GONE
                btStopper.visibility = View.VISIBLE
            }
            if(deviceData.openingMode==1){
                etat.text ="ETAT : Volet fermé "
                btEteinde.visibility = View.GONE
                btFermer.visibility = View.GONE
                btAllumer.visibility = View.GONE
                btOuvrir.visibility = View.VISIBLE
                btStopper.visibility = View.VISIBLE
            }
            if(deviceData.openingMode==2){
                etat.text ="ETAT : Volet stoppé "
                btEteinde.visibility = View.GONE
                btFermer.visibility = View.VISIBLE
                btAllumer.visibility = View.GONE
                btOuvrir.visibility = View.VISIBLE
                btStopper.visibility = View.GONE
            }
        }
        if(deviceData.type=="garage door"){
            type.text = "TYPE : Porte garage"
            if(deviceData.openingMode==0){
                etat.text ="ETAT : Porte garage ouverte "
                btEteinde.visibility = View.GONE
                btFermer.visibility = View.VISIBLE
                btAllumer.visibility = View.GONE
                btOuvrir.visibility = View.GONE
                btStopper.visibility = View.VISIBLE
            }
            if(deviceData.openingMode==1){
                etat.text ="ETAT : Porte garage fermée "
                btEteinde.visibility = View.GONE
                btFermer.visibility = View.GONE
                btAllumer.visibility = View.GONE
                btOuvrir.visibility = View.VISIBLE
                btStopper.visibility = View.VISIBLE
            }
            if(deviceData.openingMode==2){
                etat.text ="ETAT : Porte garage stoppée "
                btEteinde.visibility = View.GONE
                btFermer.visibility = View.VISIBLE
                btAllumer.visibility = View.GONE
                btOuvrir.visibility = View.VISIBLE
                btStopper.visibility = View.GONE
            }
        }
        btAllumer.setOnClickListener {
            val intent = Intent(context, CommandActivity::class.java)
            intent.putExtra("DEVICE_ID", deviceData.id)
            if (context is ListDevicesActivity) {
                val token = context.intent.getStringExtra("USER_TOKEN")
                val houseId= context.intent.getStringExtra("HOUSE_ID")
                val command :String="TURN ON"

                intent.putExtra("USER_TOKEN", token)
                intent.putExtra("HOUSE_ID", houseId)
                intent.putExtra("COMMAND", command)
            }

            context.startActivity(intent)
        }
        btFermer.setOnClickListener {
            val intent = Intent(context, CommandActivity::class.java)
            intent.putExtra("DEVICE_ID", deviceData.id)
            if (context is ListDevicesActivity) {
                val token = context.intent.getStringExtra("USER_TOKEN")
                val houseId= context.intent.getStringExtra("HOUSE_ID")
                val command :String="CLOSE"

                intent.putExtra("USER_TOKEN", token)
                intent.putExtra("HOUSE_ID", houseId)
                intent.putExtra("COMMAND", command)
            }

            context.startActivity(intent)
        }
        btOuvrir.setOnClickListener {
            val intent = Intent(context, CommandActivity::class.java)
            intent.putExtra("DEVICE_ID", deviceData.id)
            if (context is ListDevicesActivity) {
                val token = context.intent.getStringExtra("USER_TOKEN")
                val houseId= context.intent.getStringExtra("HOUSE_ID")
                val command :String="OPEN"

                intent.putExtra("USER_TOKEN", token)
                intent.putExtra("HOUSE_ID", houseId)
                intent.putExtra("COMMAND", command)
            }

            context.startActivity(intent)
        }
        btEteinde.setOnClickListener {
            val intent = Intent(context, CommandActivity::class.java)
            intent.putExtra("DEVICE_ID", deviceData.id)
            if (context is ListDevicesActivity) {
                val token = context.intent.getStringExtra("USER_TOKEN")
                val houseId= context.intent.getStringExtra("HOUSE_ID")
                val command :String="TURN OFF"

                intent.putExtra("USER_TOKEN", token)
                intent.putExtra("HOUSE_ID", houseId)
                intent.putExtra("COMMAND", command)
            }

            context.startActivity(intent)
        }
        btStopper.setOnClickListener {
            val intent = Intent(context, CommandActivity::class.java)
            intent.putExtra("DEVICE_ID", deviceData.id)
              if (context is ListDevicesActivity) {
                val token = context.intent.getStringExtra("USER_TOKEN")
                val houseId= context.intent.getStringExtra("HOUSE_ID")
                val command :String="STOP"

                intent.putExtra("USER_TOKEN", token)
                intent.putExtra("HOUSE_ID", houseId)
                intent.putExtra("COMMAND", command)
            }

            context.startActivity(intent)
        }
        return rowView
    }

}