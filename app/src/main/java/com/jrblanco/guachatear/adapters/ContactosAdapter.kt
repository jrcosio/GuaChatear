package com.jrblanco.guachatear.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.jrblanco.guachatear.R
import com.jrblanco.guachatear.databinding.ItemContactosBinding
import com.jrblanco.guachatear.model.UsuarioModel

class ContactosAdapter(val listacontactos:List<UsuarioModel>): RecyclerView.Adapter<ContactosAdapter.ItemViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return ItemViewHolder(layoutInflater.inflate(R.layout.item_contactos,parent,false))
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        holder.render(listacontactos[position])
    }

    override fun getItemCount(): Int {
        return listacontactos.size
    }

    class ItemViewHolder(val view: View):RecyclerView.ViewHolder(view) {

        private val binding = ItemContactosBinding.bind(view)

        fun render(usuarioModel: UsuarioModel){
            binding.apply {
                txtNombreContactos.text = usuarioModel.nombre
                txtEmailContactos.text = usuarioModel.usuario
               // Glide.with(ivAvatarContactos.context).load(usuarioModel.photo).into(ivAvatarContactos)
            }

            //============= Listener de los eventos ==============

        }

    }
}