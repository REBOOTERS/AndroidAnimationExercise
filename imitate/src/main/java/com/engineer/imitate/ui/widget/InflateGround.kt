package com.engineer.imitate.ui.widget

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import com.engineer.imitate.R

/**
 * Created on 2020/8/20.
 * @author rookie
 */

class ViewA : ConstraintLayout {
    // <editor-fold defaultstate="collapsed" desc="构造函数">
    constructor(context: Context) : super(context) {
        initView(context)
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        initView(context)
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context, attrs, defStyleAttr
    ) {
        initView(context)
    }

    // </editor-fold>

    private fun initView(context: Context?) {
        val view = LayoutInflater.from(context).inflate(R.layout.inflate_example, this, true)
        val text_cover: TextView = view.findViewById(R.id.text_cover)
        text_cover.text = this.javaClass.canonicalName
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        Log.d("ViewA", "onDetachedFromWindow() called")
    }
}


class ViewB : ConstraintLayout {
    // <editor-fold defaultstate="collapsed" desc="构造函数">
    constructor(context: Context) : super(context) {
        initView(context)
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        initView(context)
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context, attrs, defStyleAttr
    ) {
        initView(context)
    }

    // </editor-fold>

    private fun initView(context: Context?) {
        val view = LayoutInflater.from(context).inflate(R.layout.inflate_example, this, false)
        val text_cover: TextView = view.findViewById(R.id.text_cover)
        text_cover.text = this.javaClass.canonicalName
        addView(view)
    }
}

class ViewC : ConstraintLayout {
    // <editor-fold defaultstate="collapsed" desc="构造函数">
    constructor(context: Context) : super(context) {
        initView(context)
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        initView(context)
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context, attrs, defStyleAttr
    ) {
        initView(context)
    }

    // </editor-fold>

    private fun initView(context: Context?) {
        val view = LayoutInflater.from(context).inflate(R.layout.inflate_example, null, false)
        val text_cover: TextView = view.findViewById(R.id.text_cover)
        text_cover.text = this.javaClass.canonicalName
        addView(view)
    }
}

class ViewD : ConstraintLayout {
    // <editor-fold defaultstate="collapsed" desc="构造函数">
    constructor(context: Context) : super(context) {
        initView(context)
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        initView(context)
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context, attrs, defStyleAttr
    ) {
        initView(context)
    }

    // </editor-fold>

    private fun initView(context: Context?) {
        val view = LayoutInflater.from(context).inflate(R.layout.inflate_example, null, true)
        val text_cover: TextView = view.findViewById(R.id.text_cover)
        text_cover.text = this.javaClass.canonicalName
        addView(view)
    }
}

class ViewE : ConstraintLayout {
    // <editor-fold defaultstate="collapsed" desc="构造函数">
    constructor(context: Context) : super(context) {
        initView(context)
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        initView(context)
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context, attrs, defStyleAttr
    ) {
        initView(context)
    }

    // </editor-fold>

    private fun initView(context: Context?) {
        val view = LayoutInflater.from(context).inflate(R.layout.inflate_example, null)
        // return inflate(resource, root, root != null);
        // 等价于  LayoutInflater.from(context).inflate(R.layout.inflate_example, null,false)
        val text_cover: TextView = view.findViewById(R.id.text_cover)
        text_cover.text = this.javaClass.canonicalName
        addView(view)
    }
}

class ViewF : ConstraintLayout {
    // <editor-fold defaultstate="collapsed" desc="构造函数">
    constructor(context: Context) : super(context) {
        initView(context)
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        initView(context)
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context, attrs, defStyleAttr
    ) {
        initView(context)
    }

    // </editor-fold>

    private fun initView(context: Context?) {
        val view = LayoutInflater.from(context).inflate(R.layout.inflate_example, this)
        // return inflate(resource, root, root != null);
        // 等价于  LayoutInflater.from(context).inflate(R.layout.inflate_example, this,true)
//        addView(view) 在 addView 会报错
        val text_cover: TextView = view.findViewById(R.id.text_cover)
        text_cover.text = this.javaClass.canonicalName
    }
}

class ViewG : ConstraintLayout {
    // <editor-fold defaultstate="collapsed" desc="构造函数">
    constructor(context: Context) : super(context) {
        initView(context)
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        initView(context)
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context, attrs, defStyleAttr
    ) {
        initView(context)
    }

    // </editor-fold>

    override fun onFinishInflate() {
        super.onFinishInflate()
        val text_cover_best: TextView = findViewById(R.id.text_cover)
        text_cover_best.text = this.javaClass.canonicalName
    }

    private fun initView(context: Context?) {
//        val view = LayoutInflater.from(context).inflate(R.layout.inflate_example, this)
        // return inflate(resource, root, root != null);
        // 等价于  LayoutInflater.from(context).inflate(R.layout.inflate_example, this,true)
//        addView(view) 在 addView 会报错
    }
}