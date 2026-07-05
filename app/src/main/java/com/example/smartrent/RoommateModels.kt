package com.example.smartrent

import java.io.Serializable

data class Roommate(
    val id: Int,
    val name: String,
    var isSharingElectric: Boolean = true, // Tích chia điện
    var isSharingWater: Boolean = true,    // Tích chia nước
    var isSharingServices: Boolean = true  // Tích chia dịch vụ
) : Serializable
