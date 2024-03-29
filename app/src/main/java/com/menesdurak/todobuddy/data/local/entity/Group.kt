package com.menesdurak.todobuddy.data.local.entity

data class Group(
    val name: String,
    val buddysEmails: List<String>,
    val notes: List<Note> = emptyList(),
    val groupReference: String
)
