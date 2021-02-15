package com.dlutrix.eonetunofficial.ui.home

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dlutrix.eonetunofficial.data.model.DetailCategories
import com.dlutrix.eonetunofficial.data.model.EventCategories
import com.dlutrix.eonetunofficial.repository.detailCategories.DetailCategoriesRepository
import com.dlutrix.eonetunofficial.repository.eventCategories.EventCategoriesRepository
import com.dlutrix.eonetunofficial.utils.DispatcherProvider
import com.dlutrix.eonetunofficial.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor (
    private val categoriesRepository: EventCategoriesRepository,
    private val detailCategoriesRepository: DetailCategoriesRepository,
    private val dispatcherProvider: DispatcherProvider,
) : ViewModel() {

    init {
        viewModelScope.launch(dispatcherProvider.io) {
            categoriesRepository.categories.onStart {
                _categories.value = CategoriesEvent.Loading
            }.collect {
                when (it) {
                    is Resource.Success -> {
                        _categories.value = CategoriesEvent.Success(it.data!!)
                    }
                    is Resource.Error -> {
                        _categories.value = CategoriesEvent.Failure(it.message!!)
                    }
                }
            }
        }
    }

    sealed class CategoriesEvent {
        class Success(val result: EventCategories) : CategoriesEvent()
        class Failure(val errorText: String) : CategoriesEvent()
        object Loading : CategoriesEvent()
        object Empty : CategoriesEvent()
    }

    sealed class DetailCategoriesEvent {
        class Success(val result: DetailCategories): DetailCategoriesEvent()
        class Failure(val errorText: String): DetailCategoriesEvent()
        object Loading : DetailCategoriesEvent()
        object Empty: DetailCategoriesEvent()
    }

    private val _categories = MutableStateFlow<CategoriesEvent>(CategoriesEvent.Empty)
    val categories: StateFlow<CategoriesEvent> get() = _categories

    private val _detailCategories = MutableStateFlow<DetailCategoriesEvent>(DetailCategoriesEvent.Empty)
    val detailCategories: StateFlow<DetailCategoriesEvent> get() = _detailCategories

    fun getDetailCategories(categoryName: String) = viewModelScope.launch(dispatcherProvider.io) {
        detailCategoriesRepository.getDetailCategories(categoryName).onStart {
            _detailCategories.value = DetailCategoriesEvent.Loading
        }.collect {
            when(it) {
                is Resource.Success -> {
                    _detailCategories.value = DetailCategoriesEvent.Success(it.data!!)
                }
                is Resource.Error -> {
                    _detailCategories.value = DetailCategoriesEvent.Failure(it.message!!)
                }
            }
        }
    }
}