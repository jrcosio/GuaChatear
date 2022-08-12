package com.jrblanco.guachatear.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import com.jrblanco.guachatear.R
import com.jrblanco.guachatear.adapters.ContactosAdapter
import com.jrblanco.guachatear.databinding.FragmentContactosBinding
import com.jrblanco.guachatear.model.UsuarioModel

class ContactosFragment : Fragment() {

    private lateinit var binding: FragmentContactosBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentContactosBinding.inflate(layoutInflater)



        initrecyclerview()
        return binding.root
    }

    private fun initrecyclerview() {
        binding.apply {
            rvContactos.layoutManager = GridLayoutManager(context,2)
            rvContactos.adapter = ContactosAdapter(listaContactos)
        }
    }

    companion object {
        val listaContactos = listOf<UsuarioModel>(
            UsuarioModel("","PEDRO","pedro",""),
            UsuarioModel("","SANDRA","ssbielva",""),
            UsuarioModel("","CARMEN","mama",""),
            UsuarioModel("","EMMA","emmaguti",""),
            UsuarioModel("","RAMON","rblanco","")
        )
    }

}