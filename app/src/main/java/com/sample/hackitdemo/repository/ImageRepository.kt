package com.sample.hackitdemo.repository

import com.sample.hackitdemo.network.api.ApiService
import com.sample.hackitdemo.paging.ImagePagingSource
import javax.inject.Inject

class ImageRepository @Inject constructor( private val apiService: ApiService) {

     fun loadImages()= ImagePagingSource(apiService)

}