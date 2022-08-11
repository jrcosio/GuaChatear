package com.jrblanco.guachatear.view

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.jrblanco.guachatear.R
import com.jrblanco.guachatear.databinding.FragmentAjustesBinding
import com.jrblanco.guachatear.model.UsuarioModel

class AjustesFragment: Fragment() {

    private lateinit var binding: FragmentAjustesBinding


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentAjustesBinding.inflate(layoutInflater)


        val mAuth = FirebaseAuth.getInstance()
        // Configura el google con el token
        val googleConf = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        //Inicia el cliente de google (GoogleSignInClient)
        val mGoogle = GoogleSignIn.getClient(requireActivity().applicationContext,googleConf)
                                        //El context se puede obtener de un componente: binding.contenedor.context

        binding.apply {
            txtNombre.text = Firebase.auth.currentUser?.displayName
            txtemail.text = Firebase.auth.currentUser?.email
            Glide.with(ivAvatar.context).load(Firebase.auth.currentUser?.photoUrl).into(ivAvatar)

            btnCerrarSesion.setOnClickListener {
                //Firebase.auth.signOut() //Cierra la sesión con Firebase
                mGoogle.signOut()   //Cierra sesión de Google
                mAuth.signOut()     //Cierra sesión de Firebase
                activity?.finish()  //Finaliza el Activity Principal
                val loginIntent= Intent(context,LoginActivity::class.java)
                loginIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(loginIntent)

            }
        }

        return binding.root
    }
}