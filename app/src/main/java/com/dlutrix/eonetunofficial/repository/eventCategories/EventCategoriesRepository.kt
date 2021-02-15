package com.dlutrix.eonetunofficial.repository.eventCategories

import com.dlutrix.eonetunofficial.data.model.EventCategories
import com.dlutrix.eonetunofficial.data.source.eventCategories.EventCategoriesRemoteDataSource
import com.dlutrix.eonetunofficial.utils.Resource
import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

class EventCategoriesRepository @Inject constructor(
    private val remoteDataSource: EventCategoriesRemoteDataSource
) {

    val categories = flow<Resource<EventCategories>> {
        remoteDataSource.categories.catch { e ->
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