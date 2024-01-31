package com.sample.hackitdemo.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.sample.hackitdemo.R
import com.sample.hackitdemo.databinding.WallpaperItemLayoutBinding
import com.sample.hackitdemo.network.models.Image

class WallpaperPagingAdapter(private val download:(Image) -> Unit):PagingDataAdapter<Image,WallpaperPagingAdapter.ViewHolder>(ImageDiffCallBack) {

    class ViewHolder(val binding:WallpaperItemLayoutBinding):RecyclerView.ViewHolder(binding.root)

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        with(holder) {
            val image = getItem(position)
            binding.image.load(data = image?.urls?.small, builder = { placeholder(R.drawable.image_placeholder)})
            if (image != null) {
                binding.tvDiscription.text = image.description
                binding.buttonLike.isChecked = image.isLiked
                binding.buttonDownload.setOnClickListener {
                    download.invoke(image)
                }
                binding.buttonLike.setOnClickListener{  image.isLiked = binding.buttonLike.isChecked  }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = WallpaperItemLayoutBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ViewHolder(binding)
    }



    object ImageDiffCallBack : DiffUtil.ItemCallback<Image>() {
        override fun areItemsTheSame(oldItem: Image, newItem: Image): Boolean {
            // Id is unique.
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Image, newItem: Image): Boolean {
            return oldItem == newItem
        }
    }
}