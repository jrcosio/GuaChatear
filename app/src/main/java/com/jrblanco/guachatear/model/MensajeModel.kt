package com.jrblanco.guachatear.model

import com.google.firebase.Timestamp
import java.util.*

data class MensajeModel(
    var iduser: String = "",
    var nombre: String = "",
    var mensaje: String = "",
    var fechayhora: Timestamp? = null
)
