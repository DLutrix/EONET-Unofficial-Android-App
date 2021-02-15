package com.dlutrix.eonetunofficial.data.source.eventCategories

import com.dlutrix.eonetunofficial.data.source.EonetApiService
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class EventCategoriesRemoteDataSource @Inject constructor(
    private val api: EonetApiService
) {

    val categories = flow {
        emit(api.getCategories())
    }
}