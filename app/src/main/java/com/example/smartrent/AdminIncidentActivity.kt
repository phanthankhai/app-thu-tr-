package com.example.smartrent

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AdminIncidentActivity : AppCompatActivity() {

    private lateinit var rvIncidents: RecyclerView
    private lateinit var adapter: IncidentAdapter
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout
    private lateinit var searchView: SearchView
    private var token: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_incident)

        rvIncidents = findViewById(R.id.rvIncidents)
        rvIncidents.layoutManager = LinearLayoutManager(this)
        swipeRefreshLayout = findViewById(R.id.swipeRefreshLayout)
        searchView = findViewById(R.id.searchView)

        val savedToken = getSharedPreferences("SmartRentPrefs", MODE_PRIVATE).getString("AUTH_TOKEN", "") ?: ""
        token = "Bearer $savedToken"

        // Khởi tạo Adapter rỗng trước
        adapter = IncidentAdapter(emptyList()) { incident ->
            // Khi Admin bấm nút Xử lý ở 1 item, hiển thị Dialog chọn trạng thái
            showStatusDialog(incident)
        }
        rvIncidents.adapter = adapter

        // Pull to refresh
        swipeRefreshLayout.setOnRefreshListener {
            loadIncidents()
        }

        // Search logic
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean = false
            override fun onQueryTextChange(newText: String?): Boolean {
                adapter.filter(newText.orEmpty())
                return true
            }
        })

        // Tải danh sách báo cáo
        loadIncidents()
    }

    private fun loadIncidents() {
        RetrofitClient.getInstance(this).getAllIncidents(token).enqueue(object : Callback<IncidentListResponse> {
            override fun onResponse(call: Call<IncidentListResponse>, response: Response<IncidentListResponse>) {
                swipeRefreshLayout.isRefreshing = false
                if (response.isSuccessful && response.body()?.success == true) {
                    val list = response.body()?.data ?: emptyList()
                    adapter.updateData(list)
                }
            }

            override fun onFailure(call: Call<IncidentListResponse>, t: Throwable) {
                swipeRefreshLayout.isRefreshing = false
                Toast.makeText(this@AdminIncidentActivity, "Lỗi mạng", Toast.LENGTH_SHORT).show()
                Log.e("API_ERROR", t.message ?: "Unknown error")
            }
        })
    }

    private fun showStatusDialog(incident: IncidentData) {
        val options = arrayOf("Đang sửa chữa (processing)", "Đã xong (resolved)")
        val statusValues = arrayOf("processing", "resolved")

        AlertDialog.Builder(this)
            .setTitle("Cập nhật trạng thái")
            .setItems(options) { _, which ->
                val selectedStatus = statusValues[which]
                updateStatusApi(incident.id, selectedStatus)
            }
            .setNegativeButton("Hủy", null)
            .show()
    }

    private fun updateStatusApi(incidentId: Int, newStatus: String) {
        val requestBody = mapOf("status" to newStatus)

        RetrofitClient.getInstance(this).updateIncidentStatus(token, incidentId, requestBody)
            .enqueue(object : Callback<StatusResponse> {
                override fun onResponse(call: Call<StatusResponse>, response: Response<StatusResponse>) {
                    if (response.isSuccessful) {
                        Toast.makeText(this@AdminIncidentActivity, "Cập nhật thành công!", Toast.LENGTH_SHORT).show()
                        loadIncidents() // Tải lại danh sách để màu sắc tự cập nhật
                    } else {
                        Toast.makeText(this@AdminIncidentActivity, "Lỗi cập nhật", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<StatusResponse>, t: Throwable) {
                    Toast.makeText(this@AdminIncidentActivity, "Mất kết nối mạng", Toast.LENGTH_SHORT).show()
                }
            })
    }
}
