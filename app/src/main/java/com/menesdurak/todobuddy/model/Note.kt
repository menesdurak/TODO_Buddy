package com.menesdurak.todobuddy.model

import com.google.firebase.database.IgnoreExtraProperties
@IgnoreExtraProperties
data class Note (val note: String? = null, val drawn: Boolean? = false)