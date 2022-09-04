package com.jrblanco.guachatear.model

import com.google.firebase.Timestamp

/**
 *  POJO para almacenar los datos de un Chats
 */
data class ChatsModel(
    var idChat: String = "",
    var icono: String = "",
    var nombre: String = "",
    var ultimomensaje: String = "",
    var diahora: Timestamp? = null,
    var tipoChat: String = "Grupo"
){
    companion object {
        val CHATS = "chats"
        val MENSAJES = "mensajes"
    }
}
