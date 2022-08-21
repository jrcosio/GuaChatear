package com.jrblanco.guachatear.view

import android.content.ContentValues
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.jrblanco.guachatear.R
import com.jrblanco.guachatear.adapters.ContactosAdapter
import com.jrblanco.guachatear.databinding.FragmentContactosBinding
import com.jrblanco.guachatear.model.UsuarioModel

class ContactosFragment : Fragment() {

    private lateinit var binding: FragmentContactosBinding
    private val listaContactos = ArrayList<UsuarioModel>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentContactosBinding.inflate(layoutInflater)

        binding.btnAddContacto.setOnClickListener{

            val showAnadirContacto = AnadirContactoFragment()
            showAnadirContacto.show((activity as AppCompatActivity).supportFragmentManager,"Añadir Contacto")


            //1º obtener todos los usuarios del sistema
//            val db = Firebase.firestore
//
//            db.collection(UsuarioModel.USUARIOS)
//                .get()
//                .addOnSuccessListener { result ->
//                    for (document in result) {
//                        println(document.id)
//                        println(document.data.values)
//                        println("Nombre: ${document.data["nombre"]}")
//                       // Log.d(TAG, "${document.id} => ${document.data}")
//                    }
//                }
//                .addOnFailureListener { exception ->
//                    //Log.d(TAG, "Error getting documents: ", exception)
//                }
//
//            //2º Añadir el usuario seleccionado
//
//            val mAuth = FirebaseAuth.getInstance() //Quien soy
//
//            val contactoNuevo = UsuarioModel("njvMU4las0TbUF4ehJUV1RJ7fXJ2","CantabriaMaker","cantabriamaker@gmail.com","https://lh3.googleusercontent.com/a/AItbvmkx_L69qvfFMVCjz4J0sQQRK-o8TquJ2mYACrNK=s96-c")
//
//
//            db.collection(UsuarioModel.USUARIOS).document(mAuth.currentUser?.uid!!)
//                .collection(UsuarioModel.CONTACTOS).document(contactoNuevo.idGoogle)
//                .set(contactoNuevo)
//                .addOnSuccessListener {
//                    Toast.makeText(context,"Contacto añadido",Toast.LENGTH_SHORT).show()
//                }
//                .addOnFailureListener { e -> Log.w(ContentValues.TAG, "Error writing document", e) }

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

    }


//    companion object {
//        val listaContactos = listOf<UsuarioModel>(
//            UsuarioModel("","PEDRO","pedro",""),
//            UsuarioModel("","SANDRA","ssbielva",""),
//            UsuarioModel("","CARMEN","mama",""),
//            UsuarioModel("","EMMA","emmaguti",""),
//            UsuarioModel("","RAMON","rblanco",""),
//            UsuarioModel("","PABLO","pblanco",""),
//            UsuarioModel("","JOSERA","jrablanco","")
//        )
//    }

}