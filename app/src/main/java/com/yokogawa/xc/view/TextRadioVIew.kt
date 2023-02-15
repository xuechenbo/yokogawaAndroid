package com.yokogawa.xc.view

import android.content.Context
import android.util.AttributeSet
import android.util.Log
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
import com.yokogawa.xc.utils.Utils

class TextRadioVIew : FlexboxLayout {
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
                    val drawable = context.resources.getDrawable(R.drawable.check_style_radio)
                    checkbox.background = drawable
                    checkbox.width = 30
                    checkbox.height = 30
                    if (!boolean) {
                        checkbox.isEnabled = false
                    }
                    addView(checkbox)
                }
            }
            if (index < split.size - 1) {
                val editText = AppCompatEditText(context)
                editText.minWidth = DensityUtils.dp2px(context.applicationContext, 60.0F)
                editText.textSize = 14f
                if (!boolean) {
                    editText.visibility = INVISIBLE
                }
                editText.setTextColor(
                        ContextCompat.getColor(
                                context.applicationContext,
                                R.color.black
                        )
                )
                addView(editText)
            }
        }
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

}