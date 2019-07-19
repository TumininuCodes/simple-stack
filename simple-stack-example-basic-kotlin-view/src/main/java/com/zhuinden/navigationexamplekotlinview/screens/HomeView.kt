package com.zhuinden.navigationexamplekotlinview.screens

import android.annotation.TargetApi
import android.content.Context
import android.util.AttributeSet
import android.widget.RelativeLayout
import com.zhuinden.navigationexamplekotlinview.utils.backstack
import com.zhuinden.navigationexamplekotlinview.utils.onClick
import com.zhuinden.simplestack.Backstack
import kotlinx.android.synthetic.main.home_view.view.*

/**
 * Created by Owner on 2017. 06. 29..
 */

class HomeView : RelativeLayout {
    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr)
    @TargetApi(21)
    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int, defStyleRes: Int) : super(context, attrs, defStyleAttr, defStyleRes)

    override fun onFinishInflate() {
        super.onFinishInflate()

        buttonHome.onClick {
            backstack.goTo(OtherKey())
        }

        val homeKey = Backstack.getKey<HomeKey>(context) // get args
    }
}

