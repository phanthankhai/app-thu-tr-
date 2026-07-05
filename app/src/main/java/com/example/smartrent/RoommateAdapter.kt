package com.example.smartrent

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class RoommateAdapter(
    private val roommates: List<Roommate>,
    private val onCheckChanged: () -> Unit 
) : RecyclerView.Adapter<RoommateAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvName: TextView = view.findViewById(R.id.tvRoommateName)
        val cbElectric: CheckBox = view.findViewById(R.id.cbElectric)
        val cbWater: CheckBox = view.findViewById(R.id.cbWater)
        val cbService: CheckBox = view.findViewById(R.id.cbService)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_roommate_checkbox, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val roommate = roommates[position]
        holder.tvName.text = roommate.name
        
        // Gỡ listener cũ
        holder.cbElectric.setOnCheckedChangeListener(null)
        holder.cbWater.setOnCheckedChangeListener(null)
        holder.cbService.setOnCheckedChangeListener(null)

        // Set trạng thái
        holder.cbElectric.isChecked = roommate.isSharingElectric
        holder.cbWater.isChecked = roommate.isSharingWater
        holder.cbService.isChecked = roommate.isSharingServices

        // Lắng nghe thay đổi
        holder.cbElectric.setOnCheckedChangeListener { _, isChecked -> 
            roommate.isSharingElectric = isChecked
            onCheckChanged()
        }
        holder.cbWater.setOnCheckedChangeListener { _, isChecked -> 
            roommate.isSharingWater = isChecked
            onCheckChanged()
        }
        holder.cbService.setOnCheckedChangeListener { _, isChecked -> 
            roommate.isSharingServices = isChecked
            onCheckChanged()
        }
    }

    override fun getItemCount() = roommates.size
}
