package joe.gallerydemo.widgets

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.util.AttributeSet
import androidx.annotation.ColorInt
import androidx.appcompat.widget.AppCompatTextView
import joe.gallerydemo.R


class ColorDashTextView : AppCompatTextView {

    //stroke
    private var mStrokeWidth = 0
    private var mStrokeColor = 0
    private var mStrokeDashWidth = 0f
    private var mStrokeDashGap = 0f
    //radius
    private var mRadius = 0f

    private var mBackgroundColor = 0

    private var mBackground: GradientDrawable? = null


     constructor(context: Context, attributeSet: AttributeSet): super(context, attributeSet){
         setUp(attributeSet)
    }

    constructor(context: Context, attributeSet: AttributeSet, defStyleAttr: Int): super(context, attributeSet, defStyleAttr){
        setUp(attributeSet)
    }

    init {
    }

    private fun setUp(attrs: AttributeSet) {
        mBackground = GradientDrawable()
        val a = context.obtainStyledAttributes(attrs, R.styleable.ColorDashTextView)
        mStrokeDashWidth = a.getDimensionPixelSize(R.styleable.ColorDashTextView_cdt_strokeDashWidth, 0)*1f
        mStrokeDashGap = a.getDimensionPixelSize(R.styleable.ColorDashTextView_cdt_strokeDashGap, 0)*1f
        mStrokeWidth = a.getDimensionPixelSize(R.styleable.ColorDashTextView_cdt_strokeWidth, 0)
        mStrokeColor = a.getColor(R.styleable.ColorDashTextView_cdt_strokeColor, Color.TRANSPARENT)
        mRadius = a.getDimensionPixelSize(R.styleable.ColorDashTextView_cdt_radius, 0)*1f
        mBackgroundColor = a.getColor(R.styleable.ColorDashTextView_cdt_backgroundColor, Color.TRANSPARENT)
        setAll()
        a.recycle()
    }

    private fun setAll() {
        mBackground?.setStroke(mStrokeWidth, mStrokeColor, mStrokeDashWidth, mStrokeDashGap)
        mBackground?.cornerRadius = mRadius
        mBackground?.setColor(mBackgroundColor)
        background = mBackground
    }


    fun setDashStrokeColor(@ColorInt strokeColor: Int) {
        mBackground?.setStroke(mStrokeWidth, strokeColor, mStrokeDashWidth, mStrokeDashGap)
        background = mBackground
    }

    fun setStrokeAndBgColor(@ColorInt strokeColor: Int, @ColorInt bgColor: Int) {
        mBackground?.setStroke(mStrokeWidth, strokeColor, mStrokeDashWidth, mStrokeDashGap)
        mBackground?.setColor(bgColor)
        background = mBackground
    }

    fun setBgColor(@ColorInt bgColor: Int) {
        mBackground?.setColor(bgColor)
        background = mBackground
    }

    private fun setCorners() {
        mBackground?.setCornerRadius(mRadius)
        background = mBackground
    }


    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        mRadius = h / 2f
        updateShape()
    }

    private fun updateShape() {
        setCorners()
    }
}