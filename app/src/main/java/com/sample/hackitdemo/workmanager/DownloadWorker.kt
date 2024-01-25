package com.sample.hackitdemo.workmanager

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import com.sample.hackitdemo.R
import java.io.OutputStream
import java.net.URL

class DownloadWorker(private val context: Context, workerParameters: WorkerParameters):CoroutineWorker(context,workerParameters){

    object FileParams{
        const val KEY_IMAGE_URL = "key_image_url"
        const val KEY_FILE_NAME = "key_file_name"
        const val KEY_FILE_URI = "key_file_uri"
    }
    @SuppressLint("MissingPermission")
    override suspend fun doWork(): Result {
        val imageUrl = inputData.getString(FileParams.KEY_IMAGE_URL) ?: ""
        val fileName = inputData.getString(FileParams.KEY_FILE_NAME) ?: ""
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){

            val name = NotificationConstants.CHANNEL_NAME
            val description = NotificationConstants.CHANNEL_DESCRIPTION
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel(NotificationConstants.CHANNEL_ID,name,importance)
            channel.description = description

            val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager?

            notificationManager?.createNotificationChannel(channel)
        }
        val builder = NotificationCompat.Builder(context,NotificationConstants.CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle("Downloading your file...")
            .setOngoing(true)
            .setProgress(0,0,true)


        NotificationManagerCompat.from(context).notify(NotificationConstants.NOTIFICATION_ID,builder.build())

        val uri = saveImageFile(
            fileName = fileName,
            imageUrl = imageUrl,
            context = context
        )
        Log.d("TAG","image url ${uri.toString()}")
        val intent = Intent(Intent.ACTION_VIEW)
        intent.data = uri
        intent.flags = Intent.FLAG_GRANT_READ_URI_PERMISSION.or(Intent.FLAG_GRANT_WRITE_URI_PERMISSION)
//
        val pendingIntent = PendingIntent.getActivity(context,1,intent,if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) PendingIntent.FLAG_IMMUTABLE else PendingIntent.FLAG_ONE_SHOT)
        val doneBuilder = NotificationCompat.Builder(context,NotificationConstants.CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle("Downloading Completed")
            .setContentIntent(pendingIntent)
//            .setOngoing(true)
//            .setContentIntent(pendingIntent)
        NotificationManagerCompat.from(context).notify(NotificationConstants.NOTIFICATION_ID,doneBuilder.build())
        return Result.success(workDataOf(FileParams.KEY_FILE_URI to uri.toString()))
    }


    object NotificationConstants{
        const val CHANNEL_NAME = "download_wallpaper_worker_channel"
        const val CHANNEL_DESCRIPTION = "download_file_worker_demo_description"
        const val CHANNEL_ID = "download_image_worker_channel"
        const val NOTIFICATION_ID = 1
    }

    private fun saveImageFile(
        fileName:String,
        imageUrl:String,
        context: Context): Uri? {
        var fos: OutputStream? = null
        var imageUri:Uri? = null
        context.contentResolver?.also { resolver ->
            val contentValues = ContentValues().apply {
                put(MediaStore.MediaColumns.DISPLAY_NAME, fileName)
                put(MediaStore.MediaColumns.MIME_TYPE, "image/jpg")
                if(Build.VERSION.SDK_INT > Build.VERSION_CODES.P) {
                    put(MediaStore.Images.Media.RELATIVE_PATH, "Pictures/Unsplash Images")
                }
            }
             imageUri = resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)
        imageUri?.let { fos = resolver.openOutputStream(it) }
        }
        URL(imageUrl).openStream().use { input->
            fos?.use {
                input.copyTo(it)
            }
        }
        return imageUri
    }
}