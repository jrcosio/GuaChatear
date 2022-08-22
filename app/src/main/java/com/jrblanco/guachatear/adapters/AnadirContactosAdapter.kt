package com.jrblanco.guachatear.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.jrblanco.guachatear.R
import com.jrblanco.guachatear.databinding.ItemAnadirContactoBinding
import com.jrblanco.guachatear.model.UsuarioModel

class AnadirContactosAdapter(val listaUsuarios:List<UsuarioModel>, private val onClickItemAnadirContacto:(UsuarioModel) -> Unit): RecyclerView.Adapter<AnadirContactosAdapter.ItemViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return ItemViewHolder(layoutInflater.inflate(R.layout.item_anadir_contacto,parent,false))
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
      holder.render(listaUsuarios[position],onClickItemAnadirContacto)
    }

    override fun getItemCount(): Int {
        return listaUsuarios.size
    }

    class ItemViewHolder(view: View):RecyclerView.ViewHolder(view){
        private val binding = ItemAnadirContactoBinding.bind(view)

        fun render(usuario: UsuarioModel, onClickItemAnadirContacto: (UsuarioModel) -> Unit) {
            binding.apply {
                Glide.with(ivItemAvatarContacto.context).load(usuario.photo).into(ivItemAvatarContacto)
                txtItemNombreContacto.text = usuario.nombre
                txtItemEmailContacto.text = usuario.usuario

                cvItemAnadirContacto.setOnClickListener{
                    onClickItemAnadirContacto(usuario)
                }
            }
        }
    }
}