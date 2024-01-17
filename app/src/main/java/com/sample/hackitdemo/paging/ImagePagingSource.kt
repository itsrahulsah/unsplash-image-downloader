package com.sample.hackitdemo.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.sample.hackitdemo.network.api.ApiService
import com.sample.hackitdemo.network.models.Image
import com.sample.hackitdemo.repository.ImageRepository

class ImagePagingSource(private val apiService: ApiService):PagingSource<Int,Image>() {
    override fun getRefreshKey(state: PagingState<Int, Image>): Int? {
       return null
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Image> {
        return try {

            val nextPage = params.key ?: 1
            val response = apiService.getImages(nextPage)

            LoadResult.Page(
                data = response,
                prevKey = if (nextPage == 1) null else nextPage - 1,
                nextKey = nextPage + 1
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

}