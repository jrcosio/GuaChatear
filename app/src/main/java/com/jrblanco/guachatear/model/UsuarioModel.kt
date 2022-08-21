package com.jrblanco.guachatear.model

/**
 * POJO para los usuarios
 */
data class UsuarioModel(
    var idGoogle: String = "",
    var nombre: String = "",
    var usuario: String = "",
    var photo: String = ""
){
    companion object {
        val USUARIOS = "usuarios"
        val CONTACTOS = "contactos"
    }
}


