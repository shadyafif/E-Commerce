package com.example.shopping.utilies

import android.annotation.TargetApi
import android.content.ContentResolver
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.util.Log
import java.io.IOException

class BitmapResolver {

    companion object {

        private const val TAG = "BitmapResolver"

        private fun getBitmapLegacy(contentResolver: ContentResolver, fileUri: Uri): Bitmap {
            var bitmap: Bitmap? = null

            try {
                bitmap = MediaStore.Images.Media.getBitmap(contentResolver, fileUri)
            } catch (e: IOException) {
                e.printStackTrace()
            }

            return bitmap!!
        }


        @TargetApi(Build.VERSION_CODES.P)
        private fun getBitmapImageDecoder(contentResolver: ContentResolver, fileUri: Uri): Bitmap {
            var bitmap: Bitmap? = null
            try {
                bitmap =
                    ImageDecoder.decodeBitmap(ImageDecoder.createSource(contentResolver, fileUri))
            } catch (e: IOException) {
                e.printStackTrace()
            }
            return bitmap!!
        }


        fun getBitmap(contentResolver: ContentResolver, fileUri: Uri?): Bitmap? {
            if (fileUri == null) {
                Log.i(TAG, "returning null because URI was null")
                return null
            }

            return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                getBitmapImageDecoder(contentResolver, fileUri)
            } else getBitmapLegacy(contentResolver, fileUri)
        }

    }

}