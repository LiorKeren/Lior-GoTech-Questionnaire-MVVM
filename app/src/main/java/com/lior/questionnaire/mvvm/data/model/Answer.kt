package com.lior.questionnaire.mvvm.data.model

import com.squareup.moshi.Json

data class Answer(
    @Json(name = "question")
    val question: String,
    @Json(name = "text")
    val answerText: String = " "
){

    override fun equals(other: Any?)
            = (other is Answer)
            && other.question == question

    override fun hashCode(): Int {
        var result = answerText.hashCode()
        result = 31 * result + question.hashCode()
        return result
    }
}
