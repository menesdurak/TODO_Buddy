package com.menesdurak.todobuddy.common.mapper

interface Mapper<I,O>{
    fun map(input:I):O
}