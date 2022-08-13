package com.jrblanco.guachatear.model

/**
 *  POJO para almacenar los datos de un Chats
 */
data class ChatsModel(
    var idChat: String ="",
    var icono: String = "",
    var nombre: String = "",
    var ultimotexto: String = "",
    var date: String = "",
    var tipoChat: Int = -1,
    //Lista con los mienbros del Chat
    // 2 -> Para chat entre dos
    // +2 -> Grupos
)
