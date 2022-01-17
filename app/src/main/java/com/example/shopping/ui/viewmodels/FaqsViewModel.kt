package com.example.shopping.ui.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shopping.data.model.faqsDetails.FaqsDatum
import com.example.shopping.data.repository.FaqsRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FaqsViewModel @Inject constructor(var repo: FaqsRepo) : ViewModel() {

    fun getDatumList(): MutableLiveData<List<FaqsDatum>> {
        return repo.getFags()
    }

     fun getAllFaqs(lang:String) {
         viewModelScope.launch {
             repo.getFaqsDetails(lang)
         }

    }

}