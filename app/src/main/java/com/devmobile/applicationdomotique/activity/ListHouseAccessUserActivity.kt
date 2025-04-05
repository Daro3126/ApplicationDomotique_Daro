package com.devmobile.applicationdomotique.activity


import android.os.Bundle
import android.widget.ListView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.devmobile.applicationdomotique.Api
import com.devmobile.applicationdomotique.R
import com.devmobile.applicationdomotique.adapter.UserAdapter
import com.devmobile.applicationdomotique.data.UrlData
import com.devmobile.applicationdomotique.data.UserAccessData

class ListHouseAccessUserActivity : AppCompatActivity() {
    private val houses = ArrayList<UserAccessData>()
    private lateinit var userAdapter: UserAdapter
    private var houseId: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list_house_access_user)

        val token = intent.getStringExtra("USER_TOKEN")
        val id = intent.getStringExtra("HOUSE_ID")

        if (token.isNullOrEmpty()) {
            Toast.makeText(this,
                "Token manquant ou invalide",
                Toast.LENGTH_SHORT).show()

        }
        if (id.isNullOrEmpty()) {
            Toast.makeText(this,
                "ID de maison manquant",
                Toast.LENGTH_SHORT).show()
            return
        }

        houseId = id.toInt()
        initHouseListView()
        listHouse(token, houseId)
    }

    private fun initHouseListView() {
        val listView = findViewById<ListView>(R.id.lstUser)
        userAdapter = UserAdapter(this, houses)
        listView.adapter = userAdapter
    }

    private fun listHouse(token: String?, houseId: Int) {
        val urls = UrlData()
        val url = urls.listHouseAccessUser(houseId)
        Api().get<List<UserAccessData>>(
            url,
            ::onHouseSuccess,
            token
        )
    }

    private fun onHouseSuccess(responseCode: Int, data: List<UserAccessData>?) {
        runOnUiThread {
            if (responseCode == 200) {

                if (data != null) {
                    houses.addAll(data) // Ajouter les nouvelles données
                    userAdapter.notifyDataSetChanged()

                    Toast.makeText(this,
                        " La liste des utilisateurs a bien été envoyée",
                        Toast.LENGTH_SHORT).show()
                }
                else{
                    Toast.makeText(this,
                        " aucun utilisateur n'a access à la maison",
                        Toast.LENGTH_SHORT).show()
                }
            }
            if (responseCode == 400) {
                Toast.makeText(this,
                    "Les données fournies sont incorrectes",
                    Toast.LENGTH_SHORT).show()
            }
            if (responseCode == 403) {
                Toast.makeText(this,
                    "Accès interdit (token invalide ou ne correspondant pas au Propriétaire ou a un invité de la maison)",
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

