package com.devmobile.applicationdomotique.activity

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.devmobile.applicationdomotique.Api
import com.devmobile.applicationdomotique.R
import com.devmobile.applicationdomotique.data.AccessData
import com.devmobile.applicationdomotique.data.UrlData

class AccessHouseActivity : AppCompatActivity() {

    private var houseId: Int = 0
    private var token: String? = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_access_house)


        token = intent.getStringExtra("USER_TOKEN")
        val id = intent.getStringExtra("HOUSE_ID")

        val btnValid = findViewById<Button>(R.id.btnvalid)
        btnValid.setOnClickListener {
            val login = findViewById<EditText>(R.id.user).text.toString()

            if (id.isNullOrEmpty()) {
                Toast.makeText(this,
                    "Erreur : ID maison manquant",
                     Toast.LENGTH_SHORT).show()

            }

            houseId = id.toString().toInt()
            giveAccessHouse(houseId, login, token)

        }
    }

    private fun giveAccessHouse(houseId: Int, userLogin: String, token: String?) {

        val urls = UrlData()
        val url = urls.accessHouse(houseId)
        val data = AccessData(userLogin)
        Api().post(
            url,
            data,
            ::onGiveAccessSuccess,
            token
        )
    }

    private fun onGiveAccessSuccess(responseCode: Int) {
        runOnUiThread {
            if (responseCode == 200) {
                val intent = Intent(this, ListHousesActivity::class.java)

                intent.putExtra("HOUSE_ID", houseId)
                intent.putExtra("USER_TOKEN", token)

                startActivity(intent)
                Toast.makeText(this,
                    "Accès accordé avec succès",
                    Toast.LENGTH_SHORT).show()
                finish()
            }

            if (responseCode == 400) {
                Toast.makeText(this,
                    "Les données fournies sont incorrectes",
                    Toast.LENGTH_SHORT).show()
            }

            if (responseCode == 403) {
                Toast.makeText(this,
                    "Accès interdit (token invalide ou non-propriétaire)",
                    Toast.LENGTH_SHORT).show()
            }

            if (responseCode == 409) {
                Toast.makeText(this,
                    "L’utilisateur est déjà associé à la maison",
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
