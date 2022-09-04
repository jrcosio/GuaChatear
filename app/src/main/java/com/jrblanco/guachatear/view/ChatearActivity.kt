package com.jrblanco.guachatear.view

import android.content.ContentValues
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.jrblanco.guachatear.adapters.ChatearAdater
import com.jrblanco.guachatear.databinding.ActivityChatearBinding
import com.jrblanco.guachatear.model.ChatsModel
import com.jrblanco.guachatear.model.MensajeModel
import com.jrblanco.guachatear.model.UsuarioModel
import kotlin.collections.ArrayList

class ChatearActivity : AppCompatActivity() {
    private lateinit var binding: ActivityChatearBinding
    private val listamensajes = ArrayList<MensajeModel>()

    private val layoutManager = LinearLayoutManager(this)  //Indicamos que tipo de LayoutManager creamos
    private lateinit var adapter: ChatearAdater                   //Define el adapter

    private val db = Firebase.firestore     //Para gestionar la base de datos
    val mAuth = FirebaseAuth.getInstance()  //Datos del usuario

    private var datosChat = ChatsModel()

    private lateinit var idChat:String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChatearBinding.inflate(layoutInflater)
        setContentView(binding.root)


        binding.btnVolverAtras.setOnClickListener{
            onBackPressed()
        }

        binding.btnEnviar.setOnClickListener{
            if (binding.etxtMensaje.text.isNotEmpty() && binding.etxtMensaje.text.isNotBlank()) {
                val textoMensaje= binding.etxtMensaje.text.toString()
                val mensaje = MensajeModel(
                    iduser = mAuth.currentUser?.uid.toString(),
                    nombre = mAuth.currentUser?.displayName.toString(),
                    mensaje = textoMensaje,
                    fechayhora = Timestamp.now())
                guardarMensajeEnBaseDatos(mensaje)
                binding.etxtMensaje.text.clear() //limpia el edittext de mensajes
            }
        }

        //recuperamos el IDCHAT que se pasa en el intent
        idChat = intent.getStringExtra("IDCHAT").toString()

        obtenerInfoChat()
        initRecyclerView()
    }

    private fun guardarMensajeEnBaseDatos(mensaje: MensajeModel) {
        db.collection(ChatsModel.CHATS).document(datosChat.idChat).collection(ChatsModel.MENSAJES)
            .add(mensaje)
            .addOnSuccessListener {
                //Actualizamos en los datos del chat el ultimo mensaje y la hora
                val datos = hashMapOf<String, Any>(
                    "ultimomensaje" to "${mensaje.nombre}:${mensaje.mensaje}",
                    "diahora" to mensaje.fechayhora!!)

                db.collection(ChatsModel.CHATS).document(datosChat.idChat)
                    .update(datos)
            }
            .addOnFailureListener { e ->
                Log.w("INFOJR", "Error adding document", e)
            }
    }

    private fun obtenerInfoChat() {
        if (idChat != null) {
            db.collection(ChatsModel.CHATS).document(idChat)
                .get()
                .addOnSuccessListener {
                    datosChat.idChat = idChat
                    datosChat.ultimomensaje = it.data?.get("ultimomensaje") as String
                    datosChat.diahora = it.data?.get("diahora") as Timestamp
                    datosChat.tipoChat = it.data?.get("tipoChat") as String


                    if (datosChat.tipoChat.equals("Grupo")) {
                        datosChat.icono = it.data?.get("icono") as String
                        datosChat.nombre = it.data?.get("nombre") as String
                    } else { //sino es grupo es un chat mostramos icono del otro usuario.
                        obtenerDatosUsuario()
                    }
                    //-------- mostramos info en los componentes ---------
                    binding.txtNombreChatear.text = datosChat.nombre

                    //Lanzar escuchador
                    LeerBasedeDatosEnTiempoReal()
                }
                .addOnFailureListener { Log.d(ContentValues.TAG, "Error getting documents: ", it) }
        }
    }

    private fun obtenerDatosUsuario() {
        db.collection(UsuarioModel.USUARIOS).document(mAuth.currentUser?.uid.toString())
            .collection("mischat").document(idChat)
            .get()
            .addOnSuccessListener {
                datosChat.icono = it.data?.get("icono") as String
                datosChat.nombre = it.data?.get("nombre") as String
                binding.txtNombreChatear.text = datosChat.nombre
                Glide.with(binding.ivIconoChatear.context).load(datosChat.icono).into(binding.ivIconoChatear)
            }
            .addOnFailureListener {  Log.d(ContentValues.TAG, "Error getting documents: ", it)  }
    }

    private fun initRecyclerView() {
        adapter = ChatearAdater(                // Inicializa el Adapter
            conversacion = listamensajes
        )
        binding.rvChatear.layoutManager = layoutManager
        binding.rvChatear.adapter = adapter

    }

    private fun LeerBasedeDatosEnTiempoReal() {

        db.collection(ChatsModel.CHATS).document(datosChat.idChat).collection(ChatsModel.MENSAJES)
            .orderBy("fechayhora", Query.Direction.ASCENDING)
            .addSnapshotListener { snapshot, e ->
                if (e != null) {
                    Log.w("ERRORJR", "Escuchando mensajes ha fallado" + e.message)
                    return@addSnapshotListener
                }
                if (snapshot != null && !snapshot.isEmpty){
                    for (dc in snapshot.documentChanges) {
                        if (dc.type == DocumentChange.Type.ADDED) { //Lo queremos los que son nuevos
                            listamensajes.add(MensajeModel(
                                iduser = dc.document.data["iduser"] as String,
                                nombre = dc.document.data["nombre"] as String,
                                mensaje = dc.document.data["mensaje"] as String,
                                fechayhora = dc.document.data["fechayhora"] as Timestamp)
                            )
                            binding.rvChatear.adapter?.notifyDataSetChanged()
                            //que el recycler se baje al ultimo...
                            layoutManager.scrollToPositionWithOffset(listamensajes.size - 1, 0)
                            //Log.i("INFOJR", dc.document.toString())
                        }
                    }
                }

            }

    }

}