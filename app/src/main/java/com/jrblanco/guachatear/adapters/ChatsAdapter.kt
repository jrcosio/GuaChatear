package com.jrblanco.guachatear.adapters

import android.view.LayoutInflater
import android.view.VelocityTracker
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.jrblanco.guachatear.R
import com.jrblanco.guachatear.databinding.ItemChatsBinding
import com.jrblanco.guachatear.model.ChatsModel

class ChatsAdapter(val listaChats:List<ChatsModel>): RecyclerView.Adapter<ChatsAdapter.ItemViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return  ItemViewHolder(layoutInflater.inflate(R.layout.item_chats,parent,false))
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        holder.render(listaChats[position])
    }

    override fun getItemCount(): Int {
        return listaChats.size
    }

    class ItemViewHolder(val view: View):RecyclerView.ViewHolder(view) {
        private val binding = ItemChatsBinding.bind(view)

        fun render(chatsModel: ChatsModel) {
            binding.apply {
                txtNombreChat.text = chatsModel.nombre
                txtUltimoMnsj.text = chatsModel.ultimotexto
                txtHoraUltiMnsj.text = chatsModel.date
                if (chatsModel.tipoChat == 1) { ivTipoChat.visibility = View.VISIBLE }
            }
        }
    }
}