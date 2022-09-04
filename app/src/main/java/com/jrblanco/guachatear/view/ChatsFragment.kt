package com.jrblanco.guachatear.view

import android.content.ContentValues
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.gms.common.util.CollectionUtils
import com.google.common.collect.Iterables.retainAll
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.jrblanco.guachatear.R
import com.jrblanco.guachatear.adapters.ChatsAdapter
import com.jrblanco.guachatear.databinding.FragmentChatsBinding
import com.jrblanco.guachatear.model.ChatsModel
import com.jrblanco.guachatear.model.ChatsUsuarioModel
import com.jrblanco.guachatear.model.UsuarioModel

class ChatsFragment : Fragment() {

    private lateinit var binding: FragmentChatsBinding
    private val db = Firebase.firestore     //Para gestionar la base de datos
    private var listaChats = ArrayList<ChatsModel>()

    override fun onStart() {
        super.onStart()
        cargarMisChats()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentChatsBinding.inflate(layoutInflater)


       // cargarMisChats()
        initRecyclerView()
        return binding.root
    }

    private fun initRecyclerView() {
        binding.apply {
            rvChats.layoutManager = LinearLayoutManager(context)
            rvChats.adapter = ChatsAdapter(listaChats)
        }
    }

    private fun cargarMisChats(){
        val mAuth = FirebaseAuth.getInstance()
        val listaMisChat = ArrayList<ChatsUsuarioModel>()

        db.collection(UsuarioModel.USUARIOS).document(mAuth.currentUser?.uid.toString()).collection("mischat")
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    if (document.data["tipo"]?.equals("Grupo") == true) {
                        listaMisChat.add(ChatsUsuarioModel(
                            idchat = document.id,
                            tipo = document.data["tipo"] as String)
                        )
                    }else {
                        listaMisChat.add(ChatsUsuarioModel(
                            idchat = document.id,
                            tipo = document.data["tipo"] as String,
                            idUser = document.data["idGoogle"] as String,
                            nombre = document.data["nombre"] as String,
                            icono = document.data["icono"] as String)
                        )
                    }
                }
                // Una vez que hemos leido los chat a los que esta suscrito el usuario
                // les obtenemos del listado de chat principal
                obtenerChats(listaMisChat)
            }
            .addOnFailureListener { Log.d(ContentValues.TAG, "Error getting documents: ", it) }
    }

    /**
     * Método que obtine los chat que tiene el usuario
     */
    private fun obtenerChats(misChat: List<ChatsUsuarioModel>) {

        this.listaChats.clear() //Limpia la lista de chat para que este vacia.

        db.collection(ChatsModel.CHATS)
            .get()
            .addOnSuccessListener {
                for (document in it){
                    for (miChat in misChat) {
                        if (document.id == miChat.idchat) {
                            if (miChat.tipo.equals("Grupo")) {
                                this.listaChats.add(ChatsModel(
                                        idChat = document.id,
                                        icono = document.data["icono"] as String,
                                        nombre = document.data["nombre"] as String,
                                        ultimomensaje = document.data["ultimomensaje"] as String,
                                        diahora = document.data["diahora"] as Timestamp,
                                        tipoChat = document.data["tipoChat"] as String)
                                )
                            } else {
                                this.listaChats.add(ChatsModel(
                                    idChat = document.id,
                                    icono = miChat.icono,
                                    nombre = miChat.nombre,
                                    ultimomensaje = document.data["ultimomensaje"] as String,
                                    diahora = document.data["diahora"] as Timestamp,
                                    tipoChat = miChat.tipo)
                                )
                            }
                        }
                    }
                }
                binding.rvChats.adapter?.notifyDataSetChanged()
            }
            .addOnFailureListener{ Log.d(ContentValues.TAG, "Error obteniendo chats: ", it) }
    }
}