package com.yoriz.yorizdemo

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context

/**
 * Created by yoriz
 * on 2019/2/27 11:32 AM.
 */
object Test {

    /*AlertDialog.Builder(this.applicationContext).setTitle("111")
    .setCancelable(false)
    .setMessage("222")
    .setPositiveButton("确定") { _, _ ->

    }
    .setNegativeButton("退出") { _, _ ->
        onBackPressed()
    }.show()*/

    fun testDialog(context: Context) {
        val dialog: AlertDialog
        dialog = AlertDialog.Builder(context.applicationContext)
            .setNegativeButton("exit") { v, _ ->
                v.cancel()
            }
            .create()
        dialog.show()


        val dialog2 = Dialog(context.applicationContext)
        dialog2.setOnCancelListener {
            dialog2.dismiss()
        }
        dialog2.show()
    }
}
