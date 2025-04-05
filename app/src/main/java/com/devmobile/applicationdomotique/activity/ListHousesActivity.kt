package com.devmobile.applicationdomotique.activity


import android.os.Bundle
import android.widget.ListView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.devmobile.applicationdomotique.Api
import com.devmobile.applicationdomotique.R
import com.devmobile.applicationdomotique.adapter.HouseAdapter
import com.devmobile.applicationdomotique.data.HouseData
import com.devmobile.applicationdomotique.data.UrlData

class ListHousesActivity : AppCompatActivity() {
    private val houses = ArrayList<HouseData>()
    private lateinit var houseAdapter: HouseAdapter


    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list_house)

        val token = intent.getStringExtra("USER_TOKEN")


        if (token.isNullOrEmpty()) {
            Toast.makeText(this,
                "Token manquant ou invalide",
                Toast.LENGTH_SHORT).show()

        }

        initHouseListView()
        listHouse(token)



    }


    private fun initHouseListView() {
        val listView = findViewById<ListView>(R.id.lstHouse)

        houseAdapter = HouseAdapter(this, houses)
        listView.adapter = houseAdapter
    }


    private fun listHouse(token: String?) {
        val urls = UrlData()
        val url = urls.listHouse()
        Api().get<List<HouseData>>(
            url,
            ::onHouseSuccess,
            token
        )
    }


    private fun onHouseSuccess(responseCode: Int, data: List<HouseData>?) {
        runOnUiThread {
            if (responseCode == 200) {

                if (data != null) {
                    houses.addAll(data)
                    houseAdapter.notifyDataSetChanged()

                    Toast.makeText(this,
                        "Maisons chargées avec succès", Toast.LENGTH_SHORT).show()
                }
            }
            if (responseCode == 400) {
                Toast.makeText(this,
                    "Les données fournies sont incorrectes",
                    Toast.LENGTH_SHORT).show()
            }

            if (responseCode == 403) {
                Toast.makeText(this,
                    "Accès interdit token invalide ou ne correspondant pas au propriétaire de la maison)",
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

