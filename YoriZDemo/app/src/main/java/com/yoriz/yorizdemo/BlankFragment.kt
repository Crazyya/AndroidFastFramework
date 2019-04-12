package com.yoriz.yorizdemo

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.BaseAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.fragment_blank.view.*
import kotlinx.android.synthetic.main.item_view.view.*


class BlankFragment : Fragment(), AdapterView.OnItemClickListener {
    override fun onItemClick(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        Toast.makeText(this@BlankFragment.context, "点击了$position", Toast.LENGTH_LONG).show()
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val d = ArrayList<String>().apply {
            add("a")
            add("b")
        }

        val view = inflater.inflate(R.layout.fragment_blank, container, false)

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
        return view
    }
}
