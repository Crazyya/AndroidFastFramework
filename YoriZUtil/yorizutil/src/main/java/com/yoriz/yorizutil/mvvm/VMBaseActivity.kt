package com.yoriz.yorizutil.mvvm

import com.yoriz.yorizutil.BaseActivity
import com.yoriz.yorizutil.widget.YoriLoadingDialog

/**
 * Created by yoriz
 * on 2019-09-23 12:12.
 */
abstract class VMBaseActivity<VM : BaseViewModel> : BaseActivity() {

    protected abstract fun initViewModel(): VM

    protected val loadingDialog: YoriLoadingDialog by lazy {
        YoriLoadingDialog(this)
    }

    protected val vm: VM by lazy {
        initViewModel()
    }

    override fun initCreate() {
        vm.getShowDialog({ lifecycle }, {
            if (it) {
                loadingDialog.show()
            } else {
                loadingDialog.dismiss()
            }
        })
        vm.getError({ lifecycle }, {
            showError(it)
        })
        vm.getMsg({ lifecycle }, {
            showMsg(it)
        })
    }

    protected abstract fun showError(data: Any)

    protected abstract fun showMsg(data: Any)
}