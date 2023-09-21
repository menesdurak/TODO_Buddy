package com.menesdurak.todobuddy.data.local.entity

import java.io.Serializable

data class Note(
    var note: String,
    var isDrawn: Boolean = false,
    var noteReference: String
) : Serializable