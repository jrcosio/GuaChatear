package com.jrblanco.guachatear.view

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.jrblanco.guachatear.R
import com.jrblanco.guachatear.adapters.AnadirContactosAdapter
import com.jrblanco.guachatear.adapters.ChatearAdater
import com.jrblanco.guachatear.adapters.ContactosAdapter
import com.jrblanco.guachatear.databinding.FragmentAnadirContactoBinding
import com.jrblanco.guachatear.model.UsuarioModel


class AnadirContactoFragment : DialogFragment() {

    private lateinit var binding: FragmentAnadirContactoBinding

    private val listaUsuarios = ArrayList<UsuarioModel>()

    private val layoutManager = LinearLayoutManager(context)  //Indicamos que tipo de LayoutManager creamos
    private lateinit var adapter: AnadirContactosAdapter                  //Define el adapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAnadirContactoBinding.inflate(layoutInflater)


        binding.apply {
            btnCancelarAddContacto.setOnClickListener {
                dismiss()
            }
        }

        leerUsuariosBaseDatos()
        initRecyclerView()

        // Inflate the layout for this fragment
        return binding.root
        //inflater.inflate(R.layout.fragment_anadir_contacto, container, false)
    }

    private fun initRecyclerView() {
        adapter = AnadirContactosAdapter(listaUsuarios)
        binding.rvTodosUsuarios.layoutManager = this.layoutManager
        binding.rvTodosUsuarios.adapter = this.adapter
    }


    private fun leerUsuariosBaseDatos() {
        val db = Firebase.firestore
        val mAuth = FirebaseAuth.getInstance()

        //if (!usuario.idGoogle.equals(mAuth.currentUser?.uid)){

        db.collection(UsuarioModel.USUARIOS)
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    if (!document.data["idGoogle"].toString().equals(mAuth.currentUser?.uid)) {
                        this.listaUsuarios.add(
                            UsuarioModel(
                                document.data["idGoogle"] as String,
                                document.data["nombre"] as String,
                                document.data["usuario"] as String,
                                document.data["photo"] as String
                            )
                        )
                    }
                }
                this.adapter.notifyDataSetChanged()     //Notifica al RecyclerView que hay datos.
            }
            .addOnFailureListener { exception ->
                Log.d(TAG, "Error getting documents: ", exception)
            }
    }

}