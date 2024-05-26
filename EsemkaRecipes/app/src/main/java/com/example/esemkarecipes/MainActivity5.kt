package com.example.esemkarecipes

import android.graphics.BitmapFactory
import android.os.Bundle
import android.renderscript.ScriptGroup.Binding
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.esemkarecipes.databinding.ActivityMain5Binding
import com.example.esemkarecipes.databinding.TextViewBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.json.JSONArray
import org.json.JSONObject
import java.net.HttpURLConnection
import java.net.URL

class MainActivity5 : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val Binding = ActivityMain5Binding.inflate(layoutInflater)
        setContentView(Binding.root)

        GlobalScope.launch(Dispatchers.IO) {
            val jsonArray = JSONArray(
                URL("${MainActivity.Url}/api/me/liked-recipes").openStream().bufferedReader()
                    .readText()
            )
            val ListL = ArrayList<String>()
            for (i in 0 until jsonArray.length()) {
                val jsonObject = jsonArray.getJSONObject(i)
                ListL.add(jsonObject.getString("id"))
            }
            runOnUiThread {
                if (ListL.contains(MainActivity4.idRecipe)) {
                    Binding.Like.setImageResource(R.drawable.liked)
                } else {
                    Binding.Like.setImageResource(R.drawable.like)
                }
            }
        }


        GlobalScope.launch(Dispatchers.IO) {
            val Detail =
                URL("${MainActivity.Url}/api/recipes/detail/${MainActivity4.idRecipe}").openStream()
                    .bufferedReader().readText()
            val ObjectD = JSONObject(Detail)

            Binding.toolbar2.title = ObjectD.getString("title")
            Binding.titleName.text = ObjectD.getString("title")
            Binding.price.text = ObjectD.getString("priceEstimate")
            Binding.time.text = ObjectD.getString("cookingTimeEstimate")
            val Categori = JSONObject(ObjectD.getString("category"))
            Binding.type.text = Categori.getString("name")
            Binding.des.text = ObjectD.getString("description")
            val ingredients = JSONArray(ObjectD.getString("ingredients"))
            val steps = JSONArray(ObjectD.getString("steps"))

            val img7 =
                BitmapFactory.decodeStream(
                    URL(
                        "${MainActivity.Url}/images/recipes/" + ObjectD.getString(
                            "image"
                        )
                    ).openStream()
                )
            val img10 = BitmapFactory.decodeStream(
                URL(
                    "${MainActivity.Url}/images/categories/" + Categori.getString("icon")
                ).openStream()
            )
            runOnUiThread {
                Binding.imageView7.setImageBitmap(img7)
                Binding.imageView10.setImageBitmap(img10)

//        "ingredients":
//        "steps":
                val adapter = object : RecyclerView.Adapter<IngView>() {
                    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): IngView {
                        val binding =
                            TextViewBinding.inflate(
                                LayoutInflater.from(parent.context),
                                parent,
                                false
                            )
                        return IngView(binding)
                    }

                    override fun onBindViewHolder(holder: IngView, position: Int) {
                        val data = ingredients[position]
                        holder.binding.TextValue.text = data.toString()
                    }

                    override fun getItemCount(): Int = ingredients.length()

                }

                val adapterS = object : RecyclerView.Adapter<StepTextView>() {
                    override fun onCreateViewHolder(
                        parent: ViewGroup,
                        viewType: Int
                    ): StepTextView {
                        val binding =
                            TextViewBinding.inflate(
                                LayoutInflater.from(parent.context),
                                parent,
                                false
                            )
                        return StepTextView(binding)
                    }

                    override fun onBindViewHolder(holder: StepTextView, position: Int) {
                        val data = steps[position]
                        holder.binding.TextValue.text = data.toString()
                    }

                    override fun getItemCount(): Int = steps.length()

                }

                Binding.Ing.adapter = adapter
                Binding.Ing.layoutManager = LinearLayoutManager(this@MainActivity5)

                Binding.Steps.adapter = adapterS
                    Binding.Steps.layoutManager = LinearLayoutManager(this@MainActivity5)
            }
        }
//        "categoryId": 0,
//        "title": "string",
//        "description": "string",
//        "priceEstimate": 0,
//        "cookingTimeEstimate": 0,
//        "image": "string",
//        "category": {
//            "id": 0,
//            "name": "string",
//            "icon": "string"
//        }
        Binding.Like.setOnClickListener {
            GlobalScope.launch(Dispatchers.IO) {
                val Like =
                    URL("${MainActivity.Url}/api/recipes/like-recipe?recipeId=${MainActivity4.idRecipe}").openStream()
                        .bufferedReader().readText()
                finish()
            }
        }

        Binding.toolbar2.setOnClickListener { finish() }
    }
}

class StepTextView (val binding: TextViewBinding) : RecyclerView.ViewHolder(binding.root)

class IngView (val binding: TextViewBinding):RecyclerView.ViewHolder(binding.root)
