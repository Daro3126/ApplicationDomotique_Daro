package com.devmobile.applicationdomotique.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ListView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.devmobile.applicationdomotique.Api
import com.devmobile.applicationdomotique.R
import com.devmobile.applicationdomotique.adapter.DeviceAdapter
import com.devmobile.applicationdomotique.data.CommandData
import com.devmobile.applicationdomotique.data.DeviceData
import com.devmobile.applicationdomotique.data.DevicesList
import com.devmobile.applicationdomotique.data.UrlData

class ListDevicesActivity : AppCompatActivity() {
    private val devices = ArrayList<DeviceData>()
    private lateinit var deviceAdapter: DeviceAdapter
    private var houseId: Int = 0
    private var token: String? = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list_devices)

        token = intent.getStringExtra("USER_TOKEN")
        val id = intent.getStringExtra("HOUSE_ID")
        houseId = id.toString().toInt()

        val btnAllumerTous = findViewById<Button>(R.id.btnAllumert)
        val btnEteindreTous = findViewById<Button>(R.id.btnEteindret)
        val btnOuvrirTous = findViewById<Button>(R.id.btnOuvrirt)
        val btnFermerTous = findViewById<Button>(R.id.btnFermert)
        val btnStopperTous = findViewById<Button>(R.id.btnStoppert)

        initDeviceList()
        listDevices(token, houseId)



        btnAllumerTous.setOnClickListener {
            commandDevices("TURN ON")
        }

        btnEteindreTous.setOnClickListener {
            commandDevices("TURN OFF")
        }

        btnOuvrirTous.setOnClickListener {
            commandDevices("OPEN")
        }

        btnFermerTous.setOnClickListener {
            commandDevices("CLOSE")
        }

        btnStopperTous.setOnClickListener {
            commandDevices("STOP")
        }
    }

    private fun initDeviceList() {
        val listView = findViewById<ListView>(R.id.lstDevice)
        deviceAdapter = DeviceAdapter(this, devices)
        listView.adapter = deviceAdapter
    }

    private fun listDevices(token: String?, houseId: Int) {
        val urls = UrlData()
        val url = urls.device(houseId)
        Api().get<DevicesList>(
            url,
            ::onDeviceListSuccess,
            token
        )
    }

    private fun onDeviceListSuccess(responseCode: Int, data: DevicesList?) {
        runOnUiThread {
            if (responseCode == 200) {
                if (data != null) {
                    if (data.devices.isNotEmpty()) {
                        devices.addAll(data.devices)
                        deviceAdapter.notifyDataSetChanged()
                        Toast.makeText(this, "Données récupérées avec succès", Toast.LENGTH_SHORT).show()
                    }
                }
            }

            if (responseCode == 400) {
                Toast.makeText(this, "Les données fournies sont incorrectes", Toast.LENGTH_SHORT).show()
            }

            if (responseCode == 403) {
                Toast.makeText(this, "Accès interdit (token invalide ou utilisateur non autorisé)", Toast.LENGTH_SHORT).show()
            }

            if (responseCode == 500) {
                Toast.makeText(this, "Erreur du serveur", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun commandDevices(command: String) {
        for (device in devices) {
            if (command == "TURN ON" || command == "TURN OFF") {
                if (device.type == "light") {
                    command(houseId, command,device.id, token)
                }
            }

            if (command == "OPEN" || command == "CLOSE" || command == "STOP") {
                if (device.type == "rolling shutter") {
                    command(houseId, command,device.id, token)
                }
                if (device.type == "garage door") {
                    command(houseId, command,device.id, token)
                }
            }
        }
    }

    fun command(houseId: Int, command: String,deviceId: String, token: String?) {

        val urls = UrlData()
        val url = urls.command(houseId, deviceId)
        val data = CommandData(command)
        Api().post<CommandData>(url,
            data,
            ::commandSuccess,
            token)

    }
    private fun commandSuccess(responseCode: Int) {
        runOnUiThread {
            if (responseCode == 200) {
                val intent = Intent(this, ListDevicesActivity::class.java)
                intent.putExtra("HOUSE_ID", houseId.toString())
                intent.putExtra("USER_TOKEN", token)
                startActivity(intent)
                Toast.makeText(this,
                    "Requête acceptée",
                    Toast.LENGTH_SHORT).show()
                finish()
            }

            if (responseCode == 403) {
                Toast.makeText(this,
                    "Accès interdit (token invalide ou non-propriétaire)",
                    Toast.LENGTH_SHORT).show()
            }



            if (responseCode == 500) {
                Toast.makeText(this,
                    "Erreur du serveur",
                    Toast.LENGTH_SHORT).show()
            }
        }
    }


}

