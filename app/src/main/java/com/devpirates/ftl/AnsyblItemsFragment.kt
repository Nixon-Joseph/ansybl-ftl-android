package com.devpirates.ftl

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.devpirates.ftl.databinding.FragmentAnsyblItemsBinding
import com.devpirates.ftl.recycler.AnsyblItemsRecyclerView

/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */
class AnsyblItemsFragment : Fragment() {

    private var _binding: FragmentAnsyblItemsBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    val args: AnsyblItemsFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAnsyblItemsBinding.inflate(inflater, container, false)

        val recyclerView = binding.root.findViewById<RecyclerView>(R.id.items_recycler_view)
        recyclerView?.layoutManager = LinearLayoutManager(super.getContext())
        val adapter = AnsyblItemsRecyclerView(args.item.items)
        recyclerView?.adapter = adapter
        val toolbar = requireActivity().findViewById<androidx.appcompat.widget.Toolbar>(R.id.toolbar)
        toolbar.title = args.item.summary

        adapter.onItemClick = { item ->
            val action = AnsyblItemsFragmentDirections.actionAnsyblItemsFragmentToItemDetailFragment(item)
            findNavController().navigate(action)
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}