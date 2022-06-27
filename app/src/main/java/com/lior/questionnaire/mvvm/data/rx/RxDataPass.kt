package com.lior.questionnaire.mvvm.data.rx

import com.lior.questionnaire.mvvm.data.model.Answer
import io.reactivex.rxjava3.subjects.BehaviorSubject

class RxDataPass {

    private val behaviorSubject: BehaviorSubject<Answer> = BehaviorSubject.create()

    //Subscribe an Answer when item click
    fun getListItemClickSubject(): BehaviorSubject<Answer> {
        return behaviorSubject
    }
}