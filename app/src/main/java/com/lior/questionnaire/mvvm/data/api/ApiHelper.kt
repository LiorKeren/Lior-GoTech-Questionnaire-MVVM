package com.lior.questionnaire.mvvm.data.api

import com.lior.questionnaire.mvvm.data.model.Answer
import com.lior.questionnaire.mvvm.data.model.Question
import retrofit2.Response

interface ApiHelper {

    suspend fun getQuestions(): Response<List<Question>>
    suspend fun postAnswers(answers: List<Answer>)
}