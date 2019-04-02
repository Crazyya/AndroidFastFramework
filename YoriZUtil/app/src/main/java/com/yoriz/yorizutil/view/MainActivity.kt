package com.yoriz.yorizutil.view

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.widget.AppCompatButton
import androidx.recyclerview.widget.LinearLayoutManager
import com.yoriz.yorizutil.R
import com.yoriz.yorizutil.YoriDialog
import com.yoriz.yorizutil.YoriToast
import com.yoriz.yorizutil.contact.MainContact
import com.yoriz.yorizutil.mvp.BasePresenter
import com.yoriz.yorizutil.mvp.MVPActivity
import com.yoriz.yorizutil.presenter.MainPresenter
import com.yoriz.yorizutil.recycler.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.dialog_main.view.*

/**
 * Created by yoriz
 * on 2019/4/2 11:46 AM.
 */
class MainActivity(override val layoutId: Int = R.layout.activity_main) : MVPActivity(), MainContact.IMainView {
    private val presenter: MainContact.IMainPresenter = MainPresenter(this)
    private val data: List<String> = ArrayList()
    private lateinit var dialog: YoriDialog

    override fun bindPresenter(): BasePresenter {
        return presenter
    }

    override fun showData(msg: List<String>) {
        data as ArrayList
        data.clear()
        data.addAll(msg)
        rv.adapter!!.notifyDataSetChanged()
    }

    override fun initCreate() {
        dialog = YoriDialog.Builder(this, R.layout.dialog_main).let {
            it.setOnClickListener { view, manager ->
                when (view.id) {
                    R.id.yes -> {
                        YoriToast.showLongToast("check yes")
                        manager.cancel()
                    }
                    R.id.no -> {
                        YoriToast.showLongToast("check no")
                        manager.cancel()
                    }
                }
            }
            it.createDialog()
        }.apply {
            setDetail { view, dialog ->
                val title = "YoriZ Dialog"
                view.title.text = title
                val msg = "This is Dialog message"
                view.msg.text = msg
                dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            }
        }

        with(rv) {
            layoutManager = ScrollLinearLayoutManager(this@MainActivity, LinearLayoutManager.VERTICAL, false)
            addItemDecoration(YoriDividerItemDecoration(this@MainActivity, LinearLayoutManager.VERTICAL))
            adapter = UniversalAdapter(data, object : OnBindDataInterface<String> {
                override fun onBindData(model: String, viewHolder: UniversalViewHolder, viewType: Int) {
                    with(viewHolder) {
                        getSubView<AppCompatButton>(R.id.btn).run {
                            text = model
                            setOnClickListener {
                                dialog.show()
                            }
                        }
                    }
                }

                override fun getItemLayoutId(viewType: Int): Int {
                    return R.layout.item_main_button
                }

            })
        }

        presenter.startGetData()
    }

}