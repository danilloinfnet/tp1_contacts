package com.example.contatos

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import java.io.File
import java.io.IOException

class PhotosStorage {
    companion object {
        fun loadPhotoFromInternalStorage(context: Context?, filename: String): Drawable? {
            val file = File(context?.filesDir, filename)
            return Drawable.createFromPath(file.toString())
        }

        fun savePhotoInternalToInternalStorage(context: Context?, filename: String, bmp: Bitmap): String {
            return try {
                val filenameExt = "$filename.jpg"
                context?.openFileOutput(filenameExt, Context.MODE_PRIVATE).use { stream ->
                    if (!bmp.compress(Bitmap.CompressFormat.JPEG, 95, stream)) {
                        throw IOException("Couldn't save bitmap")
                    }

                    filenameExt
                }
            } catch (e: IOException) {
                e.printStackTrace()
                
                ""
            }
        }
        
        fun deletePhotoFromInternalStorage(context: Context?, filename: String): Boolean {
            val file = File(context?.filesDir, filename)
            return file.delete()
        }
    }
}