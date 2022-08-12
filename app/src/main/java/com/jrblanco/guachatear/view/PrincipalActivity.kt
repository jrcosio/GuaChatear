package com.jrblanco.guachatear.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.jrblanco.guachatear.R
import com.jrblanco.guachatear.databinding.ActivityPrincipalBinding
import com.jrblanco.guachatear.model.UsuarioModel

class PrincipalActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPrincipalBinding


    private lateinit var usuario: UsuarioModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPrincipalBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.bnview.itemIconTintList = null
        binding.bnview.selectedItemId = R.id.menu_chats  //Indicamos que al inicio se la opción "guaChat" la marcada
        cargarFragment(ChatsFragment()) //Fragment de los CHATS como inicio

        menuBottomNavigationsView()
    }

    /**
     * Método para las opciones del ButtomNavigationView -> (menú de abajo)
     */
    private fun menuBottomNavigationsView() {
        binding.bnview.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.menu_contactos -> {    // Carga el fragment de los contactos
                   // title = "Contactos"
                    cargarFragment(ContactosFragment())
                    true
                }
                R.id.menu_chats -> {        // Carga el fragment de los Chats
                    //title = "guaChats"
                    cargarFragment(ChatsFragment())
                    true
                }
                R.id.menu_Ajustes -> {      // Carga el fragment de los ajustes
                   // title = "Ajustes"

                    cargarFragment(AjustesFragment())
                    true
                }
                else -> {false}
            }
        }
    }

    /**
     * Método para cambiar el fragment
     *
     * @param fragment El Fragment (clase Fragment) que se quiere usar
     */
    private fun cargarFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.framelayoutprincipal,fragment)
            .commit()
    }
}