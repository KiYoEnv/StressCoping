package com.example.stresscoping.view

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.stresscoping.StressCopingListViewAdapter
import com.example.stresscoping.viewmodel.StressCopingListViewModel
import com.example.stresscoping.databinding.FragmentStressCopingListBinding
import com.example.stresscoping.model.StressCopingModel

/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */
class StressCopingListFragment : Fragment() {
    private val viewModel: StressCopingListViewModel by viewModels()
    private lateinit var binding: FragmentStressCopingListBinding
    private lateinit var stressCopingListViewAdapter: StressCopingListViewAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentStressCopingListBinding.inflate(inflater, container, false)
        binding.fabAddStressCoping.setOnClickListener {
            val dialogFragment = StressCopingAddDialogFragment()
            dialogFragment.listener = viewModel
            dialogFragment.show(childFragmentManager, "StressCopingAddDialogFragment")
        }

        stressCopingListViewAdapter = StressCopingListViewAdapter(viewLifecycleOwner, viewModel)
        binding.recyclerviewStressCopingList.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            itemAnimator = DefaultItemAnimator()
            adapter = stressCopingListViewAdapter
            addItemDecoration(DividerItemDecoration(context, LinearLayoutManager.VERTICAL))
        }
        return binding.root

    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.run {
            stressCopingListItems.observe(viewLifecycleOwner) { stressCopingListItems ->
                stressCopingListViewAdapter.submitList(stressCopingListItems)
            }
            showEditDialog.observe(viewLifecycleOwner) { stressCoping ->
                val dialog = StressCopingEditDialogFragment()
                dialog.listener = this
                dialog.arguments = Bundle().apply {
                    putString(StressCopingModel.KEY_STRESS_COPING_MODEL, stressCoping.toJson())
                }
                dialog.show(childFragmentManager, "stress_coping_edit_dialog")
            }
            showDeleteDialog.observe(viewLifecycleOwner) { stressCoping ->
                val dialog = StressCopingDeleteDialogFragment()
                dialog.listener = this
                dialog.arguments = Bundle().apply {
                    putString(StressCopingModel.KEY_STRESS_COPING_MODEL, stressCoping.toJson())
                }
                dialog.show(childFragmentManager, "stress_coping_delete_dialog")
            }
            refreshRecyclerViewAdapter.observe(viewLifecycleOwner) {
                stressCopingListViewAdapter.notifyDataSetChanged()
            }
            refreshRecyclerViewAdapterByPosition.observe(viewLifecycleOwner) { position ->
                stressCopingListViewAdapter.notifyItemChanged(position)
            }
        }
    }
}