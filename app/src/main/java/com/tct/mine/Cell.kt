package com.tct.mine

class Cell{
    private var mValue:Int = 0
    private var mIsVisiable:Boolean = false
    private var mIsMarked:Boolean = false
    private var mRow:Int = -1
    private var mCol:Int = -1

    constructor()
    {
    }

    constructor(row:Int, col:Int)
    {
        mRow = row
        mCol = col
    }

    var row:Int
        get() = mRow
        set(value) {mRow = value}

    var col:Int
        get() = mCol
        set(value) {mCol = value}

    var cellValue:Int
        get() = mValue
        set(value) {mValue = value}

    var isVisiable:Boolean
        get() = mIsVisiable
        set(value) {mIsVisiable = value}

    var isMarked:Boolean
        get() = mIsMarked
        set(value) {mIsMarked = value}

    init {

    }
}