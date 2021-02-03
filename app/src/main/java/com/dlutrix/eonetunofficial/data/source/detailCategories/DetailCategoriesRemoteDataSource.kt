package com.dlutrix.eonetunofficial.data.source.detailCategories

import com.dlutrix.eonetunofficial.data.source.EonetApiService
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DetailCategoriesRemoteDataSource @Inject constructor(
    private val api: EonetApiService
) {

    fun getDetailCategories(categoryName: String) = flow {
        emit(api.getDetailCategories(categoryName))
    }
}