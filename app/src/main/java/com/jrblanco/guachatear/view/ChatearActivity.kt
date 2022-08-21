package com.jrblanco.guachatear.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.jrblanco.guachatear.adapters.ChatearAdater
import com.jrblanco.guachatear.databinding.ActivityChatearBinding
import com.jrblanco.guachatear.model.MensajeModel

class ChatearActivity : AppCompatActivity() {
    private lateinit var binding: ActivityChatearBinding
    private val listamensajes = ArrayList<MensajeModel>()

    private val layoutManager = LinearLayoutManager(this)  //Indicamos que tipo de LayoutManager creamos
    private lateinit var adapter: ChatearAdater                   //Define el adapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChatearBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnVolverAtras.setOnClickListener{
            onBackPressed()
        }

        binding.btnEnviar.setOnClickListener{
            if (binding.etxtMensaje.text.isNotEmpty() && binding.etxtMensaje.text.isNotBlank()) {
                val mnsjTemporal = binding.etxtMensaje.text.toString()
                listamensajes.add(MensajeModel("YO","YO",mnsjTemporal,"12:00"))

                adapter.notifyItemInserted(listamensajes.size-1) // Notifica al recyclerView que se ha insertado al final
                layoutManager.scrollToPositionWithOffset(listamensajes.size-1,0) //que el recycler se baje al ultimo...

                binding.etxtMensaje.text.clear() //limpia el edittext de mensajes
            }
        }

        initLista()
        initRecyclerView()
    }

    private fun initRecyclerView() {
        adapter = ChatearAdater(                // Inicializa el Adapter
            conversacion = listamensajes
        )

        binding.rvChatear.layoutManager = layoutManager
        binding.rvChatear.adapter = adapter
        //que el recycler se baje al ultimo...
        layoutManager.scrollToPositionWithOffset(listamensajes.size - 1, 0)
    }

    private fun initLista(){
        listamensajes.add(MensajeModel("YO","YO","Hola","12:00"))
        listamensajes.add(MensajeModel("XX","PEPE","Hola, quien eres???","12:01"))
        listamensajes.add(MensajeModel("YO","YO","Soy Yo, tu eres PEPE","12:00"))
        listamensajes.add(MensajeModel("XX","PEPE","si soy PEPE quien eres tu????","12:00"))
        listamensajes.add(MensajeModel("YO","PEPE","si soy PEPE quien eres tu????","12:00"))
        listamensajes.add(MensajeModel("XX","PEPE - JUAN","si soy PEPE quien eres tu????","12:00"))
        listamensajes.add(MensajeModel("YO","PEPE","si soy PEPE quien eres tu????","12:00"))
        listamensajes.add(MensajeModel("YY","PEPE","si?","12:00"))
        listamensajes.add(MensajeModel("YY","PEPE","si soy PEPE quien eres tu????","12:00"))
        listamensajes.add(MensajeModel("YY","PEPE","si soy PEPE quien eres tu????","12:00"))
        listamensajes.add(MensajeModel("YO","PEPE","????","12:00"))
    }

}