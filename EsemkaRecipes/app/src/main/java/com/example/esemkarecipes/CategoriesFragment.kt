package com.example.esemkarecipes

import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Bundle
import android.renderscript.ScriptGroup.Binding
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.esemkarecipes.databinding.CardCategoriesBinding
import com.example.esemkarecipes.databinding.FragmentCategoriesBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.json.JSONArray
import org.json.JSONObject
import java.net.URL

class CategoriesFragment : Fragment() {

    companion object {
        lateinit var idCategori : String
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val Binding = FragmentCategoriesBinding.inflate(layoutInflater)
        val view = Binding.root
        GlobalScope.launch(Dispatchers.IO) {
            val Contain =
                URL("${MainActivity.Url}/api/categories").openStream().bufferedReader().readText()
            val ArrayC = JSONArray(Contain)

            GlobalScope.launch(Dispatchers.Main) {
                val adapter = object : RecyclerView.Adapter<CardView>() {
                    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardView {
                        val binding = CardCategoriesBinding.inflate(
                            LayoutInflater.from(parent.context),
                            parent,
                            false
                        )

                        return CardView(binding)
                    }

                    override fun onBindViewHolder(holder: CardView, position: Int) {
                        val data = ArrayC.getJSONObject(position)
                        holder.binding.textView7.text = data.getString("name")
                        GlobalScope.launch(Dispatchers.IO) {
                            val IMG = "${MainActivity.Url}/images/categories/"+data.getString("icon")
//                            Log.d("TAG", "onBindViewHolder: $IMG")
                            val ImgBitmap = BitmapFactory.decodeStream(URL(IMG).openStream())
                            GlobalScope.launch(Dispatchers.Main) {
                                holder.binding.imageView3.setImageBitmap(ImgBitmap)
                            }
                        }
                        holder.binding.CardId.setOnClickListener {
                            idCategori = data.getString("id")
                            startActivity(Intent(requireContext(),MainActivity4::class.java))
                        }
                    }

                    override fun getItemCount(): Int = ArrayC.length()

                }

                Binding.RcV.adapter = adapter
                Binding.RcV.layoutManager =
                    GridLayoutManager(requireContext(), 2, RecyclerView.VERTICAL, false)
//                Binding.RcV.layoutManager = LinearLayoutManager(requireContext())
            }
        }
        return view
    }
class CardView (val binding : CardCategoriesBinding) : RecyclerView.ViewHolder(binding.root)
}

