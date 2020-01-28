package com.yangbob.tiltsensor

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.hardware.SensorEvent
import android.view.View


class TiltView(context: Context?) : View(context) {
    private val greenPaint : Paint = Paint()
    private val blackPaint : Paint = Paint()

    private var cX : Float = 0f
    private var cY : Float = 0f
    private var xCoord : Float = 0f
    private var yCoord : Float = 0f

    init {
        greenPaint.color = Color.GREEN  // 녹색 페인트
        blackPaint.style = Paint.Style.STROKE   // 검은색 테두리 페인트
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        cY = h / 2F
        cX = w / 2f
    }

    override fun onDraw(canvas: Canvas?) {
        // 동일한 크기의 원 2개 생성
        canvas?.drawCircle(cX,cY,100F,blackPaint)
        canvas?.drawCircle(xCoord + cX,yCoord + cY,100F,greenPaint)

        // 중앙에 길이가 40인 직선 두개를 교차하여 십자선
        canvas?.drawLine(cX, cY -20F, cX, cY +20F, blackPaint)
        canvas?.drawLine(cX -20F, cY, cX + 20F , cY, blackPaint)
    }

    fun sensorEvent(event: SensorEvent?)
    {
        // 원본 값의 크기가 작아서, 이동하는게 보이지 않을 수 있으니 *20
        // 가로로 회전했으니까, x와 y 좌표 값을 반대로 적용
        yCoord = event!!.values[0] * 20
        xCoord = event!!.values[1] * 20
        invalidate()    // onDraw()메서드를 다시 호출하는. 즉 다시 뷰를 그리게 되는
    }
}
