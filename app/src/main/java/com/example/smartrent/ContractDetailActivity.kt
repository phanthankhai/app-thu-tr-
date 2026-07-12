package com.example.smartrent

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ContractDetailActivity : AppCompatActivity() {

    private lateinit var btnTerminateContract: Button
    private lateinit var tvContractInfoDisplay: TextView
    
    private var contractId: Int = -1
    private var currentUserRole: String = ""
    private var currentContractStatus: String = ""
    private var authToken: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_contract_detail)

        btnTerminateContract = findViewById(R.id.btnTerminateContract)
        tvContractInfoDisplay = findViewById(R.id.tvContractInfoDisplay)

        // Lấy dữ liệu từ Intent
        contractId = intent.getIntExtra("CONTRACT_ID", -1)
        currentUserRole = intent.getStringExtra("USER_ROLE") ?: ""
        currentContractStatus = intent.getStringExtra("CONTRACT_STATUS") ?: ""
        
        val sharedPrefs = getSharedPreferences("SmartRentPrefs", MODE_PRIVATE)
        authToken = "Bearer " + (sharedPrefs.getString("AUTH_TOKEN", "") ?: "")

        // Hiển thị thông tin tạm thời (có thể mở rộng lấy chi tiết từ API)
        tvContractInfoDisplay.text = "ID Hợp đồng: $contractId\nTrạng thái: $currentContractStatus"

        setupTerminationButton()
    }

    private fun setupTerminationButton() {
        if (currentUserRole == "admin" && currentContractStatus == "active") {
            btnTerminateContract.visibility = View.VISIBLE
            btnTerminateContract.text = "Yêu cầu thanh lý hợp đồng"
            btnTerminateContract.setOnClickListener { callApiRequestTerminate() }
            
        } else if (currentUserRole == "tenant" && currentContractStatus == "pending_termination") {
            btnTerminateContract.visibility = View.VISIBLE
            btnTerminateContract.text = "Xác nhận thanh lý (Đồng ý)"
            btnTerminateContract.setOnClickListener { callApiConfirmTerminate() }
            
        } else {
            btnTerminateContract.visibility = View.GONE
        }
    }

    private fun callApiRequestTerminate() {
        btnTerminateContract.isEnabled = false

        RetrofitClient.getInstance(this).requestTerminateContract(authToken, contractId)
            .enqueue(object : Callback<JsonObject> {
                override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                    btnTerminateContract.isEnabled = true
                    if (response.isSuccessful && response.body() != null) {
                        val msg = response.body()?.get("message")?.asString ?: "Đã gửi yêu cầu"
                        Toast.makeText(this@ContractDetailActivity, msg, Toast.LENGTH_LONG).show()
                        
                        currentContractStatus = "pending_termination"
                        tvContractInfoDisplay.text = "ID Hợp đồng: $contractId\nTrạng thái: $currentContractStatus"
                        setupTerminationButton()
                    } else {
                        Toast.makeText(this@ContractDetailActivity, "Lỗi: Không thể gửi yêu cầu", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                    btnTerminateContract.isEnabled = true
                    Toast.makeText(this@ContractDetailActivity, "Lỗi mạng: ${t.message}", Toast.LENGTH_SHORT).show()
                }
            })
    }

    private fun callApiConfirmTerminate() {
        btnTerminateContract.isEnabled = false

        RetrofitClient.getInstance(this).confirmTerminateContract(authToken, contractId)
            .enqueue(object : Callback<JsonObject> {
                override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                    btnTerminateContract.isEnabled = true
                    if (response.isSuccessful && response.body() != null) {
                        val msg = response.body()?.get("message")?.asString ?: "Đã xác nhận"
                        val deposit = response.body()?.get("deposit_to_refund")?.asString ?: "0"
                        
                        Toast.makeText(this@ContractDetailActivity, "$msg\nTiền cọc hoàn lại: $deposit", Toast.LENGTH_LONG).show()
                        
                        btnTerminateContract.visibility = View.GONE
                        currentContractStatus = "terminated"
                        tvContractInfoDisplay.text = "ID Hợp đồng: $contractId\nTrạng thái: $currentContractStatus"
                    } else {
                        Toast.makeText(this@ContractDetailActivity, "Lỗi: Không thể xác nhận", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                    btnTerminateContract.isEnabled = true
                    Toast.makeText(this@ContractDetailActivity, "Lỗi mạng: ${t.message}", Toast.LENGTH_SHORT).show()
                }
            })
    }
}
