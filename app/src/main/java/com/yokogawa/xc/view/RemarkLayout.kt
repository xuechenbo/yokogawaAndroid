package com.yokogawa.xc.view

import android.content.Context
import android.graphics.Color
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.view.View.OnFocusChangeListener
import android.view.inputmethod.InputMethodManager
import android.widget.CheckBox
import android.widget.RadioGroup
import androidx.appcompat.widget.AppCompatCheckBox
import androidx.appcompat.widget.AppCompatEditText
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.content.ContextCompat
import com.google.android.flexbox.AlignItems
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexWrap
import com.google.android.flexbox.FlexboxLayout
import com.yokogawa.xc.R
import com.yokogawa.xc.utils.DensityUtils
import com.yokogawa.xc.utils.ExamUtils
import com.yokogawa.xc.utils.GlideUtils


//TODO 填空+选择
class RemarkLayout : FlexboxLayout {
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

    fun setData(
        remark: String,
        imageUrl: List<String>
    ) {
        removeAllViews()
        var imagePostion = 0;
        val imageSplit = remark.split("▲")
        imageSplit.forEachIndexed { index2, conStr ->
            val textView = AppCompatTextView(context)
            textView.setTextColor(
                ContextCompat.getColor(
                    context.applicationContext,
                    R.color.black
                )
            )
            textView.textSize = 14f
            textView.text = conStr
            textView.setPadding(0, 5, 0, 5)
            addView(textView)
            if (index2 < imageSplit.size - 1) {
                val imageView = AppCompatImageView(context)
                imageView.maxHeight = 35
                imageView.maxWidth = 35
                imageView.tag = imagePostion
                imageView.setOnClickListener {
                    onExamItemChangeListener?.imageClickListener(
                        imageView,
                        imagePostion
                    )
                }
                GlideUtils.loadImageSize(context, imageUrl[imagePostion], imageView)
                imagePostion++
                addView(imageView)
            }
        }
    }

    /*=================================全部回调======================================*/
    private var onExamItemChangeListener: OnExamItemChangeListener? = null

    interface OnExamItemChangeListener {
        fun imageClickListener(imageView: AppCompatImageView?, tag: Int)
    }

    fun setOnExChangeListener(onExamItemChangeListener: OnExamItemChangeListener) {
        this.onExamItemChangeListener = onExamItemChangeListener
    }
}