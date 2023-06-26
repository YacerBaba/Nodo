package owner.yacer.nodoproject.ui.CustomImageView

import android.content.Context
import android.graphics.Canvas
import android.graphics.Path
import android.graphics.RectF
import android.util.AttributeSet

class RoundedCornerImageView : androidx.appcompat.widget.AppCompatImageView {

    private val cornerRadius = 11f
    private val path = Path()
    private val rectF = RectF()

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    override fun onDraw(canvas: Canvas) {
        rectF.set(0f, 0f, width.toFloat(), height.toFloat())
        path.addRoundRect(rectF, cornerRadius, cornerRadius, Path.Direction.CW)
        canvas.clipPath(path)
        super.onDraw(canvas)
    }
}
