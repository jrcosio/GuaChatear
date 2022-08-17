package com.jrblanco.guachatear.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.jrblanco.guachatear.databinding.ActivityChatearBinding

class ChatearActivity : AppCompatActivity() {
    private lateinit var binding: ActivityChatearBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChatearBinding.inflate(layoutInflater)
        setContentView(binding.root)




        binding.btnVolverAtras.setOnClickListener{
            onBackPressed()
        }

    }
}