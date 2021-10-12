package com.example.shopping.ui.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.shopping.data.model.categoriesDetails.CategoryDatum
import com.example.shopping.data.repository.CategoryRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class CategoryViewModel @Inject constructor(private val repo: CategoryRepo) : ViewModel() {

    fun getDatumList(): MutableLiveData<List<CategoryDatum>> {
        return repo.getCategories()
    }

    suspend fun getAllCategories(lang:String) {
        repo.getCategoriesListRepo(lang)
    }
}