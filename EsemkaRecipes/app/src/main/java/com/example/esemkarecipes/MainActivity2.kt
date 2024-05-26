package com.example.esemkarecipes

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.esemkarecipes.databinding.ActivityMain2Binding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.json.JSONObject
import java.net.HttpURLConnection
import java.net.URL

class MainActivity2 : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val Binding = ActivityMain2Binding.inflate(layoutInflater)
        setContentView(Binding.root)

        Binding.textView4.setOnClickListener {
            finish()
        }

        Binding.button2.setOnClickListener {
            GlobalScope.launch(Dispatchers.IO){
                val Regis = URL("${MainActivity.Url}/api/sign-up").openConnection() as HttpURLConnection
                Regis.requestMethod = "POST"
                Regis.setRequestProperty("Content-Type","application/json")

                if(Binding.editTextTextPassword3.text != Binding.editTextTextPassword2.text){
                    runOnUiThread {
                        Toast.makeText(this@MainActivity2, "Password & Confirmation Password filed", Toast.LENGTH_SHORT).show()
                    }
                    return@launch
                }
                val Json = JSONObject().apply {
                    put("username",Binding.editTextText2.text)
                    put("fullName",Binding.editTextText3.text)
                    put("dateOfBirth",Binding.editTextDate.text)
                    put("password",Binding.editTextTextPassword2.text)
//                    "username": "string",
//                    "fullName": "string",
//                    "password": "string",
//                    "dateOfBirth": "2024-04-12T14:42:28.737Z"
                }

                Regis.outputStream.write(Json.toString().toByteArray())
                val Cek = Regis.responseCode
                runOnUiThread {
                    if (Cek == 200){
                        Toast.makeText(this@MainActivity2, "Register Account succes", Toast.LENGTH_SHORT).show()
                        finish()
                    }else {
                        Toast.makeText(this@MainActivity2, "Register Input filed", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
        Binding.imageView2.setImageResource(R.drawable.esemka_recipes)
    }
}