package com.tct.mine

class MineMap{
    private var mCellArray:Array<Array<Cell>>
    private var mRowSize:Int = 0
    private var mColSize:Int = 0
    init {

    }

    constructor(row:Int, col:Int){
        mCellArray = Array(row, {Array(col,{Cell()})})
        mRowSize = row
        mColSize = col
    }

    fun getCellValue(row: Int, col: Int):Int{
        return mCellArray[row][col].cellValue
    }

    fun getVisibility(row: Int, col: Int):Boolean{
        return mCellArray[row][col].isVisiable
    }

    fun markCell(row: Int, col: Int){
        mCellArray[row][col].isMarked = !mCellArray[row][col].isMarked
    }

    fun getMarked(row: Int, col: Int):Boolean{
        if (row < 0 || col < 0 || row >= mRowSize || col >= mColSize)
        {
            return false
        }
        return mCellArray[row][col].isMarked
    }

    private fun canDisplay(row: Int, col: Int):Boolean{
        if (mCellArray[row][col].isMarked)
        {
            return false
        }

        if (getMarkCount(row, col) == mCellArray[row][col].cellValue || mCellArray[row][col].cellValue == 0)
        {
            return true
        }
        return false
    }

    private fun getMarkCount(row: Int, col: Int):Int{
        var count = 0
        for (i in -1..1){
            for (j in -1..1){
                if (getMarked(row + i, col + j))
                {
                    count = count + 1
                }
            }
        }
        return count
    }

    fun randomMap(mineCount:Int)
    {
        var row:Int
        var col:Int
        var remainCount = mineCount
        resetMap()
        while (remainCount > 0) {
            row = (Math.random() * (mRowSize - 1)).toInt()
            col = (Math.random() * (mColSize - 1)).toInt()

            if (mCellArray[row][col].cellValue == 0)
            {
                mCellArray[row][col].cellValue = -1;
                remainCount = remainCount - 1
            }
        }
        completeMap()
    }

    fun displayAllMap()
    {
        for (row in 0..mRowSize - 1){
            for (col in 0..mColSize - 1){
                mCellArray[row][col].isVisiable = true
            }
        }
    }

    fun displayMap(row: Int, col: Int, flag:Int)
    {
        if (row < 0 || col < 0 || row >= mRowSize || col >= mColSize)
        {
            return
        }

        if (mCellArray[row][col].isMarked)
        {
            return
        }

        if (mCellArray[row][col].isVisiable && flag == 0)
        {
            return
        }

        mCellArray[row][col].isVisiable = true

        if (canDisplay(row, col))
        {
            displayMap(row - 1, col - 1, 0)
            displayMap(row - 1, col, 0)
            displayMap(row - 1, col + 1, 0)
            displayMap(row , col - 1, 0)
            displayMap(row, col + 1, 0)
            displayMap(row + 1, col - 1, 0)
            displayMap(row + 1, col, 0)
            displayMap(row + 1, col + 1, 0)
        }
    }

    fun resetMap(){
        for (row in 0..mRowSize - 1){
            for (col in 0..mColSize - 1){
                mCellArray[row][col].cellValue = 0
                mCellArray[row][col].isVisiable = false
                mCellArray[row][col].isMarked = false
            }
        }
    }

    fun completeMap(){
        for (row in 0..mRowSize - 1){
            for (col in 0..mColSize - 1){
                if (mCellArray[row][col].cellValue == -1)
                {
                    continue
                }

                var temp = 0
                if (row > 0){
                    if (col > 0 && mCellArray[row - 1][col - 1].cellValue == -1){
                        temp = temp + 1
                    }
                    if (mCellArray[row - 1][col].cellValue == -1){
                        temp = temp + 1
                    }
                    if ((col < mColSize - 1) && mCellArray[row - 1][col + 1].cellValue == -1){
                        temp = temp + 1
                    }
                }

                if (col > 0 && mCellArray[row][col - 1].cellValue == -1){
                    temp = temp + 1
                }
                if ((col < mColSize - 1) && mCellArray[row][col + 1].cellValue == -1){
                    temp = temp + 1
                }

                if (row < mRowSize - 1){
                    if (col > 0 && mCellArray[row + 1][col - 1].cellValue == -1){
                        temp = temp + 1
                    }
                    if (mCellArray[row + 1][col].cellValue == -1){
                        temp = temp + 1
                    }
                    if ((col < mColSize - 1) && mCellArray[row + 1][col + 1].cellValue == -1){
                        temp = temp + 1
                    }
                }

                mCellArray[row][col].cellValue = temp
            }
        }
    }

    fun isWin():Boolean
    {
        for (row in 0..mRowSize - 1){
            for (col in 0..mColSize - 1){
                if (mCellArray[row][col].cellValue >= 0 && mCellArray[row][col].isVisiable == false)
                {
                    return false
                }
            }
        }

        return true
    }

    fun isLose():Boolean
    {
        for (row in 0..mRowSize - 1){
            for (col in 0..mColSize - 1){
                if (mCellArray[row][col].cellValue == -1 && mCellArray[row][col].isVisiable == true)
                {
                    return true
                }
            }
        }

        return false
    }
}