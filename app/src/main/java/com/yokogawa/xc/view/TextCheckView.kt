package com.yokogawa.xc.view

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.view.View.OnFocusChangeListener
import android.widget.CheckBox
import androidx.appcompat.widget.AppCompatCheckBox
import androidx.appcompat.widget.AppCompatEditText
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.content.ContextCompat
import com.google.android.flexbox.AlignItems
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexWrap
import com.google.android.flexbox.FlexboxLayout
import com.yokogawa.xc.R
import com.yokogawa.xc.utils.DensityUtils

//填空+多选
class TextCheckView : FlexboxLayout {
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


    /**
     * data:检查条目
     * boolean：是否可编辑
     * rule：显示规则
     */
    fun setData(data: String, boolean: Boolean, rule: String?) {
        removeAllViews()
        val split = data.split("<fill>")
        split.forEachIndexed { index, s ->
            val split1 = s.split("<check>")
            split1.forEachIndexed { index1, content ->
                val textView = AppCompatTextView(context)
                textView.setTextColor(
                        ContextCompat.getColor(
                                context.applicationContext,
                                R.color.black
                        )
                )
                textView.textSize = 14f //textview的文字大小
                textView.text = content //设置textview的文字
                textView.setPadding(0, 5, 0, 5)
                addView(textView)
                if (index1 < split1.size - 1) {
                    val checkbox = AppCompatCheckBox(context)
                    checkbox.buttonDrawable = null
                    val drawable = context.resources.getDrawable(R.drawable.check_style_check)
                    checkbox.background = drawable
                    checkbox.width = 30
                    checkbox.height = 30
                    if (!boolean) {
                        checkbox.isEnabled = false
                    }
                    //TODO 回调所有CheckBox的CheckedChangeListener
                    checkbox.setOnCheckedChangeListener { _, isCheck ->
                        onCheckListListener?.OnMultCheckChange(checkbox, isCheck)
                    }
                    addView(checkbox)
                }
            }
            if (index < split.size - 1) {
                val editText = AppCompatEditText(context)
                editText.minWidth = DensityUtils.dp2px(context.applicationContext, 60.0F)
                editText.textSize = 14f
                if (!boolean) {
                    editText.isEnabled = false
                }
                editText.setTextColor(
                        ContextCompat.getColor(
                                context.applicationContext,
                                R.color.black
                        )
                )
                editText.onFocusChangeListener = OnFocusChangeListener { view: View?, hasFocus: Boolean ->
                    if (hasFocus) {
                        editText.addTextChangedListener(object : TextWatcher {
                            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                            }
                            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                            }
                            override fun afterTextChanged(p0: Editable?) {
                                onEditChangeListener?.onMultEdittextChange(editText, p0.toString())
                            }
                        })
                    } else {
                        editText.removeTextChangedListener(textWatcher)
                    }
                }
                addView(editText)
            }
        }

        //按<>顺序生成tag,唯一标识
        var mPostion = 0
        for (i in 0 until childCount) {
            val childAt = getChildAt(i)
            if (childAt is AppCompatEditText) {
                childAt.setTag(mPostion)
                mPostion++
            } else if (childAt is AppCompatCheckBox) {
                childAt.setTag(mPostion)
                mPostion++
            }
        }
    }

    //获取用户输入的文字列表
    fun getResult(msgAnswer: String?, msg: String): MutableList<String> {
        val result = mutableListOf<String>()
        if (msgAnswer == null) {
            for (i in 0 until childCount) {
                val childAt = getChildAt(i)
                if (childAt is AppCompatEditText) {
                    result.add("")
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


    fun setAnswer(result: List<String>) {
        var mPostion = 0
        for (i in 0 until childCount) {
            Log.e("TAG", "i===$i")
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


    /*================================checkBox回调==================================================*/

    private var onCheckListListener: OnCheckListListener? = null

    interface OnCheckListListener {
        fun OnMultCheckChange(checkBox: CheckBox?, isCheck: Boolean)
    }

    fun setOnCheckListListener(onCheckListListener: OnCheckListListener?) {
        this.onCheckListListener = onCheckListListener
    }

    /*==================================Edittext  文本修改  回调=====================================*/

    private var onEditChangeListener: OnEditChangeListener? = null

    interface OnEditChangeListener {
        fun onMultEdittextChange(editText: AppCompatEditText, string: String)
    }

    fun setonEditChnageListener(onEditChangeListener: OnEditChangeListener) {
        this.onEditChangeListener = onEditChangeListener
    }

    /*=================================Edittext 获取焦点   回调======================================*/


    /*==============================================================================================*/


    var textWatcher: TextWatcher = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
        override fun afterTextChanged(s: Editable) {}
    }

}