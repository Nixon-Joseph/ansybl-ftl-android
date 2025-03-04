package com.devpirates.ftl.recycler

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.devpirates.ftl.R
import com.devpirates.ftl.room.ansyblconnection.AnsyblConnection

class AnsyblConnectionRecyclerView(private val dataset: ArrayList<AnsyblConnection>) : RecyclerView.Adapter<AnsyblConnectionRecyclerView.ViewHolder>() {
    var onItemClick: ((AnsyblConnection) -> Unit)? = null;

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val textView: TextView

        init {
            // Define click listener for the ViewHolder's View
            textView = view.findViewById(R.id.connectionTextView)
            view.setOnClickListener {
                onItemClick?.invoke(dataset[layoutPosition])
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val textView = LayoutInflater.from(parent.context)
                .inflate(R.layout.ansybl_connection_row, parent, false) as ConstraintLayout
            return ViewHolder(textView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.textView.text = dataset[position].summary
    }

    override fun getItemCount() = dataset.size
};