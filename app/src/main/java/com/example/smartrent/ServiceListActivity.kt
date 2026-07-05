package com.example.smartrent

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ServiceListActivity : AppCompatActivity() {

    private lateinit var rvServices: RecyclerView
    private lateinit var adapter: ServiceAdapter
    private var token = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_service_list)

        token = getSharedPreferences("SmartRentPrefs", MODE_PRIVATE).getString("AUTH_TOKEN", "") ?: ""

        rvServices = findViewById(R.id.rvServices)
        rvServices.layoutManager = LinearLayoutManager(this)

        // Khởi tạo Adapter với danh sách rỗng trước
        adapter = ServiceAdapter(emptyList(),
            onEditClick = { service ->
                // Mở màn hình ManageServiceActivity và truyền dữ liệu sang để Sửa
                val intent = Intent(this, ManageServiceActivity::class.java)
                intent.putExtra("SERVICE_ID", service.id)
                intent.putExtra("SERVICE_NAME", service.name)
                intent.putExtra("SERVICE_PRICE", service.price)
                intent.putExtra("SERVICE_UNIT", service.unit)
                intent.putExtra("SERVICE_DESC", service.description)
                startActivity(intent)
            },
            onDeleteClick = { service ->
                // Hiển thị Popup xác nhận trước khi xóa
                AlertDialog.Builder(this)
                    .setTitle("Xác nhận xóa")
                    .setMessage("Bạn có chắc chắn muốn xóa dịch vụ ${service.name} không?")
                    .setPositiveButton("Xóa") { _, _ -> deleteServiceFromApi(service.id) }
                    .setNegativeButton("Hủy", null)
                    .show()
            }
        )
        rvServices.adapter = adapter

        findViewById<Button>(R.id.btnAddServiceNew).setOnClickListener {
            // Mở màn hình Thêm mới
            startActivity(Intent(this, ManageServiceActivity::class.java))
        }
    }

    // Mỗi khi màn hình này hiện lại (ví dụ sau khi thêm/sửa xong đóng màn hình kia), sẽ tự động load lại API
    override fun onResume() {
        super.onResume()
        loadServicesFromApi()
    }

    private fun loadServicesFromApi() {
        RetrofitClient.getInstance(this).getServices(token).enqueue(object : Callback<ServiceListResponse> {
            override fun onResponse(call: Call<ServiceListResponse>, response: Response<ServiceListResponse>) {
                if (response.isSuccessful && response.body()?.success == true) {
                    val list = response.body()?.data ?: emptyList()
                    adapter.updateData(list)
                }
            }
            override fun onFailure(call: Call<ServiceListResponse>, t: Throwable) {
                Toast.makeText(this@ServiceListActivity, "Lỗi tải dữ liệu", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun deleteServiceFromApi(id: Int) {
        RetrofitClient.getInstance(this).deleteService(token, id).enqueue(object : Callback<ServiceCrudResponse> {
            override fun onResponse(call: Call<ServiceCrudResponse>, response: Response<ServiceCrudResponse>) {
                if (response.isSuccessful) {
                    Toast.makeText(this@ServiceListActivity, "Đã xóa thành công!", Toast.LENGTH_SHORT).show()
                    loadServicesFromApi() // Load lại danh sách sau khi xóa
                }
            }
            override fun onFailure(call: Call<ServiceCrudResponse>, t: Throwable) {
                Toast.makeText(this@ServiceListActivity, "Lỗi mạng", Toast.LENGTH_SHORT).show()
            }
        })
    }
}
