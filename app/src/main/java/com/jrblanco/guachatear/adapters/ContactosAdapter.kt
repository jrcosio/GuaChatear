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
import java.util.*
import kotlin.collections.ArrayList

class ContactosAdapter(var listacontactos:ArrayList<UsuarioModel>): RecyclerView.Adapter<ContactosAdapter.ItemViewHolder>() {

    private var listaOriginal = listacontactos

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

//    fun filtro(txtBuscar:String) {
//        if (txtBuscar.isEmpty()) {
//            this.listacontactos.clear()
//            this.listacontactos.addAll(listaOriginal)
//        } else {
//            val listatemporal = listacontactos.filter{it.nombre.lowercase().contains(txtBuscar.lowercase())}
//            listacontactos.clear()
//            listacontactos.addAll(listatemporal)
//        }
//        notifyDataSetChanged()
//    }

    class ItemViewHolder(val view: View):RecyclerView.ViewHolder(view) {

        private val binding = ItemContactosBinding.bind(view)

        fun render(usuarioModel: UsuarioModel){
            binding.apply {
                txtNombreContactos.text = usuarioModel.nombre.lowercase()
                    .replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.ROOT) else it.toString() }
                txtEmailContactos.text = usuarioModel.usuario.substringBefore("@")
                Glide.with(ivAvatarContactos.context).load(usuarioModel.photo).into(ivAvatarContactos)
            }

            //============= Listener de los eventos ==============

        }

    }
}