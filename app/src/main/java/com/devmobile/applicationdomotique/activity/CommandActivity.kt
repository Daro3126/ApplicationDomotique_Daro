package com.devmobile.applicationdomotique.activity

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.devmobile.applicationdomotique.Api
import com.devmobile.applicationdomotique.data.CommandData
import com.devmobile.applicationdomotique.data.UrlData

class CommandActivity : AppCompatActivity() {
    private var houseId: Int = 0
    private var token: String? = ""
    private var command: String = ""
    private  var deviceId : String = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)



        token = intent.getStringExtra("USER_TOKEN")
        command = intent.getStringExtra("COMMAND").toString()
        deviceId = intent.getStringExtra("DEVICE_ID").toString()
        houseId = intent.getStringExtra("HOUSE_ID").toString().toInt()



        command(houseId,command,deviceId,token)
    }

    private fun command(houseId: Int, command: String,deviceId: String, token: String?) {

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



