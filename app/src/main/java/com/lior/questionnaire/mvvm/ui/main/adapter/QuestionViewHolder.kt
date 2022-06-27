package com.lior.questionnaire.mvvm.ui.main.adapter

import android.os.Build
import android.text.Editable
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.content.ContextCompat
import androidx.core.view.contains
import androidx.core.widget.doAfterTextChanged
import androidx.recyclerview.widget.RecyclerView
import com.lior.questionnaire.mvvm.R
import com.lior.questionnaire.mvvm.data.model.Answer
import com.lior.questionnaire.mvvm.data.model.Question
import com.lior.questionnaire.mvvm.data.model.QuestionType
import com.lior.questionnaire.mvvm.data.rx.RxDataPass
import kotlinx.android.synthetic.main.item_layout.view.*
import org.koin.core.KoinComponent
import org.koin.core.inject

class QuestionViewHolder (itemView: View) : RecyclerView.ViewHolder(itemView), KoinComponent {
    private val rxDataPass: RxDataPass by inject()
    private lateinit var questionText: String
    private val radioEditText = EditText(itemView.context)

    init {
//        itemView.radioGroup.setOnCheckedChangeListener {group, checkedId ->
//            val radio:RadioButton = group.findViewById(checkedId)
//            var text = radio.text.toString()
//            if (text.contains("Other")){
//                text = "$text ${radioEditText.text}"
//            }
//            postAnswer(text)
//
//        }
//        itemView.answerTypeTextEditText.doAfterTextChanged {
//            Log.i("aaaa", "itemView.answerTypeTextEditText.doAfterTextChanged $it.toString()")
//
//            postAnswer(it.toString())
//        }
    }

    private fun postAnswer(answerText: String){
        if (answerText.isNotEmpty())rxDataPass.getListItemClickSubject().onNext(Answer(answerText = answerText, question = questionText))
    }

    fun bind(question: Question) {
        questionText = question.text
        //For re loading the list because we are using recyclerView the prev data is still there
        itemView.radioGroup.removeAllViews()
        itemView.radioGroup.clearCheck( )


        itemView.answerTypeTextEditText.visibility = View.GONE
        itemView.radioGroup.visibility = View.GONE

        itemView.answerTypeTextEditText.text = Editable.Factory.getInstance().newEditable("")
        when(question.getType()){
            QuestionType.Choose -> {
                //Dynamically create the RadioButton's by list size
                addRadioButtons(question, itemView.radioGroup, itemView.multipleChoiceRelativeLayout)

            }

            QuestionType.Text -> {
                itemView.answerTypeTextEditText.visibility = View.VISIBLE
                itemView.answerTypeTextEditText.doAfterTextChanged {
                    Log.i("aaaa", "itemView.answerTypeTextEditText.doAfterTextChanged $it.toString()")
                    if(it!!.isNotEmpty()) postAnswer(it.toString())
                }

            }

            QuestionType.Unknown -> {
                //Use For new values and testing
            }
        }

        itemView.textViewUserName.text = question.text

        //Create view for required question
        if(question.isRequired) {
            val required = SpannableString(" *")
            required.setSpan(ForegroundColorSpan(ContextCompat.getColor(itemView.context, R.color.red)), 0, required.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
            itemView.textViewUserName.append(required)
        }

    }

    private fun addRadioButtons(question: Question, radioGroup: RadioGroup, parentView: RelativeLayout) {
        radioGroup.setOnCheckedChangeListener {group, checkedId ->
            if(checkedId != -1) {
                val radio: RadioButton = group.findViewById(checkedId)
                var text = radio.text.toString()
                if (text.contains("Other")) {
                    text = "$text ${radioEditText.text}"
                }
                Log.i("aaaa", "itemView.radioGroup.setOnCheckedChangeListener $text  $checkedId")

                postAnswer(text)
            }

        }
        radioGroup.visibility = View.VISIBLE
        radioGroup.orientation = LinearLayout.VERTICAL

        for (i in 1..question.answers.size) {
            val rdbtn = RadioButton(radioGroup.context)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                rdbtn.id = View.generateViewId()
            }
            val questionText = question.answers[i-1]
            rdbtn.text = questionText

            radioGroup.addView(rdbtn)
            if (questionText.contains("Other:")){
                if (parentView.contains(radioEditText)){
                    radioEditText.text = Editable.Factory.getInstance().newEditable("")
                    parentView.removeView(radioEditText)
                }

//                val editText = EditText(radioGroup.context)
                radioEditText.id = R.id.choiceEditText
                val layoutParams = RelativeLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
                )
                layoutParams.addRule(RelativeLayout.ALIGN_BOTTOM, radioGroup.id)
                layoutParams.addRule(RelativeLayout.RIGHT_OF, radioGroup.id)
                radioEditText.hint = parentView.context.getString(R.string.edit_text_hint)
                radioEditText.layoutParams = layoutParams
                radioEditText.doAfterTextChanged {
                    val radioButtonID = itemView.radioGroup.checkedRadioButtonId
                    if (radioButtonID != -1) {
                        Log.i("aaaa", "radioEditText.doAfterTextChanged $it.toString() $radioButtonID")


                        val radioButton: RadioButton = radioGroup.findViewById(radioButtonID)
                        if (radioButton.isChecked &&
                            radioButton.text.toString().contains("Other")
                        )

                        postAnswer(it.toString())
                    }
                }
                parentView.addView(radioEditText)
            }
        }
    }
}