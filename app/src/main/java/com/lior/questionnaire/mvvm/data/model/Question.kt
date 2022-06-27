package com.lior.questionnaire.mvvm.data.model

import com.squareup.moshi.Json

data class Question(
    @Json(name = "text")
    val text: String,
    @Json(name = "answers")
    val answers: List<String> = arrayListOf(),
    @Json(name = "isRequired")
    val isRequired: Boolean = false
){
    fun getType(): QuestionType{
        if (answers.isEmpty()) return QuestionType.Text
        return QuestionType.Choose
    }

    override fun equals(other: Any?)
            = (other is Question)
            && other.text == text

    override fun hashCode(): Int {
        var result = text.hashCode()
        result = 31 * result + answers.hashCode()
        result = 31 * result + isRequired.hashCode()
        return result
    }
}
