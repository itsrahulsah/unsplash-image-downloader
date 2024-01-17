package com.sample.hackitdemo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.work.Constraints
import androidx.work.Data
import androidx.work.ExistingWorkPolicy
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkInfo
import androidx.work.WorkManager
import com.google.android.material.snackbar.Snackbar
import com.sample.hackitdemo.adapter.WallpaperPagingAdapter
import com.sample.hackitdemo.databinding.ActivityMainBinding
import com.sample.hackitdemo.network.models.Image
import com.sample.hackitdemo.workmanager.DownloadWorker
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding:ActivityMainBinding
    private lateinit var adapter:WallpaperPagingAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
          val viewModel:MainViewModel by viewModels()
        adapter = WallpaperPagingAdapter{image ->
            startDownloadingWallpaper(image)
        }
        binding.recyclerview.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            setHasFixedSize(true)
        }
        binding.recyclerview.adapter = adapter
        viewModel.pagingImages.observe(this){
            adapter.submitData(this.lifecycle,it)
        }
    }


    private fun startDownloadingWallpaper(wallpaper: Image){
        val workManager = WorkManager.getInstance(this)
        val data = Data.Builder()

        data.apply {
            putString(DownloadWorker.FileParams.KEY_FILE_NAME, "${wallpaper.description}.jpg")
            putString(DownloadWorker.FileParams.KEY_IMAGE_URL, wallpaper.urls.full)
        }

        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .setRequiresStorageNotLow(true)
            .setRequiresBatteryNotLow(true)
            .build()

        val downloadWorker = OneTimeWorkRequestBuilder<DownloadWorker>()
            .setConstraints(constraints)
            .setInputData(data.build())
            .build()

        workManager.enqueueUniqueWork(
            "oneFileDownloadWork_${System.currentTimeMillis()}",
            ExistingWorkPolicy.KEEP,
            downloadWorker
        )

        workManager.getWorkInfoByIdLiveData(downloadWorker.id)
            .observe(this){ info->
                info?.let {
                    when (it.state) {
                        WorkInfo.State.SUCCEEDED -> {
                            showSnackbar("Wallpaper downloaded....")
                        }
                        WorkInfo.State.FAILED -> {
                            showSnackbar("Downloading failed!")
                        }
                        WorkInfo.State.RUNNING ->{

                        }
                        else -> {

                        }
                    }
                }
            }
    }

    private fun showSnackbar(message:String){
        Snackbar.make(
            binding.root,
            message,
            Snackbar.LENGTH_LONG
        ).show()
    }
}