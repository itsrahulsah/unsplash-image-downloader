package com.sample.hackitdemo

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.liveData
import com.sample.hackitdemo.network.models.Image
import com.sample.hackitdemo.repository.ImageRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(private val repository: ImageRepository):ViewModel() {

    val pagingImages:LiveData<PagingData<Image>> = Pager(
        config = PagingConfig(
            pageSize = 20, enablePlaceholders = false),
            pagingSourceFactory = { repository.loadImages() }
            )
            .liveData
            .cachedIn(viewModelScope)
}