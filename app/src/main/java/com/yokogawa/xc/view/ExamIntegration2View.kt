package com.yokogawa.xc.view

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.View.OnTouchListener
import android.widget.*
import androidx.appcompat.widget.*
import androidx.core.content.ContextCompat
import androidx.core.view.forEachIndexed
import com.bumptech.glide.Glide
import com.google.android.flexbox.AlignItems
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexWrap
import com.google.android.flexbox.FlexboxLayout
import com.yokogawa.xc.R
import com.yokogawa.xc.utils.DensityUtils

//填空+单选+多选
class ExamIntegration2View : FlexboxLayout {
    constructor(context: Context) : super(context) {
        initView()
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        initView()
    }

    constructor(context: Context, attrs: AttributeSet?, defStyle: Int) : super(
        context,
        attrs,
        defStyle
    ) {
        initView()
    }

    private fun initView() {
        flexDirection = FlexDirection.ROW
        flexWrap = FlexWrap.WRAP
        alignItems = AlignItems.CENTER
    }

    fun setData(data: String, boolean: Boolean) {
        removeAllViews()
        val Textsplit = data.split("<text>")
        Textsplit.forEachIndexed { index, s ->
            val Checksplit = s.split("<check>")
            Checksplit.forEachIndexed { index1, content ->
                if (content.indexOf("<image>") != -1) {
                    RadioGroutAdd(content, boolean)
                } else {
                    addView(TextAdd(content))
                }
                if (index1 < Checksplit.size - 1) {
                    addView(CheckAdd(boolean))
                }
            }
            if (index < Textsplit.size - 1) {
                addView(EditAdd(boolean))
            }
        }
        setTag()
    }

    fun setTag() {
        var mPostion = 0
        for (i in 0 until childCount) {
            when (val childAt = getChildAt(i)) {
                is AppCompatEditText -> {
                    childAt.setTag(mPostion)
                    mPostion++
                }
                is AppCompatCheckBox -> {
                    childAt.setTag(mPostion)
                    mPostion++
                }
                is RadioGroup -> {
                    childAt.setTag(mPostion)
                    mPostion++
                }
            }
        }
    }

    //获取用户输入的文字列表
    fun getResult(msgAnswer: String?): MutableList<String> {
        val result = mutableListOf<String>()
        if (msgAnswer == null) {
            for (i in 0 until childCount) {
                if (childCount == 1) {
                    result.add("null")
                }
                val childAt = getChildAt(i)
                if (childAt is AppCompatEditText) {
                    result.add(" ")
                } else if (childAt is AppCompatCheckBox) {
                    result.add("false")
                }
            }
        } else {
            val split = msgAnswer.split(",")
            for (i in split) {
                result.add(i)
            }
        }
        return result
    }

    fun TextAdd(content: String): AppCompatTextView {
        val textView = AppCompatTextView(context)
        textView.setTextColor(
            ContextCompat.getColor(context.applicationContext, R.color.black)
        )
        textView.textSize = 20f //textview的文字大小
        textView.text = content.trim() //设置textview的文字
        textView.setPadding(0, 5, 0, 5)
        return textView
    }

    @SuppressLint("ClickableViewAccessibility")
    fun EditAdd(boolean: Boolean): AppCompatEditText {
        val editText = AppCompatEditText(context)
        editText.minWidth = DensityUtils.dp2px(context.applicationContext, 100.0F)
        editText.textSize = 20f
        val drawable1 = context.resources.getDrawable(R.drawable.shape_layerlist_bottomline)
        editText.background = drawable1
        editText.setPadding(5, 5, 10, 5)
        val drawable = context.resources.getDrawable(R.drawable.icon_down)
        val intrinsicWidth = drawable.intrinsicWidth
        drawable.setBounds(0, 0, drawable.minimumWidth, drawable.minimumHeight);
        editText.setCompoundDrawables(null, null, drawable, null)
        editText.setTextColor(Color.RED)
        editText.setOnTouchListener(OnTouchListener { _, event ->
            if (event.action != MotionEvent.ACTION_UP) {
                return@OnTouchListener false
            }
            if (event.x > editText.width - editText.paddingRight - intrinsicWidth - 15) {
                editText.isFocusable = false
                editText.clearFocus()
                onExamItemChangeListener?.OnButtonClick1(editText)
            } else {
                editText.isFocusable = true;//设置输入框可聚集
                editText.isFocusableInTouchMode = true;//设置触摸聚焦
                editText.requestFocus();//请求焦点
                editText.findFocus();//获取焦点
            }
            return@OnTouchListener false
        })

        editText.onFocusChangeListener = OnFocusChangeListener { view: View?, hasFocus: Boolean ->
            if (hasFocus) {
                editText.addTextChangedListener(object : TextWatcher {
                    override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    }
                    override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    }
                    override fun afterTextChanged(p0: Editable?) {
                        onExamItemChangeListener?.onMultEdittextChange(editText, p0.toString())
                    }
                })
            } else {
                editText.removeTextChangedListener(textWatcher)
            }
        }
        return editText
    }

    fun CheckAdd(boolean: Boolean): AppCompatCheckBox {
        val checkbox = AppCompatCheckBox(context)
        checkbox.width = 30
        checkbox.height = 30
        if (!boolean) {
            checkbox.isEnabled = false
        }
        checkbox.setOnCheckedChangeListener { _, isCheck ->
            onExamItemChangeListener?.OnMultCheckChange(checkbox, isCheck)
        }
        return checkbox
    }

    //iamge
    fun RadioGroutAdd(content: String, boolean: Boolean) {
        val Radiosplit = content.split("<image>")
        Radiosplit.forEachIndexed { index2, sss ->
            val textView = AppCompatTextView(context)
            textView.setTextColor(
                ContextCompat.getColor(
                    context.applicationContext,
                    R.color.black
                )
            )
            textView.textSize = 14f //textview的文字大小
            textView.text = sss //设置textview的文字
            textView.setPadding(0, 5, 0, 5)
            addView(textView)
            if (index2 < Radiosplit.size - 1) {
                val imageView = AppCompatImageView(context)
                imageView.tag = index2
                imageView.maxHeight = 35
                imageView.maxWidth = 35
                imageView.setImageDrawable(
                    context.resources.getDrawable(R.drawable.tt)
                )
//                Glide.with(context).load("www.baidu.com").into(imageView)
                addView(imageView)
            }
        }
    }

    //获取前一个view
    fun getBeforeView(postion: Int) {

    }

    //获取后一个View
    fun getAfterView(postion: Int) {

    }

    //数据回显
    fun setAnswer(result: List<String>) {
//        Log.e("TAG", "答案回显$result")
        var mPostion = 0
        for (i in 0 until childCount) {
            val childAt = getChildAt(i)
            if (childAt is AppCompatEditText) {
                childAt.setText(result[mPostion])
                mPostion++
            } else if (childAt is AppCompatCheckBox) {
                childAt.isChecked = result[mPostion] != "false"
                mPostion++
            } else if (childAt is RadioGroup) {
                val radioGroup = getChildAt(i) as RadioGroup
                radioGroup.forEachIndexed { _, view ->
                    view as RadioButton
                    if (result[mPostion].trim() == view.text.trim()) {
                        Log.e("TAG", "viewTrue" + view.getTag())
                        view.isChecked = true
                    }
                }
                mPostion++
            }
        }
    }

    /*=================================全部回调======================================*/
    private var onExamItemChangeListener: OnExamItemChangeListener? = null

    interface OnExamItemChangeListener {
        //RadioButton
        fun onMultRadioGroupChange(
            radioGroup: RadioGroup,
            checkId: Int,
            radioButton: AppCompatRadioButton
        )

        //Edittext
        fun onMultEdittextChange(editText: AppCompatEditText, string: String)

        //checkBox
        fun OnMultCheckChange(checkBox: CheckBox?, isCheck: Boolean)

        //button
        fun OnButtonClick(checkBox: Button?)

        //edittext
        fun OnButtonClick1(editText: AppCompatEditText?)
    }

    fun setOnExChangeListener(onExamItemChangeListener: OnExamItemChangeListener) {
        this.onExamItemChangeListener = onExamItemChangeListener
    }
    /*==============================================================================================*/


    var textWatcher: TextWatcher = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
        override fun afterTextChanged(s: Editable) {}
    }


}