package com.example.notely.utils

import java.text.SimpleDateFormat
import java.util.*

class Helper {

    fun getTimeNow():String{
        val sdf = SimpleDateFormat("dd/M/yyyy hh:mm")
        val currentDate = sdf.format(Date())
        return currentDate
    }
}