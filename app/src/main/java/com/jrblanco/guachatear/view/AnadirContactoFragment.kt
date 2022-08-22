package com.jrblanco.guachatear.view

import android.content.ContentValues.TAG
import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AutoCompleteTextView
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.jrblanco.guachatear.adapters.AnadirContactosAdapter
import com.jrblanco.guachatear.databinding.FragmentAnadirContactoBinding
import com.jrblanco.guachatear.model.UsuarioModel


class AnadirContactoFragment : DialogFragment() {

    lateinit var onDismissListener : () -> Any

    private lateinit var binding: FragmentAnadirContactoBinding

    val db = Firebase.firestore     //Para gestionar la base de datos

    private val listaUsuarios = ArrayList<UsuarioModel>()

    private val layoutManager = LinearLayoutManager(context)  //Indicamos que tipo de LayoutManager creamos
    private lateinit var adapter: AnadirContactosAdapter      //Define el adapter

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
        adapter = AnadirContactosAdapter(listaUsuarios) {
            onItemSelected(it)
        }
        binding.rvTodosUsuarios.layoutManager = this.layoutManager
        binding.rvTodosUsuarios.adapter = this.adapter
    }


    private fun leerUsuariosBaseDatos() {

        val mAuth = FirebaseAuth.getInstance()

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


    fun onItemSelected(item:UsuarioModel) {
        val mAuth = FirebaseAuth.getInstance() //Quien soy

        val contactoNuevo = UsuarioModel(item.idGoogle,item.nombre,item.usuario,item.photo)


        db.collection(UsuarioModel.USUARIOS).document(mAuth.currentUser?.uid!!)
            .collection(UsuarioModel.CONTACTOS).document(contactoNuevo.idGoogle)
            .set(contactoNuevo)
            .addOnSuccessListener {
                dismiss()
            }
            .addOnFailureListener { e -> Log.w(TAG, "Error writing document", e) }
        //binding.rvBOC.adapter?.notifyDataSetChanged()  //Actualiza el recyclerview
    }

    override fun onDismiss(dialog: DialogInterface) {
        if (this::onDismissListener.isInitialized) {
            onDismissListener()
        }
        super.onDismiss(dialog)
    }
}