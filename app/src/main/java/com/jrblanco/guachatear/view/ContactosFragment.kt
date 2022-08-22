package com.jrblanco.guachatear.view

import android.content.ContentValues
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.jrblanco.guachatear.adapters.ContactosAdapter
import com.jrblanco.guachatear.databinding.FragmentContactosBinding
import com.jrblanco.guachatear.model.UsuarioModel

class ContactosFragment : Fragment() {

    private lateinit var binding: FragmentContactosBinding
    private val listaContactos = ArrayList<UsuarioModel>()

    val db = Firebase.firestore     //Para gestionar la base de datos

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentContactosBinding.inflate(layoutInflater)

        binding.btnAddContacto.setOnClickListener{

            val showAnadirContacto = AnadirContactoFragment()
            showAnadirContacto.onDismissListener = {
                Toast.makeText(context,"Estoy aqui",Toast.LENGTH_SHORT).show()
                obtenerContactos()
            }
            showAnadirContacto.show((activity as AppCompatActivity).supportFragmentManager,"Anadir_Contacto")
        }
        obtenerContactos()
        initrecyclerview()

        return binding.root
    }

    /**
     * método para rellenar el recyclerview
     */
    private fun initrecyclerview() {
        binding.apply {
            rvContactos.layoutManager = GridLayoutManager(context,2)
            rvContactos.adapter = ContactosAdapter(listaContactos)
        }
    }

    /**
     * método para obtener los contactos de la base de datos (firebase)
     */
    private fun obtenerContactos() {
        val mAuth = FirebaseAuth.getInstance()

        this.listaContactos.clear()

        db.collection(UsuarioModel.USUARIOS).document(mAuth.currentUser?.uid!!).collection(UsuarioModel.CONTACTOS)
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    //if (!document.data["idGoogle"].toString().equals(mAuth.currentUser?.uid)) {
                    this.listaContactos.add(
                        UsuarioModel(
                            document.data["idGoogle"] as String,
                            document.data["nombre"] as String,
                            document.data["usuario"] as String,
                            document.data["photo"] as String
                        )
                    )
                }
                binding.rvContactos.adapter?.notifyDataSetChanged()
            }
            .addOnFailureListener { exception ->
                Log.d(ContentValues.TAG, "Error getting documents: ", exception)
            }
    }


}