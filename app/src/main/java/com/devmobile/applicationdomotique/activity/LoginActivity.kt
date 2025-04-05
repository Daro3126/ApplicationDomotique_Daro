package com.devmobile.applicationdomotique.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.devmobile.applicationdomotique.Api
import com.devmobile.applicationdomotique.R
import com.devmobile.applicationdomotique.data.LoginData
import com.devmobile.applicationdomotique.data.TokenData
import com.devmobile.applicationdomotique.data.UrlData

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val button = findViewById<Button>(R.id.btnConect)
        button.setOnClickListener {
            login()
        }
    }


    public fun registerNewAccount(view: View) {
        val intent = Intent(this, RegisterActivity::class.java)
        startActivity(intent)
    }


    public fun login() {
        val login = findViewById<EditText>(R.id.txtLogin).text.toString()
        val password = findViewById<EditText>(R.id.txtPassword).text.toString()

        if (login.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Remplir tous les champs", Toast.LENGTH_SHORT).show()
            return
        }

        val loginData = LoginData(login, password)
        val urls = UrlData()
        val url = urls.login()
        Api().post<LoginData, TokenData>(
            url,
            loginData,
            ::loginSuccess
        )
    }


    fun loginSuccess(responseCode: Int, tokenData: TokenData?) {
        if (responseCode == 200 && tokenData != null) {
            runOnUiThread {
                val token = tokenData.token

                if (token.isNullOrEmpty()) {
                    Toast.makeText(this,
                        "Token manquant",
                        Toast.LENGTH_SHORT).show()

                }

                val login = findViewById<EditText>(R.id.txtLogin).text.toString()

                val intent = Intent(this, ListHousesActivity::class.java)
                intent.putExtra("USER_TOKEN", token)
                intent.putExtra("USER_LOGIN", login)
                startActivity(intent)
                Toast.makeText(this,
                    "Connexion réussie",
                    Toast.LENGTH_SHORT).show()
            }
        }

        if(responseCode == 400){
            runOnUiThread {
                Toast.makeText(this,
                    "les données fournies sont incorrectes",
                    Toast.LENGTH_SHORT).show()
            }

        }
        if (responseCode == 404){
            runOnUiThread {
                Toast.makeText(this,
                    "Aucun utilisateur ne correspond aux identifiants donnés",
                    Toast.LENGTH_SHORT).show()
            }


        }
        if (responseCode==500) {
            runOnUiThread {
                Toast.makeText(this,
                    "Une erreur s’est produite au niveau du serveur",
                    Toast.LENGTH_SHORT).show()
            }
        }


    }



}










