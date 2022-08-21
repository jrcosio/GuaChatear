package com.jrblanco.guachatear.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.jrblanco.guachatear.R
import com.jrblanco.guachatear.databinding.ItemChatsBinding
import com.jrblanco.guachatear.databinding.ItemConversacion1Binding
import com.jrblanco.guachatear.databinding.ItemConversacion2Binding
import com.jrblanco.guachatear.model.MensajeModel

class ChatearAdater(var conversacion:List<MensajeModel>): RecyclerView.Adapter<ChatearAdater.ItemViewHolder>(){

    private val MENSAJE1: Int = 1
    private val MENSAJE2: Int = 2

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        var v: View? = null
        when (viewType) {
            MENSAJE1 -> {
                val layoutInflater = LayoutInflater.from(parent.context)
                v = layoutInflater.inflate(R.layout.item_conversacion_1,parent,false)
            }
            MENSAJE2 -> {
                val layoutInflater = LayoutInflater.from(parent.context)
                v = layoutInflater.inflate(R.layout.item_conversacion_2,parent,false)
            }
        }
        return ItemViewHolder(view = v!!)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        holder.render(conversacion[position], holder.itemViewType)
    }

    override fun getItemCount(): Int {
        return conversacion.size
    }

    override fun getItemViewType(position: Int): Int {
        //return super.getItemViewType(position)
        if (conversacion[position].iduser.equals("YO")) {
            return MENSAJE2
        } else {
            return MENSAJE1
        }
    }

    class ItemViewHolder(var view: View):RecyclerView.ViewHolder(view) {

        //private val binding = ItemConversacion1Binding.bind(view)

        fun render(mensaje:MensajeModel, queMnsj: Int) {
            when (queMnsj) {
                1 -> {
                    val binding = ItemConversacion1Binding.bind(view)
                    binding.txtUserItem1.text = mensaje.nombre
                    binding.txtMensajeItem1.text = mensaje.mensaje
                    binding.txtHoraItem1.text = mensaje.fechayhora
                }
                2 -> {
                    val binding = ItemConversacion2Binding.bind(view)
                    binding.txtMensajeItem2.text = mensaje.mensaje
                    binding.txtHoraItem2.text = mensaje.fechayhora
                }
            }

        }
    }
}