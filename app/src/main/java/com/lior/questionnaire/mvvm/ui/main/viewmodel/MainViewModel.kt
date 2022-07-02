package com.lior.questionnaire.mvvm.ui.main.viewmodel

import androidx.lifecycle.*
import com.lior.questionnaire.mvvm.data.model.Answer
import com.lior.questionnaire.mvvm.data.model.Question
import com.lior.questionnaire.mvvm.data.repository.MainRepository
import com.lior.questionnaire.mvvm.data.rx.RxDataPass
import com.lior.questionnaire.mvvm.utils.NetworkHelper
import com.lior.questionnaire.mvvm.utils.Resource
import io.reactivex.rxjava3.disposables.CompositeDisposable
import kotlinx.coroutines.launch

class MainViewModel(
    private val mainRepository: MainRepository,
    private val networkHelper: NetworkHelper,
    private val rxDataPass: RxDataPass
) : ViewModel() {

    private val answers: MutableList<Answer> = mutableListOf()

    private var _compositeDisposable = CompositeDisposable()
    private val compositeDisposable: CompositeDisposable
    get() = _compositeDisposable

    private val _questions = MutableLiveData<Resource<List<Question>>>()
    val questions: LiveData<Resource<List<Question>>>
        get() = _questions

    init {
        fetchQuestions()
        getAnswerData()
    }

    private fun getAnswerData(){
        compositeDisposable.add(
        rxDataPass.getListItemClickSubject().subscribe { answer ->
            if (answers.contains(answer)) answers.remove(answer)
            answers.add(answer)
        })
    }

    fun postAnswers(){
        viewModelScope.launch {
        val isRequiredList: List<Question> = _questions.value?.data!!.filter { question -> question.isRequired  }
        var isAllRequiredFilled = false
            for(q in isRequiredList){
                if (!answers.contains(Answer(question = q.text))){
                    isAllRequiredFilled = false
                    break
                }
                isAllRequiredFilled = true
            }
            if (isAllRequiredFilled && networkHelper.isNetworkConnected() && answers.isNotEmpty()){
                mainRepository.postAnswers(answers.toList())
                answers.clear()
                fetchQuestions()
            }

        }
    }

    private fun fetchQuestions() {
        viewModelScope.launch {
            _questions.postValue(Resource.loading(null))
            if (networkHelper.isNetworkConnected()) {
                mainRepository.getQuestions().let {
                    if (it.isSuccessful) {
                        _questions.postValue(Resource.success(it.body()))
                    } else _questions.postValue(Resource.error(it.errorBody().toString(), null))

                }
            } else _questions.postValue(Resource.error("No internet connection", null))
        }
    }
}