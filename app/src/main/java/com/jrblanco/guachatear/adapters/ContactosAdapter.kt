package com.jrblanco.guachatear.adapters

import android.app.Application
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.jrblanco.guachatear.R
import com.jrblanco.guachatear.databinding.ItemContactosBinding
import com.jrblanco.guachatear.model.ChatsModel
import com.jrblanco.guachatear.model.ChatsUsuarioModel
import com.jrblanco.guachatear.model.UsuarioModel
import com.jrblanco.guachatear.view.ChatearActivity
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

        private val db = Firebase.firestore     //Para gestionar la base de datos
        val mAuth = FirebaseAuth.getInstance()

        fun render(usuarioModel: UsuarioModel){
            binding.apply {
                txtNombreContactos.text = usuarioModel.nombre.lowercase()
                    .replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.ROOT) else it.toString() }
                txtEmailContactos.text = usuarioModel.usuario.substringBefore("@")
                Glide.with(ivAvatarContactos.context).load(usuarioModel.photo).into(ivAvatarContactos)
            }

            //============= Listener de los eventos ==============
            var idChat:String = ""

            binding.itemContacto.setOnClickListener{
                //Miramos si existe conversación en los chat del usuario
                db.collection(UsuarioModel.USUARIOS).document(mAuth.currentUser?.uid.toString()).collection("mischat")
                    .get()
                    .addOnSuccessListener { result ->
                        for (document in result) {
                            if (document.data["tipo"]?.equals("Simple") == true && document.data["idGoogle"]?.equals(usuarioModel.idGoogle) == true) {
                                idChat = document.id
                            }
                        }
                        if (idChat.isNotEmpty()) {
                            lanzarchat(idChat)
                        } else {
                            //--- Busca si el otro usuario si tiene un chat iniciado
                            db.collection(UsuarioModel.USUARIOS).document(usuarioModel.idGoogle).collection("mischat")
                                .get()
                                .addOnSuccessListener { result ->
                                    for (document in result) {
                                        if (document.data["tipo"]?.equals("Simple") == true && document.data["idGoogle"]?.equals(mAuth.currentUser?.uid.toString()) == true) {
                                            idChat = document.id
                                        }
                                    }
                                    if (idChat.isNotEmpty()) {
                                        lanzarchat(idChat)
                                    } else { //Que no existe en ninguno de los dos usuario, pues procedemos a crear uno.
                                        val nuevoChatSimple = hashMapOf<String, Any>(
                                            "tipoChat" to "Simple",
                                            "ultimomensaje" to "",
                                            "diahora" to Timestamp.now()
                                        )
                                        db.collection(ChatsModel.CHATS)
                                            .add(nuevoChatSimple)
                                            .addOnSuccessListener { nuevoChat -> //Una vez creado actualizamos en los usuarios la info

                                                idChat = nuevoChat.id //Guardamos el id del CHAT

                                                val infoChatParaUsuario = hashMapOf<String, Any>(
                                                    "tipo" to "Simple",
                                                    "nombre" to usuarioModel.nombre,
                                                    "idGoogle" to usuarioModel.idGoogle,
                                                    "icono" to usuarioModel.photo)

                                                db.collection(UsuarioModel.USUARIOS).document(mAuth.currentUser?.uid.toString()).collection("mischat").document(idChat)
                                                    .set(infoChatParaUsuario)
                                                    .addOnSuccessListener { Log.d(ContentValues.TAG,"Info Chat añadida con exito") }
                                                    .addOnFailureListener { e -> Log.w(ContentValues.TAG, "Error writing document", e) }

                                                val infoChatParaUsuario2 = hashMapOf<String, Any>(
                                                    "tipo" to "Simple",
                                                    "nombre" to mAuth.currentUser?.displayName.toString(),
                                                    "idGoogle" to mAuth.currentUser?.uid.toString(),
                                                    "icono" to mAuth.currentUser?.photoUrl.toString())

                                                db.collection(UsuarioModel.USUARIOS).document(usuarioModel.idGoogle).collection("mischat").document(idChat)
                                                    .set(infoChatParaUsuario2)
                                                    .addOnSuccessListener { Log.d(ContentValues.TAG,"Info Chat añadida con exito") }
                                                    .addOnFailureListener { e -> Log.w(ContentValues.TAG, "Error writing document", e) }

                                                if (idChat.isNotEmpty()) {
                                                    lanzarchat(idChat)
                                                }
                                            }
                                            .addOnFailureListener { Log.w("INFOJR", "Error creando nuevo chat", it)}
                                    }
                                }
                        }
                    }
                    .addOnFailureListener { Log.d(ContentValues.TAG, "Error getting documents: ", it) }
            }
        }

        private fun lanzarchat(idChat: String) {
            val context = itemView.context
            val intent =  Intent(context.applicationContext, ChatearActivity::class.java).apply {
                putExtra("IDCHAT", idChat)
            }
            context.startActivity(intent)

        }


    }
}