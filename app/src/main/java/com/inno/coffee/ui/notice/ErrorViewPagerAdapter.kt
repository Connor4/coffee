package com.inno.coffee.ui.notice

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.inno.coffee.R

class ErrorViewPagerAdapter(private val list: List<DialogData>) : RecyclerView
.Adapter<ErrorViewPagerAdapter.ErrorViewHolder>() {

    class ErrorViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val titleTv: TextView = itemView.findViewById(R.id.dialog_item_title_tv)
        val descriptionTv: TextView = itemView.findViewById(R.id.dialog_item_message_tv)
        val errorIv: ImageView = itemView.findViewById(R.id.dialog_item_error_iv)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ErrorViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout
            .global_dialog_item_layout, parent, false)
        return ErrorViewHolder(view)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ErrorViewHolder, position: Int) {
        val item = list[position]
        holder.titleTv.text = "${item.errorCode}"
        holder.descriptionTv.text = item.message
        holder.errorIv.setImageResource(R.drawable.ic_launcher_foreground)
    }

}