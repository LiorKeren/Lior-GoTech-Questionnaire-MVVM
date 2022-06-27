package com.lior.questionnaire.mvvm.ui.main.adapter


import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.lior.questionnaire.mvvm.R
import com.lior.questionnaire.mvvm.data.model.Question
class MainAdapter(
    private val questions: ArrayList<Question>
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): QuestionViewHolder{
        val view = LayoutInflater.from(parent.context).inflate(
            R.layout.item_layout, parent,
            false
        )
        return QuestionViewHolder(view)
    }

    override fun getItemCount(): Int = questions.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        /*
        If we will want to create another item ViewHolder for new question type use this:

        when(questions[position].getType()){
        }

         */
        (holder as QuestionViewHolder).bind(questions[position])
    }

    fun addData(list: List<Question>) {
        questions.clear()
        questions.addAll(list)
    }

}