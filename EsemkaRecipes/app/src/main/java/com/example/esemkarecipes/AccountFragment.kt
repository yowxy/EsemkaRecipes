package com.example.esemkarecipes

import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isInvisible
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.transition.Visibility
import com.example.esemkarecipes.databinding.CardRecipeBinding
import com.example.esemkarecipes.databinding.FragmentAccountBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.json.JSONArray
import org.json.JSONObject
import java.net.URL

class AccountFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val Binding = FragmentAccountBinding.inflate(layoutInflater)
        GlobalScope.launch(Dispatchers.IO) {
            val Me = URL("${MainActivity.Url}/api/me").openStream().bufferedReader().readText()
            val ObjectMe = JSONObject(Me)
            val IMG = BitmapFactory.decodeStream(URL("${MainActivity.Url}/images/profiles/"+ObjectMe.getString("image")).openStream())
            GlobalScope.launch(Dispatchers.Main) {

                Binding.Name.text = ObjectMe.getString("fullName")
                Binding.imageView5.setImageBitmap(IMG)
//    "id": 11,
//    "username": "string",
//    "fullName": "string",
//    "password": "string",
//    "dateOfBirth": "2024-04-13T02:08:06.045Z",
//    "image": "marcelo_uden.jpg"
            }

            val Follow = URL("${MainActivity.Url}/api/me/liked-recipes").openStream().bufferedReader().readText()
            val ArrayF = JSONArray(Follow)

            if(ArrayF.length() > 0) {
                Binding.L2.visibility = View.INVISIBLE
                GlobalScope.launch(Dispatchers.Main) {
                    val adapter = object : RecyclerView.Adapter<CardFollow>() {
                        override fun onCreateViewHolder(
                            parent: ViewGroup,
                            viewType: Int
                        ): CardFollow {
                            val binding = CardRecipeBinding.inflate(
                                LayoutInflater.from(parent.context),
                                parent,
                                false
                            )
                            return CardFollow(binding)
                        }

                        override fun onBindViewHolder(holder: CardFollow, position: Int) {
                            val data = ArrayF.getJSONObject(position)
                            holder.binding.L2.visibility = View.INVISIBLE
                            GlobalScope.launch(Dispatchers.IO) {
                                val IMG = BitmapFactory.decodeStream(
                                    URL(
                                        "${MainActivity.Url}/images/recipes/" + data.getString("image")
                                    ).openStream()
                                )
                                GlobalScope.launch(Dispatchers.Main) {

                                    holder.binding.imageView6.setImageBitmap(IMG)
                                }
                            }
                        }

                        override fun getItemCount(): Int = ArrayF.length()

                    }
                    Binding.RcV.layoutManager =
                        GridLayoutManager(requireContext(), 2, RecyclerView.VERTICAL, false)
                    Binding.RcV.adapter = adapter
                }
            }
        }
        return Binding.root
    }
}

class CardFollow (val binding: CardRecipeBinding) : RecyclerView.ViewHolder(binding.root)
