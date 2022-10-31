package com.pdf.music

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import java.io.ByteArrayOutputStream
import java.util.*

object Converter {

    fun bitmapToString(bitmap: Bitmap): String {
        val stream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)

        val bytes = stream.toByteArray()

        return Base64.getEncoder().encodeToString(bytes)
    }

    fun stringToBitmap(encodedString: String): Bitmap? {
        return try {
            val encodeByte = android.util.Base64.decode(encodedString, android.util.Base64.DEFAULT)
            BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.size)
        } catch (e: Exception) {
            null
        }
    }


}