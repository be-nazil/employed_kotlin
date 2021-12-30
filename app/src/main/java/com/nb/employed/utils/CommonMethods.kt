package com.nb.employed.utils

import android.annotation.SuppressLint
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.util.Log
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.ProgressBar
import android.widget.Toast
import com.nb.employed.BuildConfig
import com.nb.employed.data.DeviceDetails
import java.text.ParseException
import java.text.SimpleDateFormat
import java.time.YearMonth
import java.util.*

private const val YYYYMMDD_HHMMSS = "yyyy-MM-dd hh:mm:ss"
private const val YYYYMMDD = "yyyy-MM-dd"
private const val DDMMYYYY_HHMMSS = "dd/MM/yyyy hh:mm:ss"

fun Context.showShortToast(message: String) =
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()


fun ProgressBar.show(){
    visibility = VISIBLE
}

fun ProgressBar.hide(){
    visibility = GONE
}


/**
* Getting number days of a month
* */
fun getLastDayFromMonthYear(intYear: Int, intMonth: Int):Int{
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        val yearMonthObject = YearMonth.of(intYear, intMonth)
        yearMonthObject.lengthOfMonth()
    } else {
        val mycal: Calendar = GregorianCalendar(intYear, intMonth - 1, 1)
        mycal.getActualMaximum(Calendar.DAY_OF_MONTH)
    }
}


fun getMonthFromDate(strDate: String): String{
    var stringDate = strDate
    val convertDate: Date
    try {
        convertDate = SimpleDateFormat(YYYYMMDD, Locale.ENGLISH).parse(strDate)
        stringDate = SimpleDateFormat("MMM", Locale.ENGLISH).format(convertDate)
    }catch (e: ParseException){
        e.printStackTrace()
    }
    return stringDate
}


fun getDDFromDate(strDate: String): Int{
    try {
        val convertDate = SimpleDateFormat(YYYYMMDD_HHMMSS, Locale.ENGLISH).parse(strDate)
        return SimpleDateFormat("dd", Locale.ENGLISH).format(convertDate).toInt()
    }catch (e: ParseException){
        e.printStackTrace()
    }
    return 0
}

fun getDDFromYYYYMMDD(strDate: String): Int{
    try {
        val convertDate = SimpleDateFormat(YYYYMMDD, Locale.ENGLISH).parse(strDate)
        return SimpleDateFormat("dd", Locale.ENGLISH).format(convertDate).toInt()
    }catch (e: ParseException){
        e.printStackTrace()
    }
    return 0
}


@SuppressLint("SimpleDateFormat")
fun getEEEDay(startDate: String, addDay: Int): String{
    try {
        val cal = Calendar.getInstance()
        cal.time = SimpleDateFormat(YYYYMMDD).parse(startDate)
        cal.add(Calendar.DATE, addDay)
        return SimpleDateFormat("EEEE").format(cal.time)
    }catch (e: ParseException){
        e.printStackTrace()
    }
    return startDate
}

/**
* Getting number of hours by adding two dates
* */
fun getNumberOfHours(startDate: String, endDate: String):Int{
    return try {

        val startTime = SimpleDateFormat(YYYYMMDD_HHMMSS, Locale.getDefault()).parse(startDate).time
        val endTime = SimpleDateFormat(YYYYMMDD_HHMMSS, Locale.getDefault()).parse(endDate).time
        val milliToHour: Long = 1000 * 60 * 60

        ((endTime.minus(startTime)).div(milliToHour)).toInt()

    } catch (e: ParseException) {
        e.printStackTrace()
        Log.i("Common", e.message.toString())
    }
}

fun getCurrentDate(): String{
    return SimpleDateFormat(DDMMYYYY_HHMMSS).format(Date())
}


@SuppressLint("HardwareIds")
fun getDeviceDetail(): DeviceDetails {
    return DeviceDetails(
            Build.BRAND,
            Build.MODEL,
            Build.VERSION.RELEASE,
            BuildConfig.VERSION_NAME
    )
}



fun isOnline(context: Context): Boolean {
    val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    if (connectivityManager != null) {
        val capabilities =
                connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
        if (capabilities != null) {
            if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) {
                Log.i("Internet", "NetworkCapabilities.TRANSPORT_CELLULAR")
                return true
            } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
                Log.i("Internet", "NetworkCapabilities.TRANSPORT_WIFI")
                return true
            } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)) {
                Log.i("Internet", "NetworkCapabilities.TRANSPORT_ETHERNET")
                return true
            }
        }
    }
    return false
}