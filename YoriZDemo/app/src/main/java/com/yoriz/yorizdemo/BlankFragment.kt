package com.yoriz.yorizdemo

import android.content.ComponentName
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.fragment_blank.view.*


class BlankFragment : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_blank, container, false)
        view.one.setOnClickListener {
            view.dv.setRatio(16, 9)
        }
        view.two.setOnClickListener {
            view.dv.setRatio(4, 3)
        }
        view.three.setOnClickListener {
            view.dv.setRatio(1, 1)
        }
        view.four.setOnClickListener {
            view.dv.setRatio(3, 2)
        }
        view.c_one.setOnClickListener {
            view.dv.dimmedColor = ContextCompat.getColor(this.context!!, R.color.cstore_red)
        }
        view.c_two.setOnClickListener {
            view.dv.dimmedColor = ContextCompat.getColor(this.context!!, R.color.sure)
        }
        view.c_three.setOnClickListener {
            view.dv.dimmedColor = ContextCompat.getColor(this.context!!, R.color.add_less)
        }
        view.c_four.setOnClickListener {
            view.dv.dimmedColor = ContextCompat.getColor(this.context!!, R.color.white)
        }

        view.ap.progress = view.dv.dimmedAlpha
        view.ap.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                view.dv.dimmedAlpha = progress
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
            }

        })
        view.bmp.setOnClickListener {
            val newBmp = view.dv.cropImg(view.img)
            view.bmp.setImageBitmap(newBmp)
        }
        /* val d = ArrayList<String>().apply {
             add("a")
             add("b")
         }


         view.list.adapter = object : BaseAdapter() {
             @SuppressLint("ViewHolder")
             override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
                 val v = inflater.inflate(R.layout.item_view, parent, false)
                 v.text.text = d[position]
                 return v
             }

             override fun getItem(position: Int): Any {
                 return d[position]
             }

             override fun getItemId(position: Int): Long {
                 return position.toLong()
             }

             override fun getCount(): Int {
                 return d.size
             }
         }
         view.list.onItemClickListener = this
         view.button.setOnClickListener {
             switchIcon()
         }*/
        return view
    }

    private fun switchIcon() {
        try {
            val one = "com.yoriz.yorizdemo.MainActivity"
            val two = "com.yoriz.yorizdemo.StartTwoActivity"
            val pm = context!!.packageManager
            val normalComponentName = ComponentName(this.context!!, one)
            val actComponentName = ComponentName(context!!, two)
            if (pm.getComponentEnabledSetting(normalComponentName) == PackageManager.COMPONENT_ENABLED_STATE_ENABLED) {
                pm.setComponentEnabledSetting(
                    normalComponentName,
                    PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                    PackageManager.DONT_KILL_APP
                )
                pm.setComponentEnabledSetting(
                    actComponentName,
                    PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                    0
                )
            } else {
                pm.setComponentEnabledSetting(
                    actComponentName,
                    PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                    PackageManager.DONT_KILL_APP
                )
                pm.setComponentEnabledSetting(
                    normalComponentName,
                    PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                    0
                )
            }

        } catch (e: Exception) {

        }
    }
}
