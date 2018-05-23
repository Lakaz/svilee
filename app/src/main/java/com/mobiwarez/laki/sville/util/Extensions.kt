package com.mobiwarez.laki.sville.util

import android.content.Context
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import java.math.BigDecimal


/*
fun Double.roundTo2DecimalPlaces(){
    BigDecimal(this).setScale(2, BigDecimal.ROUND_HALF_UP).toDouble()
}
*/

fun Double.roundTo2DecimalPlaces() =
        BigDecimal(this).setScale(2, BigDecimal.ROUND_HALF_UP).toDouble()

inline fun <reified T: AppCompatActivity> Context.startActivity(){

    val intent = Intent(this, T::class.java)
    startActivity(intent)
}

fun Context.showToast(message: String) {
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}

fun Context.showLongToast(message: String) {
    Toast.makeText(this, message, Toast.LENGTH_LONG).show()
}


fun ViewGroup.inflate(layoutId: Int, attachToRoot: Boolean = false): View =
        LayoutInflater.from(context).inflate(layoutId, this, attachToRoot)
