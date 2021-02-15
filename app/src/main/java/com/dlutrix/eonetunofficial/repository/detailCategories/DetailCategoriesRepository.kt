package com.dlutrix.eonetunofficial.repository.detailCategories

import com.dlutrix.eonetunofficial.data.model.DetailCategories
import com.dlutrix.eonetunofficial.data.source.detailCategories.DetailCategoriesRemoteDataSource
import com.dlutrix.eonetunofficial.utils.Resource
import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import retrofit2.Response
import javax.inject.Inject
import javax.inject.Singleton

class DetailCategoriesRepository @Inject constructor(
    private val remoteDataSource: DetailCategoriesRemoteDataSource
) {

    fun getDetailCategories(categoryName: String) = flow<Resource<DetailCategories>> {
        remoteDataSource.getDetailCategories(categoryName).catch { e ->
            emit(Resource.Error(e.message ?: "Unexpected Error"))
        }.collect {
            if (it.isSuccessful) {
                emit(Resource.Success(it.body()!!))
            } else {
                emit(Resource.Error(it.message() ?: "An error Occurred"))
            }
        }
    }
}