package com.example.smartrent

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class IncidentAdapter(
    incidentList: List<IncidentData>,
    private val onStatusClick: (IncidentData) -> Unit // Truyền sự kiện bấm nút ra ngoài Activity
) : RecyclerView.Adapter<IncidentAdapter.IncidentViewHolder>() {

    private var listAll: List<IncidentData> = incidentList
    private var listFiltered: List<IncidentData> = incidentList

    class IncidentViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvRoomName: TextView = itemView.findViewById(R.id.tvRoomName)
        val tvTitle: TextView = itemView.findViewById(R.id.tvTitle)
        val tvDescription: TextView = itemView.findViewById(R.id.tvDescription)
        val tvStatus: TextView = itemView.findViewById(R.id.tvStatus)
        val btnChangeStatus: Button = itemView.findViewById(R.id.btnChangeStatus)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): IncidentViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_incident, parent, false)
        return IncidentViewHolder(view)
    }

    override fun onBindViewHolder(holder: IncidentViewHolder, position: Int) {
        val incident = listFiltered[position]
        
        // Kiểm tra an toàn xem dữ liệu room có null không
        holder.tvRoomName.text = "Phòng: ${incident.room?.name ?: "N/A"}"
        holder.tvTitle.text = incident.title
        holder.tvDescription.text = incident.description

        // Đổi màu chữ theo trạng thái
        when (incident.status) {
            "pending" -> {
                holder.tvStatus.text = "Trạng thái: Chờ xử lý"
                holder.tvStatus.setTextColor(android.graphics.Color.RED)
            }
            "processing" -> {
                holder.tvStatus.text = "Trạng thái: Đang sửa chữa"
                holder.tvStatus.setTextColor(android.graphics.Color.parseColor("#FF9800")) // Màu cam
            }
            "resolved" -> {
                holder.tvStatus.text = "Trạng thái: Đã xong"
                holder.tvStatus.setTextColor(android.graphics.Color.parseColor("#4CAF50")) // Màu xanh
            }
        }

        holder.btnChangeStatus.setOnClickListener {
            onStatusClick(incident)
        }
    }

    override fun getItemCount(): Int = listFiltered.size

    fun updateData(newList: List<IncidentData>) {
        this.listAll = newList
        this.listFiltered = newList
        notifyDataSetChanged()
    }

    fun filter(query: String) {
        listFiltered = if (query.isEmpty()) {
            listAll
        } else {
            listAll.filter {
                it.title.contains(query, ignoreCase = true) || 
                it.description.contains(query, ignoreCase = true) ||
                (it.room?.name ?: "").contains(query, ignoreCase = true)
            }
        }
        notifyDataSetChanged()
    }
}
