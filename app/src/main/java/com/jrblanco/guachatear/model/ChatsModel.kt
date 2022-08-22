package com.jrblanco.guachatear.model

/**
 *  POJO para almacenar los datos de un Chats
 */
data class ChatsModel(
    //var idChat: String ="",
    var icono: String = "",
    var nombre: String = "",
    var ultimomensaje: String = "",
    var diahora: String = "",
    var tipoChat: String = "Grupo"
){
    companion object {
        val CHATS = "chats"
        val MENSAJES = "mensajes"
    }
}
