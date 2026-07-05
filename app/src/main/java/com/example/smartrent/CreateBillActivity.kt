package com.example.smartrent

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CreateBillActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_bill)

        val etRoomId = findViewById<EditText>(R.id.etRoomId)
        val etMonth = findViewById<EditText>(R.id.etBillingMonth)
        val etOldE = findViewById<EditText>(R.id.etOldElectric)
        val etNewE = findViewById<EditText>(R.id.etNewElectric)
        val etOldW = findViewById<EditText>(R.id.etOldWater)
        val etNewW = findViewById<EditText>(R.id.etNewWater)
        val btnSubmit = findViewById<Button>(R.id.btnSubmitBill)

        val token = getSharedPreferences("SmartRentPrefs", MODE_PRIVATE).getString("AUTH_TOKEN", "") ?: ""

        btnSubmit.setOnClickListener {
            val roomIdStr = etRoomId.text.toString()
            val month = etMonth.text.toString()
            val oldEStr = etOldE.text.toString()
            val newEStr = etNewE.text.toString()
            val oldWStr = etOldW.text.toString()
            val newWStr = etNewW.text.toString()

            // Validate sơ bộ
            if (roomIdStr.isEmpty() || month.isEmpty() || oldEStr.isEmpty() || newEStr.isEmpty() || oldWStr.isEmpty() || newWStr.isEmpty()) {
                Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val request = BillRequest(
                roomIdStr.toInt(), month,
                oldEStr.toInt(), newEStr.toInt(),
                oldWStr.toInt(), newWStr.toInt()
            )

            // Gọi API chốt bill
            RetrofitClient.getInstance(this).createBill(token, request).enqueue(object : Callback<BillResponse> {
                override fun onResponse(call: Call<BillResponse>, response: Response<BillResponse>) {
                    if (response.isSuccessful && response.body()?.success == true) {
                        val data = response.body()?.data
                        showSuccessPopup(data)
                    } else {
                        Toast.makeText(this@CreateBillActivity, "Lỗi: Kiểm tra lại số liệu (Số mới phải >= số cũ)", Toast.LENGTH_LONG).show()
                    }
                }

                override fun onFailure(call: Call<BillResponse>, t: Throwable) {
                    Toast.makeText(this@CreateBillActivity, "Lỗi kết nối mạng", Toast.LENGTH_SHORT).show()
                }
            })
        }
    }

    private fun showSuccessPopup(data: BillInfoData?) {
        if (data == null) return

        // Dựng chuỗi hiển thị chi tiết chia tiền điện nước
        val splitText = StringBuilder("Chi tiết chia tiền điện nước:\n")
        data.split_details?.forEach { (name, amount) ->
            splitText.append("- $name: $amount đ\n")
        }

        AlertDialog.Builder(this)
            .setTitle("CHỐT HÓA ĐƠN THÀNH CÔNG!")
            .setMessage("Tổng hóa đơn: ${data.total_bill} VNĐ\n\n$splitText")
            .setPositiveButton("Hoàn tất") { _, _ ->
                finish() // Đóng màn hình
            }
            .setCancelable(false)
            .show()
    }
}
