package com.lior.questionnaire.mvvm.data.api

import com.lior.questionnaire.mvvm.data.model.Answer
import com.lior.questionnaire.mvvm.data.model.Question
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface ApiService {

    @GET("/questions/")
    suspend fun getQuestions(): Response<List<Question>>

    @POST("/answers/")
    suspend fun postAnswers(@Body answers: List<Answer>)

}