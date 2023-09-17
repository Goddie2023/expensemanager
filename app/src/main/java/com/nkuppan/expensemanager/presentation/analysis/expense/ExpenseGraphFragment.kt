package com.nkuppan.expensemanager.presentation.analysis.expense

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.nkuppan.expensemanager.core.ui.fragment.BaseBindingFragment
import com.nkuppan.expensemanager.databinding.FragmentExpenseGraphListBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch


@AndroidEntryPoint
class ExpenseGraphFragment : BaseBindingFragment<FragmentExpenseGraphListBinding>() {

    private val viewModel: ExpenseGraphViewModel by viewModels()

    private val adapter = CategoryTransactionListAdapter {
        viewLifecycleOwner.lifecycleScope.launch {
            //TODO open transaction list screen with category id
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.actionAdd.setOnClickListener {
        }

        binding.dataRecyclerView.adapter = adapter

        initializeObserver()
    }

    private fun initializeObserver() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    viewModel.graphItems.collectLatest {
                        val hasRecords = it.isNotEmpty()
                        binding.infoContainer.isVisible = !hasRecords
                        binding.dataRecyclerView.isVisible = hasRecords
                        adapter.submitList(it)
                    }
                }
            }
        }
    }

    override fun inflateLayout(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): FragmentExpenseGraphListBinding {
        return FragmentExpenseGraphListBinding.inflate(inflater, container, false)
    }
}