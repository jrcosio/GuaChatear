package com.jrblanco.guachatear.view

import android.content.ContentValues
import android.content.ContentValues.TAG
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.jrblanco.guachatear.R
import com.jrblanco.guachatear.databinding.ActivityLoginBinding
import com.jrblanco.guachatear.model.ChatsModel
import com.jrblanco.guachatear.model.UsuarioModel

class LoginActivity : AppCompatActivity() {

    private lateinit var googleSingInClient: GoogleSignInClient
    private lateinit var auth: FirebaseAuth
    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = Firebase.auth    // Inicializa el atributo de autentificación de firebase

        // Configura el google con el token
        val googleConf = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        //Inicia el cliente de google
        googleSingInClient = GoogleSignIn.getClient(this,googleConf)

        // Al hacer click en el botón lanza el login de Google.
        binding.btnLogin.setOnClickListener{
            val singIntent = googleSingInClient.signInIntent
            // Este metodo es como se lanza ahora el antiguo startActivityForResult()
            googleLauncher.launch(singIntent) // Lanza la ventana de google auth
        }
    }

    /**
     * Al iniciar el activity se comprueba si esta el usuario logeado en google
     * si lo esta finaliza el acticity de Login y pasa al activity Principal
     */
    override fun onStart() {
        super.onStart()
        val user = auth.currentUser
        if (user != null) {
            finish()
            showPrincipal(user)
        }
    }

    /**
     * Recoge el resultado de la ventana de google para hacer la autentificación
     * NOTA: es la versión actual del onActivityResult
     */
    private val googleLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        val task = GoogleSignIn.getSignedInAccountFromIntent(it.data)
        try {
            val account = task.getResult(ApiException::class.java)!!
            Log.d(ContentValues.TAG,"FirebaseAuthconGoogle: " + account.id)
            firebaseAuthConGoogle(account.idToken!!)
        } catch (e: ApiException) {
            Log.w(ContentValues.TAG, "Error auth en Goolge")
        }

    }

    /**
     * Se autentifica en Google Firebase
     */
    private fun firebaseAuthConGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken,null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) {
                if (it.isSuccessful) {
                    Log.d(ContentValues.TAG, "AuthConCredencial: OK")

                    val db = Firebase.firestore

                    val usuario:UsuarioModel = UsuarioModel(
                        auth.currentUser?.uid.toString(),
                        auth.currentUser?.displayName.toString(),
                        auth.currentUser?.email.toString(),
                        auth.currentUser?.photoUrl.toString()
                    )


                    db.collection(UsuarioModel.USUARIOS).document(usuario.idGoogle)
                        .set(usuario)
                        .addOnSuccessListener {
                            // -- Todos los nuevos usuarios son añadidos al grupo por defecto del reto
                            val grupoDefauls = mapOf("tipo" to "Grupo")

                            db.collection(UsuarioModel.USUARIOS).document(usuario.idGoogle).collection("mischat").document("myJBwknUiOLj1QbKXmgd")
                                .set(grupoDefauls)
                                .addOnSuccessListener { Log.d(TAG,"Grupo por defecto añadido") }
                                .addOnFailureListener { e -> Log.w(TAG, "Error writing document", e) }
                            Toast.makeText(this,"Bienvenid@ ${auth.currentUser?.displayName}",Toast.LENGTH_LONG).show()
                        }
                        .addOnFailureListener { e -> Log.w(TAG, "Error writing document", e) }

                    finish() //Fulmina la activity actual -> la de login
                    showPrincipal(auth.currentUser)
                } else {
                    Log.d(ContentValues.TAG, "AuthConCredencial: ERROR")
                }
            }
    }

    /**
     * Para lanzar el activity Principal
     */
    private fun showPrincipal(user: FirebaseUser?) {
        if (user != null) {
            val principalIntent = Intent(applicationContext,PrincipalActivity::class.java)
            startActivity(principalIntent)

        }
    }
}