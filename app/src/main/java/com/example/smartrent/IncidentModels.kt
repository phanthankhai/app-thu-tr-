package com.example.smartrent

data class IncidentListResponse(
    val success: Boolean,
    val data: List<IncidentData>
)

data class IncidentData(
    val id: Int,
    val room_id: Int,
    val title: String,
    val description: String,
    val status: String, // pending, in_progress, resolved
    val created_at: String,
    val room: RoomData?
)

data class StatusResponse(
    val success: Boolean,
    val message: String
)
