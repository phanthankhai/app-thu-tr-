package com.example.smartrent

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody

class AddRoomActivity : AppCompatActivity() {

    // Biến để lưu URI của ảnh được người dùng chọn
    private var imageUri: Uri? = null

    // Ánh xạ các chế độ xem
    private lateinit var ivSelectRoomImage: ImageView
    private lateinit var etRoomName: EditText
    private lateinit var etRoomPrice: EditText
    private lateinit var etRoomArea: EditText
    private lateinit var etRoomDescription: EditText

    // Đăng ký ActivityResultLauncher để mở trình chọn ảnh (Launcher hiện đại)
    private val pickImageLauncher: ActivityResultLauncher<Intent> =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                // Nếu người dùng chọn một hình ảnh thành công
                result.data?.data?.let { uri ->
                    imageUri = uri
                    // Hiển thị hình ảnh được chọn lên ImageView (Nơi đang hiện hình gallery xám)
                    ivSelectRoomImage.setImageURI(imageUri)
                }
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_room)

        // 1. Ánh xạ các thành phần giao diện (findViewById)
        ivSelectRoomImage = findViewById(R.id.ivSelectRoomImage)
        val btnChooseImage = findViewById<Button>(R.id.btnChooseImage)
        etRoomName = findViewById(R.id.etRoomName)
        etRoomPrice = findViewById(R.id.etRoomPrice)
        etRoomArea = findViewById(R.id.etRoomArea)
        etRoomDescription = findViewById(R.id.etRoomDescription)
        val btnSaveRoom = findViewById<Button>(R.id.btnSaveRoom)
        val btnBack = findViewById<TextView>(R.id.btnBack)

        // 2. Lắng nghe sự kiện nút Hủy và Quay lại
        btnBack.setOnClickListener {
            finish() // Đóng màn hình Thêm phòng và quay về Home
        }

        // 3. Lắng nghe sự kiện nút Chọn ảnh phòng (Sửa lỗi bạn đang gặp!)
        btnChooseImage.setOnClickListener {
            openImagePicker() // Gọi hàm mở thư viện ảnh
        }

        // 4. Lắng nghe sự kiện nút Lưu phòng trọ
        btnSaveRoom.setOnClickListener {
            val name = etRoomName.text.toString()
            val price = etRoomPrice.text.toString()
            val area = etRoomArea.text.toString()
            val description = etRoomDescription.text.toString()

            // Kiểm tra không cho để trống tên và giá
            if (name.isEmpty() || price.isEmpty()) {
                Toast.makeText(this, "Vui lòng nhập Tên và Giá phòng!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            Toast.makeText(this, "Đang lưu dữ liệu...", Toast.LENGTH_SHORT).show()

            // Ép kiểu các chữ text thành RequestBody (chuẩn của Multipart)
            val nameReq = name.toRequestBody("text/plain".toMediaTypeOrNull())
            val priceReq = price.toRequestBody("text/plain".toMediaTypeOrNull())
            val areaReq = area.toRequestBody("text/plain".toMediaTypeOrNull())
            val statusReq = "empty".toRequestBody("text/plain".toMediaTypeOrNull())
            val descReq = description.toRequestBody("text/plain".toMediaTypeOrNull())

            // Ép kiểu File ảnh thành MultipartBody.Part
            var imagePart: okhttp3.MultipartBody.Part? = null
            if (imageUri != null) {
                val file = getFileFromUri(imageUri!!)
                if (file != null) {
                    val reqFile = file.asRequestBody("image/*".toMediaTypeOrNull())
                    imagePart = okhttp3.MultipartBody.Part.createFormData("image", file.name, reqFile)
                }
            }

            // Lấy Token từ SharedPreferences
            val sharedPreferences = getSharedPreferences("SmartRentPrefs", MODE_PRIVATE)
            val token = sharedPreferences.getString("AUTH_TOKEN", "") ?: ""

            if (token.isEmpty()) {
                Toast.makeText(this, "Bạn cần đăng nhập lại!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Gọi API gửi lên Laravel
            RetrofitClient.getInstance(this).addRoom(token, nameReq, priceReq, areaReq, statusReq, descReq, imagePart)
                .enqueue(object : retrofit2.Callback<RoomDetailResponse> { // Chú ý đổi model nhận tùy theo cách bạn định nghĩa ở ApiService
                    override fun onResponse(call: retrofit2.Call<RoomDetailResponse>, response: retrofit2.Response<RoomDetailResponse>) {
                        if (response.isSuccessful) {
                            Toast.makeText(this@AddRoomActivity, "Thêm phòng thành công!", Toast.LENGTH_LONG).show()
                            finish() // Đóng màn hình, tự động lùi về Home
                        } else {
                            Toast.makeText(this@AddRoomActivity, "Lỗi: Khởi tạo thất bại", Toast.LENGTH_SHORT).show()
                        }
                    }

                    override fun onFailure(call: retrofit2.Call<RoomDetailResponse>, t: Throwable) {
                        Toast.makeText(this@AddRoomActivity, "Lỗi mạng: ${t.message}", Toast.LENGTH_SHORT).show()
                    }
                })
        }
    }

    // Hàm để tạo Intent mở trình chọn ảnh từ bộ nhớ máy
    private fun openImagePicker() {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "image/*" // Chỉ hiển thị các file là hình ảnh
        pickImageLauncher.launch(intent)
    }

    // Hàm copy ảnh từ URI vào bộ nhớ tạm của App để biến thành File vật lý gửi đi
    private fun getFileFromUri(uri: Uri): java.io.File? {
        val tempFile = java.io.File(cacheDir, "temp_room_image_${System.currentTimeMillis()}.jpg")
        return try {
            val inputStream = contentResolver.openInputStream(uri)
            val outputStream = java.io.FileOutputStream(tempFile)
            inputStream?.copyTo(outputStream)
            inputStream?.close()
            outputStream.close()
            tempFile
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}
