package com.example.smartrent

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class TenantAdapter(
    private var tenantList: List<TenantData>,
    private val onEndRentClick: (Int) -> Unit
) : RecyclerView.Adapter<TenantAdapter.TenantViewHolder>() {

    class TenantViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvName: TextView = view.findViewById(R.id.tvTenantName)
        val tvPhone: TextView = view.findViewById(R.id.tvTenantPhone)
        val tvRoom: TextView = view.findViewById(R.id.tvTenantRoom)
        val btnEndRent: Button = view.findViewById(R.id.btnEndRent)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TenantViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_tenant, parent, false)
        return TenantViewHolder(view)
    }

    override fun onBindViewHolder(holder: TenantViewHolder, position: Int) {
        val tenant = tenantList[position]
        
        holder.tvName.text = tenant.name
        holder.tvPhone.text = "SĐT: ${tenant.phone}"

        // Xử lý logic nếu khách thuê chưa được xếp phòng hoặc đã bị đuổi
        if (tenant.room_id != null) {
            val roomName = tenant.room?.name ?: tenant.room_id.toString()
            holder.tvRoom.text = "Đang ở: Phòng $roomName"
            holder.btnEndRent.visibility = View.VISIBLE
        } else {
            holder.tvRoom.text = "Trạng thái: Chưa có phòng"
            holder.btnEndRent.visibility = View.GONE // Ẩn nút đuổi nếu không có phòng
        }

        holder.btnEndRent.setOnClickListener { 
            onEndRentClick(tenant.id) 
        }
    }

    override fun getItemCount(): Int = tenantList.size

    fun updateData(newList: List<TenantData>) {
        tenantList = newList
        notifyDataSetChanged()
    }
}
