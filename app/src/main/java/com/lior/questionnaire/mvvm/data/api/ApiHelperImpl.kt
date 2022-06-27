package com.lior.questionnaire.mvvm.data.api

import com.lior.questionnaire.mvvm.data.model.Answer
import com.lior.questionnaire.mvvm.data.model.Question
import retrofit2.Response

class ApiHelperImpl(private val apiService: ApiService) : ApiHelper {

    override suspend fun getQuestions(): Response<List<Question>> = apiService.getQuestions()

    override suspend fun postAnswers(answers: List<Answer>) = apiService.postAnswers(answers)

}