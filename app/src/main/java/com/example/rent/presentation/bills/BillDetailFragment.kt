package com.example.rent.presentation.bills

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.rent.R
import com.example.rent.databinding.FragmentBillDetailBinding

class BillDetailFragment : Fragment() {
    private var _binding: FragmentBillDetailBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentBillDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.editBillButton.setOnClickListener {
            findNavController().navigate(R.id.action_bill_detail_to_edit_bill)
        }

        binding.meterReadingButton.setOnClickListener {
            findNavController().navigate(R.id.action_bill_detail_to_meter_reading)
        }

        binding.recordPaymentButton.setOnClickListener {
            findNavController().navigate(R.id.action_bill_detail_to_payment_record)
        }

        binding.primaryPaymentButton.setOnClickListener {
            findNavController().navigate(R.id.action_bill_detail_to_payment_record)
        }

        // TODO Phase 2F+: connect sharing/export when real invoice publishing exists.
        binding.shareBillButton.isEnabled = false
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
