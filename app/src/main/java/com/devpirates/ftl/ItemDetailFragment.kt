package com.devpirates.ftl

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions


class ItemDetailFragment : Fragment() {

    val args: ItemDetailFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var binding: View? = null
        when (args.item.type.lowercase()) {
            "image" -> {
                binding = inflater.inflate(R.layout.fragment_item_detail_image, container, false)
                binding.findViewById<TextView>(R.id.detailImageTextView).text = args.item.content
                Glide.with(this).load(args.item.url).apply(
                    RequestOptions()
                        .error(R.drawable.baseline_broken_image_24)
                        .centerCrop()
                ).into(binding.findViewById(R.id.detailImageView))
            }
            else -> { // "text"/fallback
                binding = inflater.inflate(R.layout.fragment_item_detail, container, false)
                binding.findViewById<TextView>(R.id.detailTextView).text = args.item.content
            }
        }
        val toolbar = requireActivity().findViewById<androidx.appcompat.widget.Toolbar>(R.id.toolbar)
        toolbar.title = args.item.name
        return binding;
    }
}