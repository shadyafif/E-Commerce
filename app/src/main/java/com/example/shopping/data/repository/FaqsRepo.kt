package com.example.shopping.data.repository

import androidx.lifecycle.MutableLiveData
import com.example.shopping.data.model.faqsDetails.FaqsDatum
import com.example.shopping.data.network.IRetrofitApi
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FaqsRepo @Inject constructor(private var api: IRetrofitApi) {
    var faqsList: MutableLiveData<List<FaqsDatum>> = MutableLiveData()
    var faqsLst: List<FaqsDatum>? = null

    fun getFags(): MutableLiveData<List<FaqsDatum>> {
        return faqsList
    }

    suspend fun getFaqsDetails(lang:String) {
        faqsLst = api.getFaqs(lang).data.data
        faqsList.postValue(faqsLst)
    }
}