package com.example.smartrent

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import java.text.DecimalFormat

// Thêm một hàm (onItemClick) vào trong ngoặc để báo về cho HomeActivity khi có phòng bị bấm
class RoomAdapter(
    private val roomList: List<Room>,
    private val onItemClick: (Room) -> Unit
) : RecyclerView.Adapter<RoomAdapter.RoomViewHolder>() {

    // 1. Ánh xạ các thành phần trong cái khuôn item_room.xml
    class RoomViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvRoomNumber: TextView = itemView.findViewById(R.id.tvRoomNumber)
        val tvPrice: TextView = itemView.findViewById(R.id.tvPrice)
        val tvStatus: TextView = itemView.findViewById(R.id.tvStatus)
    }

    // 2. Lấy cái khuôn item_room.xml ra để chuẩn bị đổ dữ liệu
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RoomViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_room, parent, false)
        return RoomViewHolder(view)
    }

    // 3. Đổ dữ liệu thật vào khuôn dựa theo vị trí (position) của danh sách
    override fun onBindViewHolder(holder: RoomViewHolder, position: Int) {
        val currentRoom = roomList[position]

        holder.tvRoomNumber.text = "Phòng ${currentRoom.name}"

        // Định dạng giá tiền có dấu chấm phân cách (VD: 2.000.000)
        val formatter = DecimalFormat("#,###")
        val priceDouble = currentRoom.price.toDoubleOrNull() ?: 0.0
        val formattedPrice = formatter.format(priceDouble)
        holder.tvPrice.text = "Giá: $formattedPrice đ"

        // Dịch trạng thái tiếng Anh từ Database sang tiếng Việt
        if (currentRoom.status == "empty") {
            holder.tvStatus.text = "Trạng thái: Trống"
            holder.tvStatus.setTextColor(android.graphics.Color.parseColor("#4CAF50"))
        } else {
            holder.tvStatus.text = "Trạng thái: Đã thuê"
            holder.tvStatus.setTextColor(android.graphics.Color.parseColor("#F44336"))
        }

        // BẮT SỰ KIỆN CLICK VÀO CẢ CÁI THẺ PHÒNG
        holder.itemView.setOnClickListener {
            onItemClick(currentRoom)
        }
    }

    // 4. Báo cho hệ thống biết có tổng cộng bao nhiêu phòng
    override fun getItemCount(): Int {
        return roomList.size
    }
}
