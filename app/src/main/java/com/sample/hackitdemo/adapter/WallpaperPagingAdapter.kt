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

    class ViewHolder(private val binding:WallpaperItemLayoutBinding):RecyclerView.ViewHolder(binding.root){
        fun bind(image: Image?, download:(Image) -> Unit){
            binding.image.load(data = image?.urls?.small, builder = { placeholder(R.drawable.image_placeholder)})
            if (image != null) {
                binding.tvDiscription.text = image.description
                binding.buttonLike.isSelected = image.isLiked
                binding.buttonDownload.setOnClickListener {
                    download.invoke(image)
                }
                binding.buttonLike.setOnCheckedChangeListener { _, isChecked -> image.isLiked = isChecked  }
            }

        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position),download)

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