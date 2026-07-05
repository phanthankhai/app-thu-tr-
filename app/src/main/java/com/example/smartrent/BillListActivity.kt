package com.example.smartrent

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class BillListActivity : AppCompatActivity() {

    private lateinit var rvBills: RecyclerView
    private lateinit var adapter: BillAdapter
    private var token = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bill_list)

        token = getSharedPreferences("SmartRentPrefs", MODE_PRIVATE).getString("AUTH_TOKEN", "") ?: ""
        rvBills = findViewById(R.id.rvBills)
        rvBills.layoutManager = LinearLayoutManager(this)

        adapter = BillAdapter(emptyList()) { billId ->
            // Khi bấm nút "Xác nhận thành công", hiển thị popup xác nhận
            AlertDialog.Builder(this)
                .setTitle("Xác nhận thu tiền")
                .setMessage("Bạn xác nhận hóa đơn này đã được thanh toán?")
                .setPositiveButton("Đồng ý") { _, _ -> confirmPayment(billId) }
                .setNegativeButton("Hủy", null)
                .show()
        }
        rvBills.adapter = adapter
    }

    override fun onResume() {
        super.onResume()
        loadBills()
    }

    private fun loadBills() {
        RetrofitClient.getInstance(this).getBills(token).enqueue(object : Callback<BillListResponse> {
            override fun onResponse(call: Call<BillListResponse>, response: Response<BillListResponse>) {
                if (response.isSuccessful && response.body()?.success == true) {
                    val list = response.body()?.data ?: emptyList()
                    adapter.updateData(list)
                }
            }
            override fun onFailure(call: Call<BillListResponse>, t: Throwable) {
                Toast.makeText(this@BillListActivity, "Lỗi tải dữ liệu", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun confirmPayment(billId: Int) {
        // Gọi API cập nhật trạng thái thanh toán (duyệt bill)
        RetrofitClient.getInstance(this).markBillAsPaid(token, billId).enqueue(object : Callback<Any> {
            override fun onResponse(call: Call<Any>, response: Response<Any>) {
                if (response.isSuccessful) {
                    Toast.makeText(this@BillListActivity, "Hóa đơn đã được xác nhận đóng đủ tiền!", Toast.LENGTH_SHORT).show()
                    loadBills() // Tải lại danh sách để ẩn nút duyệt
                } else {
                    Toast.makeText(this@BillListActivity, "Lỗi cập nhật", Toast.LENGTH_SHORT).show()
                }
            }
            override fun onFailure(call: Call<Any>, t: Throwable) {
                Toast.makeText(this@BillListActivity, "Lỗi mạng", Toast.LENGTH_SHORT).show()
            }
        })
    }
}
