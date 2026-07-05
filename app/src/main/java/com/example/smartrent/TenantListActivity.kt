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

class TenantListActivity : AppCompatActivity() {

    private lateinit var rvTenants: RecyclerView
    private lateinit var adapter: TenantAdapter
    private var token = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tenant_list)

        token = getSharedPreferences("SmartRentPrefs", MODE_PRIVATE).getString("AUTH_TOKEN", "") ?: ""
        rvTenants = findViewById(R.id.rvTenants)
        rvTenants.layoutManager = LinearLayoutManager(this)

        adapter = TenantAdapter(emptyList()) { tenantId ->
            showEndRentConfirmDialog(tenantId)
        }
        rvTenants.adapter = adapter
    }

    override fun onResume() {
        super.onResume()
        loadTenants()
    }

    private fun loadTenants() {
        RetrofitClient.getInstance(this).getTenants(token).enqueue(object : Callback<TenantListResponse> {
            override fun onResponse(call: Call<TenantListResponse>, response: Response<TenantListResponse>) {
                if (response.isSuccessful && response.body()?.success == true) {
                    val list = response.body()?.data ?: emptyList()
                    adapter.updateData(list)
                }
            }
            override fun onFailure(call: Call<TenantListResponse>, t: Throwable) {
                Toast.makeText(this@TenantListActivity, "Lỗi tải dữ liệu", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun showEndRentConfirmDialog(tenantId: Int) {
        AlertDialog.Builder(this)
            .setTitle("CẢNH BÁO!")
            .setMessage("Bạn có chắc chắn muốn chấm dứt hợp đồng và mời khách thuê này ra khỏi phòng?")
            .setPositiveButton("Đồng ý") { _, _ -> 
                executeEndRent(tenantId) 
            }
            .setNegativeButton("Hủy", null)
            .show()
    }

    private fun executeEndRent(tenantId: Int) {
        RetrofitClient.getInstance(this).endRent(token, tenantId).enqueue(object : Callback<Any> {
            override fun onResponse(call: Call<Any>, response: Response<Any>) {
                if (response.isSuccessful) {
                    Toast.makeText(this@TenantListActivity, "Đã chấm dứt hợp đồng thành công!", Toast.LENGTH_LONG).show()
                    loadTenants() // Tải lại danh sách để cập nhật giao diện
                } else {
                    Toast.makeText(this@TenantListActivity, "Lỗi khi cập nhật", Toast.LENGTH_SHORT).show()
                }
            }
            override fun onFailure(call: Call<Any>, t: Throwable) {
                Toast.makeText(this@TenantListActivity, "Lỗi mạng", Toast.LENGTH_SHORT).show()
            }
        })
    }
}
