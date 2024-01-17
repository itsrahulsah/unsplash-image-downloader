package com.sample.hackitdemo.workmanager

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.net.Uri
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.net.toUri
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import com.sample.hackitdemo.R
import java.io.File
import java.io.FileOutputStream
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

        NotificationManagerCompat.from(context).cancel(NotificationConstants.NOTIFICATION_ID)
        Log.d("Download path",uri.toString())
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
        context: Context): Uri {
       val rootPath = context.getExternalFilesDir(null)?.absolutePath
        val file = File(rootPath, fileName)
        URL(imageUrl).openStream().use { input->
            FileOutputStream(file).use { output ->
                input.copyTo(output)
            }
        }
        return file.toUri()
    }
}