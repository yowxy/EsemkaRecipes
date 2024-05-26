package com.example.esemkarecipes

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.esemkarecipes.databinding.ActivityMain3Binding
import com.google.android.material.tabs.TabLayoutMediator
import com.example.esemkarecipes.CategoriesFragment
import com.example.esemkarecipes.AccountFragment

class MainActivity3 : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val Binding = ActivityMain3Binding.inflate(layoutInflater)
        setContentView(Binding.root)

        val Fragment = listOf(CategoriesFragment(),AccountFragment())
        val MenuF = listOf("Categories","Profile")
        val icon = listOf(R.drawable.ic_category,R.drawable.ic_my_profile)
        Binding.Fm.adapter = object : FragmentStateAdapter(supportFragmentManager,lifecycle){
            override fun getItemCount(): Int = Fragment.size
            override fun createFragment(position: Int): Fragment = Fragment[position]
        }

        TabLayoutMediator(Binding.Tl,Binding.Fm){tab,pos->
            tab.text = MenuF[pos]
            tab.icon = ContextCompat.getDrawable(this@MainActivity3,icon[pos])
        }.attach()
    }
}