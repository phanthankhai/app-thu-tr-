package com.example.smartrent

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class BillAdapter(
    private var billList: List<BillData>,
    private val onApproveClick: (Int) -> Unit
) : RecyclerView.Adapter<BillAdapter.BillViewHolder>() {

    class BillViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvTitle: TextView = view.findViewById(R.id.tvBillTitle)
        val tvTotal: TextView = view.findViewById(R.id.tvBillTotal)
        val tvStatus: TextView = view.findViewById(R.id.tvBillStatus)
        val btnApprove: Button = view.findViewById(R.id.btnPayBill)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BillViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_bill, parent, false)
        return BillViewHolder(view)
    }

    override fun onBindViewHolder(holder: BillViewHolder, position: Int) {
        val bill = billList[position]
        
        val roomName = bill.room?.name ?: bill.room_id.toString()
        holder.tvTitle.text = "Phòng $roomName - Tháng ${bill.billing_month}"
        holder.tvTotal.text = "Tổng: ${bill.total_amount} VNĐ"

        when (bill.status) {
            "unpaid" -> {
                holder.tvStatus.text = "Trạng thái: Chưa thanh toán"
                holder.tvStatus.setTextColor(Color.RED)
                holder.btnApprove.visibility = View.GONE
            }
            "pending" -> {
                holder.tvStatus.text = "Trạng thái: Chờ xác nhận ⏳"
                holder.tvStatus.setTextColor(Color.parseColor("#FF9800"))
                holder.btnApprove.visibility = View.VISIBLE
                holder.btnApprove.text = "Xác nhận thành công"
            }
            "paid" -> {
                holder.tvStatus.text = "Thanh toán thành công ✅"
                holder.tvStatus.setTextColor(Color.parseColor("#4CAF50"))
                holder.btnApprove.visibility = View.GONE
            }
            else -> {
                holder.tvStatus.text = "Trạng thái: ${bill.status}"
                holder.tvStatus.setTextColor(Color.GRAY)
                holder.btnApprove.visibility = View.GONE
            }
        }

        holder.btnApprove.setOnClickListener { onApproveClick(bill.id) }
    }

    override fun getItemCount(): Int = billList.size

    fun updateData(newList: List<BillData>) {
        billList = newList
        notifyDataSetChanged()
    }
}
