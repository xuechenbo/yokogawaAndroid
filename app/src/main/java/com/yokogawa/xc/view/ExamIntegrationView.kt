package com.yokogawa.xc.view

import android.content.Context
import android.graphics.Color
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.CheckBox
import android.widget.RadioGroup
import androidx.appcompat.widget.*
import androidx.core.content.ContextCompat
import com.google.android.flexbox.AlignItems
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexWrap
import com.google.android.flexbox.FlexboxLayout
import com.yokogawa.xc.R
import com.yokogawa.xc.utils.DensityUtils

class ExamIntegrationView : FlexboxLayout {
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

    //判断类型
    fun setCheckType(checkType: String) {
        val split = checkType.split(",")
    }

    fun setData(data: String, boolean: Boolean) {
        removeAllViews()
        val textsplit = data.split("<text>")
        textsplit.forEachIndexed { index, s ->
            val checksplit = s.split("<check>")
            checksplit.forEachIndexed { index1, content ->
                if (content.indexOf("<image>") != -1) {
                    //接收url
                    imageGroutAdd(content, boolean)
                } else {
                    addView(TextAdd(content))
                }
                if (index1 < checksplit.size - 1) {
                    addView(CheckAdd(boolean))
                }
            }
            if (index < textsplit.size - 1) {
                addView(EditAdd(boolean))
            }
        }
        setTag()
    }

    fun setTag() {
        var mPostion = 0
        var mCheckBox = 0
        for (i in 0 until childCount) {
            when (val childAt = getChildAt(i)) {
                is AppCompatEditText -> {
                    childAt.setTag(mPostion)
                    mPostion++
                }
                is AppCompatCheckBox -> {
                    childAt.setTag(mPostion)
                    childAt.id = mCheckBox
                    mCheckBox++
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

    //
    fun TextAdd(content: String): AppCompatTextView {
        val textView = AppCompatTextView(context)
        textView.setTextColor(Color.BLACK)
        textView.textSize = 16f //textview的文字大小
        textView.text = content.trim() //设置textview的文字
        textView.setPadding(0, 5, 0, 5)
        return textView
    }

    fun EditAdd(boolean: Boolean): AppCompatEditText {
        val editText = AppCompatEditText(context)
        editText.minWidth = DensityUtils.dp2px(context.applicationContext, 100.0F)
        editText.textSize = 16f
        val drawable1 = context.resources.getDrawable(R.drawable.shape_layerlist_bottomline)
        editText.background = drawable1
        editText.setPadding(5, 5, 40, 5)
        editText.setTextColor(Color.BLUE)

        if (!boolean) {
            editText.isEnabled = false
            editText.setPadding(5, 0, 5, 0)
            editText.height = 50
            val drawable = context.resources.getDrawable(R.drawable.edit_style_background)
            editText.background = drawable
        }

        if (true) {
            val drawable = context.resources.getDrawable(R.drawable.icon_down)
            val intrinsicWidth = drawable.intrinsicWidth
            drawable.setBounds(0, 0, drawable.minimumWidth, drawable.minimumHeight);
            editText.setCompoundDrawables(drawable, null, null, null)
            editText.setOnTouchListener(OnTouchListener { v, event ->
                if (event.action != MotionEvent.ACTION_UP) {
                    return@OnTouchListener false
                }
                if (event.x < editText.paddingLeft + intrinsicWidth + 15) {
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
        }

        editText.setTextColor(Color.BLUE)
        editText.onFocusChangeListener = OnFocusChangeListener { view: View?, hasFocus: Boolean ->
            if (hasFocus) {
                editText.setTextIsSelectable(true);
                showKeyBoard(context, view)
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
                onExamItemChangeListener?.onNoHasFocus(editText, boolean)
                editText.removeTextChangedListener(textWatcher)
            }
        }
        return editText
    }

    fun showKeyBoard(context: Context?, view: View?) {
        if (context == null) return
        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.showSoftInput(view, InputMethodManager.SHOW_FORCED)
    }

    fun CheckAdd(boolean: Boolean): AppCompatCheckBox {
        val checkbox = AppCompatCheckBox(context)
        checkbox.buttonDrawable = null
        val drawable = context.resources.getDrawable(R.drawable.check_style_check)
        checkbox.background = drawable
        checkbox.width = 55
        checkbox.height = 55
        checkbox.text = "  "
        if (!boolean) {
            checkbox.isEnabled = false
        }
        checkbox.setOnCheckedChangeListener { _, isCheck ->
            onExamItemChangeListener?.OnMultCheckChange(checkbox, isCheck)
        }
        return checkbox
    }

    //iamge
    fun imageGroutAdd(content: String, boolean: Boolean) {
        val imagesplit = content.split("<image>")
        imagesplit.forEachIndexed { index2, sss ->
            val textView = AppCompatTextView(context)
            textView.setTextColor(
                ContextCompat.getColor(
                    context.applicationContext,
                    R.color.black
                )
            )
            textView.textSize = 16f //textview的文字大小
            textView.text = sss //设置textview的文字
            textView.setPadding(0, 5, 0, 5)
            addView(textView)
            if (index2 < imagesplit.size - 1) {
                val imageView = AppCompatImageView(context)
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
            }
        }
    }

    /*=================================全部回调======================================*/
    private var onExamItemChangeListener: OnExamItemChangeListener? = null

    interface OnExamItemChangeListener {
        //Edittext
        fun onMultEdittextChange(editText: AppCompatEditText, string: String)

        //checkBox
        fun OnMultCheckChange(checkBox: CheckBox?, isCheck: Boolean)


        //edittext
        fun OnButtonClick1(editText: AppCompatEditText?)

        fun onNoHasFocus(editText: AppCompatEditText?, boolean: Boolean)
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