package com.lior.questionnaire.mvvm.ui.main.viewmodel

import android.util.Log
import androidx.lifecycle.*
import com.lior.questionnaire.mvvm.data.model.Answer
import com.lior.questionnaire.mvvm.data.model.Question
import com.lior.questionnaire.mvvm.data.repository.MainRepository
import com.lior.questionnaire.mvvm.data.rx.RxDataPass
import com.lior.questionnaire.mvvm.utils.NetworkHelper
import com.lior.questionnaire.mvvm.utils.Resource
import io.reactivex.rxjava3.disposables.CompositeDisposable
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import okhttp3.internal.wait

class MainViewModel(
    private val mainRepository: MainRepository,
    private val networkHelper: NetworkHelper,
    private val rxDataPass: RxDataPass
) : ViewModel() {

    private val answers: MutableList<Answer> = mutableListOf()
    private var questionList: List<Question>? = listOf()

    private var _compositeDisposable = CompositeDisposable()
    val compositeDisposable: CompositeDisposable
    get() = _compositeDisposable

    private val _questions = MutableLiveData<Resource<List<Question>>>()
    val questions: LiveData<Resource<List<Question>>>
        get() = _questions

    init {
        fetchQuestions()
        getAnswerData()
//        postAnswers()
    }

    private fun getAnswerData(){
        compositeDisposable.add(
        rxDataPass.getListItemClickSubject().subscribe { answer ->
//            val isRequiredList: List<Question> = questionList!!.filter { question -> question.isRequired  }
//            if (answer.answerText.isEmpty())return@subscribe
            if (answers.contains(answer)) answers.remove(answer)
            answers.add(answer)
            Log.i("aaaaa", "answers added ${answers}")

        })
    }

    fun postAnswers(){
        viewModelScope.launch {
        val isRequiredList: List<Question> = questionList!!.filter { question -> question.isRequired  }
        var isAllRequiredFilled = false
            for(q in isRequiredList){
                if (!answers.contains(Answer(answerText = "", question = q.text))){
                    isAllRequiredFilled = false
                    Log.i("aaaa", "need to fill all questions $answers")
                    break
                }
                isAllRequiredFilled = true
            }
            if (isAllRequiredFilled && networkHelper.isNetworkConnected() && answers.isNotEmpty()){
                mainRepository.postAnswers(answers.toList())
                Log.i("aaaaa", "answers cleared ${answers}")
                answers.clear()


//                answers.clear()
                fetchQuestions()
            }else Log.i("aaaa", "222 need to fill all questions $answers")

        }
    }

    private fun fetchQuestions() {
        viewModelScope.launch {
            _questions.postValue(Resource.loading(null))
            if (networkHelper.isNetworkConnected()) {
                mainRepository.getQuestions().let {
                    if (it.isSuccessful) {
                        questionList = it.body()
                        _questions.postValue(Resource.success(it.body()))
                    } else _questions.postValue(Resource.error(it.errorBody().toString(), null))

                }
            } else _questions.postValue(Resource.error("No internet connection", null))
        }
    }
}