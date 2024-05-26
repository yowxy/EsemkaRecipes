package com.example.esemkarecipes

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.esemkarecipes.databinding.ActivityMainBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.json.JSONObject
import java.io.DataOutputStream
import java.net.HttpURLConnection
import java.net.URL

class MainActivity : AppCompatActivity() {
    companion object{
        var Url = "http://10.0.2.2:5000"
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val Binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(Binding.root)

        Binding.editTextText.setText("string")
        Binding.editTextTextPassword.setText("string")
        Binding.imageView.setImageResource(R.drawable.auth)

        Binding.button.setOnClickListener {
            GlobalScope.launch(Dispatchers.IO) {
                val Login = URL("$Url/api/sign-in").openConnection() as HttpURLConnection
                Login.requestMethod = "POST"
                Login.setRequestProperty("Content-Type", "application/json")

                val Json = JSONObject().apply {
                    put("username", Binding.editTextText.text)
                    put("password", Binding.editTextTextPassword.text)
//                    "username": "string",
//                    "password": "string"
                }

                Login.outputStream.write(Json.toString().toByteArray())
                Login.outputStream.close()

                val Respon = Login.responseCode
                runOnUiThread {
                    if (Respon == 200) {
                        startActivity(Intent(this@MainActivity, MainActivity3::class.java))
                    } else {
                        val Error = JSONObject(Login.errorStream.bufferedReader().readText())
                        Toast.makeText(
                            this@MainActivity,
                            "${Error.getString("errors")}",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        }

        Binding.textView4.setOnClickListener {
            startActivity(Intent(this@MainActivity, MainActivity2::class.java))
        }
    }
}