package com.lior.questionnaire.mvvm.data.repository

import com.lior.questionnaire.mvvm.data.api.ApiHelper
import com.lior.questionnaire.mvvm.data.model.Answer

class MainRepository (private val apiHelper: ApiHelper) {

    suspend fun getQuestions() =  apiHelper.getQuestions()
    suspend fun postAnswers(answers: List<Answer>) = apiHelper.postAnswers(answers)

}