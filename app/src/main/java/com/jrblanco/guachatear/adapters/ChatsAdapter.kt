package com.jrblanco.guachatear.adapters

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.jrblanco.guachatear.R
import com.jrblanco.guachatear.databinding.ItemChatsBinding
import com.jrblanco.guachatear.model.ChatsModel
import com.jrblanco.guachatear.view.ChatearActivity
import java.util.*

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
                txtNombreChat.text = chatsModel.nombre.lowercase()
                    .replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.ROOT) else it.toString() }
                txtUltimoMnsj.text = chatsModel.ultimomensaje
                txtHoraUltiMnsj.text = chatsModel.diahora
                if (chatsModel.tipoChat.equals("Grupo")) { ivTipoChat.visibility = View.VISIBLE }
            }

            //================ EVENTOS ================

            binding.itemChat.setOnClickListener {
                val context = itemView.context
                val intent =  Intent(context.applicationContext,ChatearActivity::class.java)
                context.startActivity(intent)
            }
        }
    }
}