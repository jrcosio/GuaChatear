package com.jrblanco.guachatear.view

import android.content.ContentValues
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.jrblanco.guachatear.R
import com.jrblanco.guachatear.adapters.ChatsAdapter
import com.jrblanco.guachatear.databinding.FragmentChatsBinding
import com.jrblanco.guachatear.model.ChatsModel
import com.jrblanco.guachatear.model.UsuarioModel

class ChatsFragment : Fragment() {

    private lateinit var binding: FragmentChatsBinding
    val db = Firebase.firestore     //Para gestionar la base de datos
    private var listaChats = ArrayList<ChatsModel>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentChatsBinding.inflate(layoutInflater)


        cargarChats()
        initRecyclerView()
        return binding.root
    }

    private fun initRecyclerView() {
        binding.apply {
            rvChats.layoutManager = LinearLayoutManager(context)
            rvChats.adapter = ChatsAdapter(listaChats)
        }
    }

    private fun cargarChats(){
        val mAuth = FirebaseAuth.getInstance()

        this.listaChats.clear()

        db.collection(ChatsModel.CHATS)
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    println(document.data)
                    this.listaChats.add(
                        ChatsModel(
                            document.data["icono"] as String,
                            document.data["nombre"] as String,
                            document.data["ultimomensaje"] as String,
                            "",
                            document.data["tipoChat"] as String
                        )
                    )
                }
                binding.rvChats.adapter?.notifyDataSetChanged()
            }
            .addOnFailureListener { exception ->
                Log.d(ContentValues.TAG, "Error getting documents: ", exception)
            }
    }
}