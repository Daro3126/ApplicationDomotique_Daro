package com.devmobile.applicationdomotique.activity

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.devmobile.applicationdomotique.Api
import com.devmobile.applicationdomotique.R
import com.devmobile.applicationdomotique.data.DeleteData
import com.devmobile.applicationdomotique.data.UrlData

class DeleteAcessHouseActivity : AppCompatActivity() {
    private var houseId: Int = 0
    private var token: String? = ""
    private var login : String = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)



        token = intent.getStringExtra("USER_TOKEN")
        login = intent.getStringExtra("USER_LOGIN").toString()
        houseId = intent.getStringExtra("HOUSE_ID").toString().toInt()

        deleteAccessHouse(houseId, login,token)

    }


    private fun deleteAccessHouse(houseId: Int, userLogin: String, token: String?) {
        val urls = UrlData()
        val url = urls.deleteAccess(houseId)
        val data = DeleteData(userLogin)
        Api().delete(
            url,
            data,
            ::onDeleteAccessSuccess,
            token
        );
    }

    private fun onDeleteAccessSuccess(responseCode: Int) {
        runOnUiThread {
            if (responseCode == 200) {
                val intent = Intent(this, ListHouseAccessUserActivity::class.java)
                intent.putExtra("HOUSE_ID", houseId.toString())
                intent.putExtra("USER_TOKEN", token)
                startActivity(intent)
                Toast.makeText(this,
                    "Suppression réalisée",
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



            if (responseCode == 500) {
                Toast.makeText(this,
                    "Erreur du serveur",
                    Toast.LENGTH_SHORT).show()
            }
        }
    }


}


