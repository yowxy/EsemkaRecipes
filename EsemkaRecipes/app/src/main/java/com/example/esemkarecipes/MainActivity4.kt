package com.example.esemkarecipes

import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Bundle
import android.renderscript.ScriptGroup.Binding
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.esemkarecipes.databinding.ActivityMain4Binding
import com.example.esemkarecipes.databinding.CardCategoriesBinding
import com.example.esemkarecipes.databinding.CardRecipeBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.json.JSONArray
import org.json.JSONObject
import java.net.URL

class MainActivity4 : AppCompatActivity() {
    companion object{
        lateinit var idRecipe :String
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val Binding = ActivityMain4Binding.inflate(layoutInflater)
        setContentView(Binding.root)

        GlobalScope.launch(Dispatchers.IO) {
            val Categori =
                URL("${MainActivity.Url}/api/recipes?categoryId=${CategoriesFragment.idCategori}").openStream()
                    .bufferedReader().readText()
            val ArrayC = JSONArray(Categori)
            if (ArrayC.length() > 0) {
                runOnUiThread {
                    Binding.l2.visibility = View.INVISIBLE
                }
                GlobalScope.launch(Dispatchers.Main) {
                    val adapter = object : RecyclerView.Adapter<CategoriView>() {
                        override fun onCreateViewHolder(
                            parent: ViewGroup,
                            viewType: Int
                        ): CategoriView {
                            val binding = CardRecipeBinding.inflate(
                                LayoutInflater.from(parent.context),
                                parent,
                                false
                            )
                            return CategoriView(binding)
                        }

                        override fun onBindViewHolder(holder: CategoriView, position: Int) {
                            val data = ArrayC.optJSONObject(position)
                            holder.binding.textView10.text = data.getString("title")
                            holder.binding.textView11.text = data.getString("description")

                            val categori = JSONObject(data.getString("category"))
                            Binding.toolbar.title = categori.getString("name");
                            GlobalScope.launch(Dispatchers.IO) {
                                val IMG = BitmapFactory.decodeStream(
                                    URL(
                                        "${MainActivity.Url}/images/recipes/" + data.getString("image")
                                    ).openStream()
                                )

                                runOnUiThread {
                                    holder.binding.imageView6.setImageBitmap(IMG)
                                }
                            }
                            holder.binding.RecipeC.setOnClickListener {
                                idRecipe = data.getString("id")
                                startActivity(Intent(this@MainActivity4, MainActivity5::class.java))
                            }
                        }

                        override fun getItemCount(): Int = ArrayC.length()

                    }

                    Binding.RcV.adapter = adapter
                    Binding.RcV.layoutManager = LinearLayoutManager(this@MainActivity4)
                }
            }
        }

        Binding.toolbar.setOnClickListener {
            finish()
        }
    }
}

class CategoriView (val binding: CardRecipeBinding): RecyclerView.ViewHolder(binding.root)
