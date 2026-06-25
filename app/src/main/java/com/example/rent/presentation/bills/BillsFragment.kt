package com.example.rent.presentation.bills

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.rent.R
import com.example.rent.core.common.UiState
import com.example.rent.core.mock.SmartRentMockData
import com.example.rent.databinding.FragmentBillsBinding
import com.example.rent.presentation.model.BillUiModel

class BillsFragment : Fragment() {
    private var _binding: FragmentBillsBinding? = null
    private val binding get() = _binding!!
    private val billsState: UiState<List<BillUiModel>> = UiState.Success(SmartRentMockData.bills)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentBillsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.createBillButton.setOnClickListener {
            findNavController().navigate(R.id.action_bills_to_create_bill)
        }

        binding.emptyCreateBillButton.setOnClickListener {
            findNavController().navigate(R.id.action_bills_to_create_bill)
        }

        binding.billPendingCard.setOnClickListener {
            findNavController().navigate(R.id.action_bills_to_bill_detail)
        }

        binding.viewBillPendingButton.setOnClickListener {
            findNavController().navigate(R.id.action_bills_to_bill_detail)
        }

        binding.billPaidCard.setOnClickListener {
            findNavController().navigate(R.id.action_bills_to_bill_detail)
        }

        binding.billOverdueCard.setOnClickListener {
            findNavController().navigate(R.id.action_bills_to_bill_detail)
        }

        renderState(billsState)
    }

    private fun renderState(state: UiState<List<BillUiModel>>) {
        when (state) {
            UiState.Loading -> renderLoading()
            is UiState.Success -> {
                if (state.data.isEmpty()) renderEmpty() else renderContent(state.data)
            }
            is UiState.Empty -> renderEmpty()
            is UiState.Error -> renderError(state.message)
        }
    }

    private fun renderLoading() {
        setBillCardsVisible(false)
        binding.billsEmptyStateCard.visibility = View.GONE
    }

    private fun renderContent(bills: List<BillUiModel>) {
        val hasBills = bills.isNotEmpty()
        setBillCardsVisible(hasBills)
        binding.billsEmptyStateCard.visibility = if (hasBills) View.GONE else View.VISIBLE
    }

    private fun renderEmpty() {
        setBillCardsVisible(false)
        binding.billsEmptyStateCard.visibility = View.VISIBLE
    }

    private fun renderError(message: String) {
        setBillCardsVisible(false)
        binding.billsEmptyStateCard.visibility = View.GONE
    }

    private fun setBillCardsVisible(isVisible: Boolean) {
        val visibility = if (isVisible) View.VISIBLE else View.GONE
        binding.billPendingCard.visibility = visibility
        binding.billPaidCard.visibility = visibility
        binding.billOverdueCard.visibility = visibility
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
