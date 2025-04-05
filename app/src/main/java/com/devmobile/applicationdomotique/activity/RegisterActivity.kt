package com.devmobile.applicationdomotique.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.devmobile.applicationdomotique.Api
import com.devmobile.applicationdomotique.R
import com.devmobile.applicationdomotique.data.RegisterData
import com.devmobile.applicationdomotique.data.UrlData

class RegisterActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        val btnRegister = findViewById<Button>(R.id.btnRegister)
        btnRegister.setOnClickListener {
            register()
        }
    }

    public fun register() {
        val login = findViewById<EditText>(R.id.txtRegisterLogin).text.toString()
        val password = findViewById<EditText>(R.id.txtRegisterPassword).text.toString()

        if ( login.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "remplir tous les champs", Toast.LENGTH_SHORT).show()
            return
        }

        val userData = RegisterData( login, password)
        val urls = UrlData()
        val url = urls.register()
        Api().post<RegisterData>(url, userData, ::registerSuccess)
    }
    public fun registerSuccess(responseCode: Int) {
        if (responseCode == 200) {
            runOnUiThread {
                Toast.makeText(this,
                    "Le compte a bien été créé",
                    Toast.LENGTH_SHORT).show()
            }
            finish()
        }
        if(responseCode == 400){
            runOnUiThread {
                Toast.makeText(this,
                    "les données fournies sont incorrectes",
                    Toast.LENGTH_SHORT).show()
            }
            finish()
        }
        if (responseCode == 409){
            runOnUiThread {
                Toast.makeText(this,
                    "Le login est déjà utilisé par un autre compte",
                    Toast.LENGTH_SHORT).show()
            }
            finish()

        }
         if (responseCode==500) {
            runOnUiThread {
                Toast.makeText(this,
                    "Erreur",
                    Toast.LENGTH_SHORT).show()
            }
        }
    }
}