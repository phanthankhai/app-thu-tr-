package com.example.smartrent

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ServiceAdapter(
    private var serviceList: List<ServiceData>,
    private val onEditClick: (ServiceData) -> Unit,
    private val onDeleteClick: (ServiceData) -> Unit
) : RecyclerView.Adapter<ServiceAdapter.ServiceViewHolder>() {

    class ServiceViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvName: TextView = view.findViewById(R.id.tvServiceName)
        val tvPriceUnit: TextView = view.findViewById(R.id.tvServicePriceUnit)
        val btnEdit: ImageButton = view.findViewById(R.id.btnEdit)
        val btnDelete: ImageButton = view.findViewById(R.id.btnDelete)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ServiceViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_service, parent, false)
        return ServiceViewHolder(view)
    }

    override fun onBindViewHolder(holder: ServiceViewHolder, position: Int) {
        val service = serviceList[position]
        holder.tvName.text = service.name
        holder.tvPriceUnit.text = "${service.price} VNĐ / ${service.unit}"

        holder.btnEdit.setOnClickListener { onEditClick(service) }
        holder.btnDelete.setOnClickListener { onDeleteClick(service) }
    }

    override fun getItemCount(): Int = serviceList.size

    // Hàm để cập nhật lại danh sách khi có dữ liệu mới
    fun updateData(newList: List<ServiceData>) {
        serviceList = newList
        notifyDataSetChanged()
    }
}
