package com.tct.mine

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View
import java.sql.Time
import java.sql.Timestamp

class MineView
@JvmOverloads constructor(context:Context, attrs:AttributeSet?=null):View(context, attrs){
    private var TAG:String = "Mine"
    private val mLinePaint:Paint
    private val mCellPaint:Paint
    private val mVisiblePaint:Paint
    private var mCellNumber:Int = 15
    private var mCellWidth:Float = 0.toFloat()
    private var mMineMap:MineMap
    private var timestamp:Long = 0
    var lineColor: Int
        get() = mLinePaint.color
        set(color) {
            mLinePaint.color = color
        }

    init {
        mLinePaint = Paint()
        mCellPaint = Paint()
        mVisiblePaint = Paint()
        mMineMap = MineMap(mCellNumber, mCellNumber)
        mMineMap.randomMap(30)

        val typearray = context.obtainStyledAttributes(attrs, R.styleable.MineView)
        lineColor = typearray.getColor(R.styleable.MineView_lineColor, Color.BLACK)
        mVisiblePaint.color = typearray.getColor(R.styleable.MineView_visibleColor, Color.LTGRAY)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val widthMode = View.MeasureSpec.getMode(widthMeasureSpec)
        val widthSize = View.MeasureSpec.getSize(widthMeasureSpec)
        val heightMode = View.MeasureSpec.getMode(heightMeasureSpec)
        val heightSize = View.MeasureSpec.getSize(heightMeasureSpec)

        var width = -1
        var height = -1

        width = widthSize - paddingLeft - paddingRight
        height = width + 2


        Log.d(TAG, "widthSize=" + widthSize);
        Log.d(TAG, "heightSize=" + heightSize);

        mCellWidth = width / mCellNumber.toFloat()

        var cellTextSize = mCellWidth * 0.75f
        mCellPaint.textSize = cellTextSize

        setMeasuredDimension(width, height)

        //super.onMeasure(widthMeasureSpec, heightMeasureSpec)
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        for (row in 0..mCellNumber - 1) {
            for (col in 0..mCellNumber - 1) {

                if (mMineMap.getMarked(row, col)){
                    canvas!!.drawText("?", col * mCellWidth + mCellWidth / 4, (row + 1) * mCellWidth - mCellWidth / 4, mCellPaint)
                    continue
                }

                if (mMineMap.getVisibility(row, col) == false)
                {
                    continue
                }
                canvas!!.drawRect(col * mCellWidth, row * mCellWidth, (col + 1) * mCellWidth, (row + 1) * mCellWidth, mVisiblePaint)

                if (mMineMap.getCellValue(row, col) == -1)
                {
                    canvas!!.drawText("*", col * mCellWidth + mCellWidth / 4, (row + 1) * mCellWidth - mCellWidth / 4, mCellPaint)
                }
                else if (mMineMap.getCellValue(row, col) != 0){
                    canvas!!.drawText(Integer.toString(mMineMap.getCellValue(row, col)), col * mCellWidth + mCellWidth / 4, (row + 1) * mCellWidth - mCellWidth / 4, mCellPaint)
                }
            }
        }

        // draw vertical lines
        for (c in 0..mCellNumber) {
            val x = c * mCellWidth + paddingLeft
            canvas!!.drawLine(x, paddingTop.toFloat(), x, height.toFloat(), mLinePaint)
        }

        // draw horizontal lines
        for (r in 0..mCellNumber) {
            val y = r * mCellWidth + paddingTop
            canvas!!.drawLine(paddingLeft.toFloat(), y, width.toFloat(), y, mLinePaint)
        }
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        val x = event!!.getX().toInt()
        val y = event!!.getY().toInt()

        var cell:Cell? = getCellAtPoint(x, y)

        if (event.action == MotionEvent.ACTION_DOWN)
        {
            timestamp = System.currentTimeMillis()
        }

        if (event.action == MotionEvent.ACTION_UP && cell != null) {
            if (System.currentTimeMillis() - timestamp > 400)
            {
                if (!mMineMap.getVisibility(cell!!.row, cell!!.col)) {
                    mMineMap.markCell(cell!!.row, cell!!.col)
                }
            }
            else {
                if (mMineMap.getMarked(cell!!.row, cell!!.col))
                {
                    return false
                }

                mMineMap.displayMap(cell!!.row, cell!!.col, 1)
                if (mMineMap.isLose())
                {
                    mMineMap.displayAllMap()

                    val dialog = AlertDialog.Builder(this.context)
                            .setTitle("Game Over")//设置对话框的标题
                            .setPositiveButton("New Game", DialogInterface.OnClickListener { dialog, which -> mMineMap.randomMap(30);invalidate() }).create()
                    dialog.show()
                }
                else if (mMineMap.isWin()) {
                    val dialog = AlertDialog.Builder(this.context)
                            .setTitle("Congratulations")//设置对话框的标题
                            .setPositiveButton("New Game", DialogInterface.OnClickListener { dialog, which -> mMineMap.randomMap(30);invalidate() }).create()
                    dialog.show()
                }
            }
            invalidate()
        }

        return true
    }

    private fun getCellAtPoint(x: Int, y: Int): Cell? {
        // take into account padding
        val lx = x - paddingLeft
        val ly = y - paddingTop

        val row = (ly / mCellWidth).toInt()
        val col = (lx / mCellWidth).toInt()

        return if (col >= 0 && col < mCellNumber
                && row >= 0 && row < mCellNumber) {
            Cell(row, col)
        } else {
            null
        }
    }
}