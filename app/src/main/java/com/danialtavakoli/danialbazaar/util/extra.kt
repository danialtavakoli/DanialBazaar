package com.danialtavakoli.danialbazaar.util

import android.content.Context
import android.util.Log
import android.widget.Toast
import kotlinx.coroutines.CoroutineExceptionHandler
import java.text.SimpleDateFormat
import java.util.*

val coroutineExceptionHandler = CoroutineExceptionHandler { _, throwable ->
    Log.v("error", "Error -> " + throwable.message)
}

fun stylePrice(oldPrice: String): String {
    if (oldPrice.length > 3) {
        val reversed = oldPrice.reversed()
        var newPrice = ""
        for (i in oldPrice.indices) {
            newPrice += reversed[i]
            if ((i + 1) % 3 == 0) newPrice += ','
        }
        val finalString = newPrice.reversed()
        if (finalString.first() == ',') return "${finalString.substring(1)} Tomans"
        return "$finalString Tomans"
    }
    return "$oldPrice Tomans"
}

fun styleTime(timeInMillis: Long): String {
    val formatter = SimpleDateFormat("yyyy/mm/dd hh:mm")
    val calendar = Calendar.getInstance()
    calendar.timeInMillis = timeInMillis
    return formatter.format(calendar.time)
}


fun Context.showToast(message: String) {
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}

fun Context.showToastInternet() {
    Toast.makeText(this, "Please connect to internet", Toast.LENGTH_SHORT).show()
}