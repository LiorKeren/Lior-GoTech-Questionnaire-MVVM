package com.lior.questionnaire.mvvm.ui.main.view

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.lior.questionnaire.mvvm.R
import com.lior.questionnaire.mvvm.data.model.Question
import com.lior.questionnaire.mvvm.ui.main.adapter.MainAdapter
import com.lior.questionnaire.mvvm.ui.main.viewmodel.MainViewModel
import com.lior.questionnaire.mvvm.utils.Status
import kotlinx.android.synthetic.main.activity_main.*
import org.koin.android.viewmodel.ext.android.viewModel

class MainActivity : AppCompatActivity() {

    private val mainViewModel : MainViewModel by viewModel()
    private lateinit var adapter: MainAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setupUI()
        setupObserver()

    }

    private fun setupUI() {
        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = MainAdapter(arrayListOf())
        recyclerView.adapter = adapter
        submitButton.setOnClickListener { mainViewModel.postAnswers() }
    }

    private fun setupObserver() {
        mainViewModel.questions.observe(this, Observer {
            when (it.status) {
                Status.SUCCESS -> {
                    progressBar.visibility = View.GONE
                    it.data?.let { questions -> renderList(questions) }
                    recyclerView.visibility = View.VISIBLE
                }
                Status.LOADING -> {
                    progressBar.visibility = View.VISIBLE
                    recyclerView.visibility = View.GONE
                }
                Status.ERROR -> {
                    //Handle Error
                    progressBar.visibility = View.GONE
                    Toast.makeText(this, it.message, Toast.LENGTH_LONG).show()
                }
            }
        })

        mainViewModel.isAllAnswersFilled.observe(this, Observer {
            val toastMessageStr  = if (it){
                "Thank you for your time !"
            }else{
                "Please fill all required questions !"
            }
            Toast.makeText(this, toastMessageStr, Toast.LENGTH_LONG).show()
        })

    }

    private fun renderList(questions: List<Question>) {
        adapter.addData(questions)
        adapter.notifyDataSetChanged()
    }

}
