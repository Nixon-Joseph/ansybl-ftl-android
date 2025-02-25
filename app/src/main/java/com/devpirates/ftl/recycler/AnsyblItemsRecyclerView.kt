package com.devpirates.ftl.recycler

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.devpirates.ftl.R
import com.devpirates.ftl.models.AnsyblItem

class AnsyblItemsRecyclerView(private val dataset: List<AnsyblItem>) : RecyclerView.Adapter<AnsyblItemsRecyclerView.ViewHolder>() {
    var onItemClick: ((AnsyblItem) -> Unit)? = null;

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val textView: TextView

        init {
            // Define click listener for the ViewHolder's View
            textView = view.findViewById(R.id.ansyblItemTextView)
            view.setOnClickListener {
                onItemClick?.invoke(dataset[layoutPosition])
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val textView = LayoutInflater.from(parent.context)
                .inflate(R.layout.ansybl_item_row, parent, false) as ConstraintLayout
            return ViewHolder(textView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.textView.text = dataset[position].name
    }

    override fun getItemCount() = dataset.size
};