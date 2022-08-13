package com.jrblanco.guachatear.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.jrblanco.guachatear.R
import com.jrblanco.guachatear.adapters.ChatsAdapter
import com.jrblanco.guachatear.databinding.FragmentChatsBinding
import com.jrblanco.guachatear.model.ChatsModel

class ChatsFragment : Fragment() {

    private lateinit var binding: FragmentChatsBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentChatsBinding.inflate(layoutInflater)


        initRecyclerView()
        return binding.root
    }

    private fun initRecyclerView() {
        binding.apply {
            rvChats.layoutManager = LinearLayoutManager(context)
            rvChats.adapter = ChatsAdapter(listaChats)
        }
    }

    companion object {
        val listaChats = listOf<ChatsModel>(
            ChatsModel("","","SANDRA","chao","00:43",0),
            ChatsModel("","","MOMIAS","Jairo: jajajaj","10:00",1),
            ChatsModel("","","KOTLIN/JAVA","jrblanco: http://www.google.com","mar",1),
            ChatsModel("","","JOSERA","capullo","02:33",0),
            ChatsModel("","","PABLO","pringao","ayer",0),
            ChatsModel("","","FAMILIA","SANDRA: buuuuu","jue",1)
        )
    }
}