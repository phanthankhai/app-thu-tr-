<?php

use Illuminate\Database\Migrations\Migration;
use Illuminate\Database\Schema\Blueprint;
use Illuminate\Support\Facades\Schema;

return new class extends Migration
{
    public function up(): void
    {
        Schema::create('invoices', function (Blueprint $table) {
            $table->id();
            $table->foreignId('room_id')->constrained('rooms')->onDelete('cascade'); // Hóa đơn của phòng nào
            
            $table->string('billing_month'); // Tháng chốt bill (VD: 06/2026)
            
            // Chỉ số điện nước để hệ thống tự trừ và tính tiền
            $table->integer('electricity_old')->default(0); // Số điện cũ
            $table->integer('electricity_new')->default(0); // Số điện mới
            $table->integer('water_old')->default(0);       // Số nước cũ
            $table->integer('water_new')->default(0);       // Số nước mới
            
            $table->decimal('total_amount', 12, 2)->default(0); // Tổng tiền phải thanh toán
            $table->enum('status', ['unpaid', 'paid'])->default('unpaid'); // Trạng thái thanh toán
            
            $table->timestamps();
        });
    }

    public function down(): void
    {
        Schema::dropIfExists('invoices');
    }
};