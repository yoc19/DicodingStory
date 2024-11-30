package com.dicoding.picodiploma.loginwithanimation.custom

import android.content.Context
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.util.Patterns
import android.view.View
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.content.ContextCompat
import androidx.core.widget.addTextChangedListener
import com.dicoding.picodiploma.loginwithanimation.R


class EmailEditText @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : AppCompatEditText(context, attrs) {

    private var emailIcon: Drawable = ContextCompat.getDrawable(context, R.drawable.ic_email_24) as Drawable

    init {
        setButtonDrawables()

        addTextChangedListener(onTextChanged = { p0: CharSequence?, p1: Int, p2: Int, p3: Int ->
            val email = text?.trim()
            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                setError((context.getString(R.string.email_invalid)), null)
            }else{
                error = null
            }
        })
    }


    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        textAlignment = View.TEXT_ALIGNMENT_VIEW_START
    }


    private fun setButtonDrawables(
        startOfTheText: Drawable? = emailIcon,
        topOfTheText: Drawable? = null,
        endOfTheText: Drawable? = null,
        bottomOfTheText: Drawable? = null
    ) {
        setCompoundDrawablesWithIntrinsicBounds(
            startOfTheText, topOfTheText, endOfTheText, bottomOfTheText
        )
    }
}