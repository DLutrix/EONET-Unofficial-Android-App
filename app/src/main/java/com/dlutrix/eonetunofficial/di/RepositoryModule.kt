package com.dlutrix.eonetunofficial.di

import com.dlutrix.eonetunofficial.data.source.detailCategories.DetailCategoriesRemoteDataSource
import com.dlutrix.eonetunofficial.data.source.eventCategories.EventCategoriesRemoteDataSource
import com.dlutrix.eonetunofficial.repository.detailCategories.DetailCategoriesRepository
import com.dlutrix.eonetunofficial.repository.eventCategories.EventCategoriesRepository
import com.dlutrix.eonetunofficial.utils.DispatcherProvider
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

@Module
@InstallIn(ViewModelComponent::class)
object RepositoryModule {

    @Provides
    @ViewModelScoped
    fun provideDetailCategoriesRepository(
            remoteDataSource: DetailCategoriesRemoteDataSource
    ): DetailCategoriesRepository = DetailCategoriesRepository(remoteDataSource)

    @Provides
    @ViewModelScoped
    fun provideEventCategoriesRepository(
            remoteDataSource: EventCategoriesRemoteDataSource
    ): EventCategoriesRepository = EventCategoriesRepository(remoteDataSource)

    @Provides
    @ViewModelScoped
    fun provideDispatchers(): DispatcherProvider = object : DispatcherProvider {
        override val main: CoroutineDispatcher
            get() = Dispatchers.Main
        override val io: CoroutineDispatcher
            get() = Dispatchers.IO
        override val default: CoroutineDispatcher
            get() = Dispatchers.Default
        override val unconfined: CoroutineDispatcher
            get() = Dispatchers.Unconfined
    }
}