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
        val btnAllumerTous = findViewById<Button>(R.id.btnAllumert)
        val btnEteindreTous = findViewById<Button>(R.id.btnEteindret)
        val btnOuvrirTous = findViewById<Button>(R.id.btnOuvrirt)
        val btnFermerTous = findViewById<Button>(R.id.btnFermert)
        val btnStopperTous = findViewById<Button>(R.id.btnStoppert)

        houseId = id.toString().toInt()

        initDeviceList()
        listDevices(token, houseId)
        Log.d("DEBUG", "Nombre d'appareils récupérés: ${devices.size}")

        btnAllumerTous.setOnClickListener {
            commandDevices("TURN ON")
        }



    }

    private fun initDeviceList() {
        val listView = findViewById<ListView>(R.id.lstDevice)
        deviceAdapter = DeviceAdapter(this, devices)
        listView.adapter = deviceAdapter
    }

    private fun  listDevices(token: String?, houseId: Int) {
        val urls = UrlData()
        val url = urls.device(houseId)
        Api().get< DevicesList>(
            url,
            ::onDeviceListSuccess,
            token
        )
    }
    private fun commandDevices(command: String) {
        for (device in devices) {
            val intent = Intent(this, CommandActivity::class.java)
            intent.putExtra("DEVICE_ID", device.id)
            intent.putExtra("USER_TOKEN", token)
            intent.putExtra("HOUSE_ID", houseId.toString())
            intent.putExtra("COMMAND", command)
            startActivity(intent)
        }
    }


    private fun onDeviceListSuccess(responseCode: Int, data: DevicesList?)  {
        runOnUiThread {
            runOnUiThread {
                if (responseCode == 200) {

                    if (data != null && data.devices.isNotEmpty()) {
                        devices.addAll(data.devices)
                        deviceAdapter.notifyDataSetChanged()

                        Toast.makeText(this,
                            "Données récupérées avec succès",
                            Toast.LENGTH_SHORT).show()
                    }
                }
                if (responseCode == 400) {
                    Toast.makeText(
                        this,
                        "Les données fournies sont incorrectes",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                if (responseCode == 403) {
                    Toast.makeText(
                        this, "Accès interdit (token invalide ou ne correspondant pas au\n" +
                                " Propriétaire ou a un invité de la maison)", Toast.LENGTH_SHORT
                    ).show()
                }
                if (responseCode == 500) {
                    Toast.makeText(this,
                        "Erreur du serveur",
                        Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}

