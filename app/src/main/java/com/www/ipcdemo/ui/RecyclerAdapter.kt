package com.www.ipcdemo.ui

import android.annotation.SuppressLint
import android.app.Service
import android.graphics.Color
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.www.ipcdemo.R
import java.util.*

class RecyclerAdapter(val list: MutableList<String>, recycler: RecyclerView) :
    RecyclerView.Adapter<RecyclerAdapter.RecyclerHolder>() {

    var isDel = false


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_text, parent, false)
        return RecyclerHolder(view)
    }

    override fun getItemCount(): Int {
        return list.size
    }


    @SuppressLint("ClickableViewAccessibility")
    override fun onBindViewHolder(holder: RecyclerHolder, position: Int) {
        if (isDel) {
            holder.del.visibility = View.VISIBLE
        } else {
            holder.del.visibility = View.GONE
        }
        holder.text.text = list[position]
        holder.del.setOnClickListener {
            notifyItemRemoved(position)
            list.removeAt(position)
        }
    }

    class RecyclerHolder(val view: View) : RecyclerView.ViewHolder(view) {
        val text: AppCompatTextView = view.findViewById(R.id.item_text)
        val del: AppCompatImageView = view.findViewById(R.id.item_del)
        val layout: ConstraintLayout = view.findViewById(R.id.item_layout)
    }


    inner class ItemTouchListener : ItemTouchCallback.OnItemTouchListener {
        override fun onMove(fromPosition: Int, toPosition: Int) {
            if (fromPosition < toPosition) {
                //从上往下拖动，每滑动一个item，都将list中的item向下交换，向上滑同理。
                for (i in fromPosition until toPosition) {
                    Collections.swap(list, i, i + 1)
                }
            } else {
                for (i in fromPosition downTo toPosition + 1) {
                    Collections.swap(list, i, i - 1)
                }
            }
            notifyItemMoved(fromPosition, toPosition);
        }

        @RequiresApi(Build.VERSION_CODES.O)
        override fun onLongClick(holder: RecyclerView.ViewHolder?) {
            isDel = true
            val h = holder as RecyclerHolder
            h.text.setBackgroundColor(Color.RED)
            //获取系统震动服务
            val vib = holder.text.context.getSystemService(Service.VIBRATOR_SERVICE) as Vibrator
            vib.vibrate(VibrationEffect.createOneShot(70L, VibrationEffect.DEFAULT_AMPLITUDE));
        }

        override fun onUp(viewHolder: RecyclerView.ViewHolder) {
            val holder = viewHolder as RecyclerHolder
            holder.text.setBackgroundColor(Color.RED)
            notifyDataSetChanged()
        }
    }

}