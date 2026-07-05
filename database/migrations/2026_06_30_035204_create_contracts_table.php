<?php

use Illuminate\Database\Migrations\Migration;
use Illuminate\Database\Schema\Blueprint;
use Illuminate\Support\Facades\Schema;

return new class extends Migration
{
    public function up()
{
    Schema::create('contracts', function (Blueprint $table) {
        $table->id();
        $table->foreignId('room_id')->constrained('rooms')->onDelete('cascade');
        $table->string('tenant_name'); // Tên người thuê
        $table->string('tenant_phone'); // Số điện thoại
        $table->string('tenant_cccd'); // Số Căn cước công dân
        $table->date('start_date'); // Ngày bắt đầu thuê
        $table->date('end_date')->nullable(); // Ngày hết hạn (có thể để trống nếu thuê vô thời hạn)
        $table->decimal('deposit', 15, 2)->default(0); // Tiền cọc
        $table->enum('status', ['active', 'expired', 'cancelled'])->default('active'); // Trạng thái HĐ
        $table->timestamps();
    });
}

    public function down(): void
    {
        Schema::dropIfExists('contracts');
    }
};