package com.yoriz.yorizframework.view

import com.yoriz.yorizframework.R
import com.yoriz.yorizframework.contact.MainContact
import com.yoriz.yorizframework.presenter.MainPresenter
import com.yoriz.yorizframework.util.mvp.BaseActivity
import com.yoriz.yorizframework.util.mvp.BasePresenter
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity(override val layoutId: Int = R.layout.activity_main) : BaseActivity(), MainContact.IMainView {


    private val presenter: MainContact.IMainPresenter = MainPresenter(this)

    override fun bindPresenter(): BasePresenter {
        return presenter
    }

    override fun showMsg(msg: String) {
        text.text = msg
    }

    override fun initData() {
        presenter.startGetMsg()
    }

    override fun initView() {
    }

    override fun initClick() {
    }
}
