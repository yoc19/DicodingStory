package com.dicoding.picodiploma.loginwithanimation.custom

import android.content.Context
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.text.Editable
import android.text.TextWatcher
import android.text.method.PasswordTransformationMethod
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.content.ContextCompat
import com.dicoding.picodiploma.loginwithanimation.R

class PasswordEditText @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : AppCompatEditText(context, attrs), View.OnTouchListener {

    private var passwordIcon: Drawable = ContextCompat.getDrawable(context, R.drawable.ic_password_24) as Drawable
    private var passwordImage: Drawable
    private var isVisible: Boolean = false

    init {
        passwordImage = ContextCompat.getDrawable(context, R.drawable.ic_eye_open_24) as Drawable
        setButtonDrawables(endOfTheText = passwordImage)

        setOnTouchListener(this)

        addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }


            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                if (s.toString().length < 8) {
                    setError((context.getString(R.string.password_error)), null)
                }
                else{
                    error = null
                }
            }

            override fun afterTextChanged(s: Editable) {
                // Do nothing.
            }
        })
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        textAlignment = View.TEXT_ALIGNMENT_VIEW_START
    }


    private fun setButtonDrawables(
        startOfTheText: Drawable? = passwordIcon,
        topOfTheText: Drawable? = null,
        endOfTheText: Drawable? = null,
        bottomOfTheText: Drawable? = null
    ) {
        setCompoundDrawablesWithIntrinsicBounds(
            startOfTheText, topOfTheText, endOfTheText, bottomOfTheText
        )
    }

    override fun onTouch(v: View?, event: MotionEvent): Boolean {
        if (compoundDrawables[2] != null) {
            val visibleButtonStart: Float
            val visibleButtonEnd: Float
            var isVisibleButtonClicked = false
            if (layoutDirection == View.LAYOUT_DIRECTION_RTL) {
                visibleButtonEnd = (passwordImage.intrinsicWidth + paddingStart).toFloat()
                when {
                    event.x < visibleButtonEnd -> isVisibleButtonClicked = true
                }
            } else {
                visibleButtonStart = (width - paddingEnd - passwordImage.intrinsicWidth).toFloat()
                when {
                    event.x > visibleButtonStart -> isVisibleButtonClicked = true
                }
            }
            if (isVisibleButtonClicked) {
                when (event.action) {
                    MotionEvent.ACTION_DOWN -> {
                        passwordImage = ContextCompat.getDrawable(
                            context,
                            if (isVisible) R.drawable.ic_eye_close_24 else R.drawable.ic_eye_open_24
                        ) as Drawable
                        setButtonDrawables(endOfTheText = passwordImage)
                        transformationMethod =
                            if (isVisible) null else PasswordTransformationMethod.getInstance()
                        isVisible = !isVisible
                        return true
                    }

                    else -> return false
                }
            } else return false
        }
        return false
    }

}