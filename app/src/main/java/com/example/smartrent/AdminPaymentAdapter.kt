package com.example.smartrent

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import java.util.Locale

class AdminPaymentAdapter(
    private var paymentList: List<PaymentData>,
    private val onApproveClicked: (PaymentData) -> Unit
) : RecyclerView.Adapter<AdminPaymentAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvTenantName: TextView = view.findViewById(R.id.tvTenantName)
        val tvRoomAndAmount: TextView = view.findViewById(R.id.tvRoomAndAmount)
        val btnApprove: Button = view.findViewById(R.id.btnApprovePayment)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_admin_payment, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = paymentList[position]
        
        holder.tvTenantName.text = item.user?.name ?: "Ẩn danh"
        
        val roomName = item.bill?.room?.name ?: "Chưa rõ phòng"
        // Ép kiểu chuỗi tiền tệ từ API về Double để format dấu phẩy phân cách hàng nghìn
        val amountDouble = item.amount.toDoubleOrNull() ?: 0.0
        val formattedAmount = String.format(Locale.getDefault(), "%,.0fđ", amountDouble)
        
        holder.tvRoomAndAmount.text = "Phòng: $roomName • Số tiền: $formattedAmount"

        holder.btnApprove.setOnClickListener {
            onApproveClicked(item)
        }
    }

    override fun getItemCount() = paymentList.size

    fun updateList(newList: List<PaymentData>) {
        this.paymentList = newList
        notifyDataSetChanged()
    }
}