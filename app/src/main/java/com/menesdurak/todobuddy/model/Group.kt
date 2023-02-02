package com.menesdurak.todobuddy.model

import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
data class Group(
    val title: String? = null,
    val notes: ArrayList<Note>? = null,
    val userIds: ArrayList<String>? = null,
    val key: String? = null
)