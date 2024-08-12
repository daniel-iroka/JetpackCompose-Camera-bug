package com.example.camerabug

import android.content.Context
import android.net.Uri
import android.os.Environment
import androidx.core.content.FileProvider
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

private const val FILENAME_FORMAT = "yyyyMMdd_HHmmss"
private const val PACKAGE_NAME = "com.example.camerabug.provider"

fun Context.generateImageUri() : Uri {
    // Generate file for Image URi
    val timeStamp: String = SimpleDateFormat(FILENAME_FORMAT, Locale.getDefault()).format(Date())
    val storageDir: File = getExternalFilesDir(Environment.DIRECTORY_PICTURES)!!
    val imageFile =  File.createTempFile(
        "JPEG_${timeStamp}_", ".jpg", storageDir
    )
    return FileProvider.getUriForFile(this, PACKAGE_NAME, imageFile)
}