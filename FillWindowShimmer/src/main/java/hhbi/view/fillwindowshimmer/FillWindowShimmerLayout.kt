package hhbi.view.fillwindowshimmer

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.annotation.LayoutRes
import com.facebook.shimmer.ShimmerFrameLayout
import android.view.ViewTreeObserver

class FillWindowShimmerLayout : ShimmerFrameLayout, ViewTreeObserver.OnGlobalLayoutListener {

    @LayoutRes
    private var layoutRes: Int = 0
    var disableFillWindow = false

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        initLayout(context, attrs)
    }
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) :this(context, null)

    private fun initLayout(context: Context, attrs: AttributeSet?) {
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.FillWindowShimmerLayout)

        layoutRes = try {
            typedArray.getResourceId(R.styleable.FillWindowShimmerLayout_layout, -1)
        } finally {
            typedArray.recycle()
        }

        viewTreeObserver.addOnGlobalLayoutListener(this)
    }

    override fun onGlobalLayout() {
        viewTreeObserver.removeOnGlobalLayoutListener(this)

        postDelayed({
            if (disableFillWindow) {
                val contentView = createContentView(this)
                contentView.setBackgroundColor(Color.WHITE)
                addView(contentView)
            } else {
                val linearLayout = LinearLayout(context)
                linearLayout.layoutParams = LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
                linearLayout.orientation = LinearLayout.VERTICAL
                linearLayout.setBackgroundColor(Color.WHITE)

                addView(linearLayout)

                var linearLayoutHeight = 0

                val deviceHeight = resources.displayMetrics.heightPixels

                while (linearLayoutHeight < deviceHeight) {
                    val contentView = createContentView(linearLayout)
                    contentView.measure(MeasureSpec.UNSPECIFIED, MeasureSpec.UNSPECIFIED)
                    val contentHeight = contentView.measuredHeight
                    linearLayout.addView(contentView)
                    linearLayoutHeight += contentHeight
                }
            }
        }, 100)
    }

    private fun createContentView(parent: ViewGroup): View {
        return LayoutInflater.from(context).inflate(layoutRes, parent, false)
    }

}