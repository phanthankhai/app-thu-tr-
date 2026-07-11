<?php

use Illuminate\Database\Migrations\Migration;
use Illuminate\Database\Schema\Blueprint;
use Illuminate\Support\Facades\Schema;

return new class extends Migration
{
    public function up(): void
    {
        Schema::create('payments', function (Blueprint $table) {
            $table->id();
            // Liên kết cũ
            $table->foreignId('invoice_id')->nullable()->constrained('invoices')->onDelete('cascade');
            
            // BỔ SUNG THÊM LIÊN KẾT MỚI CHO CHỨC NĂNG CHIA TIỀN
            $table->unsignedBigInteger('bill_id')->nullable(); 
            $table->unsignedBigInteger('user_id')->nullable(); // Để biết ai là người đóng tiền
            $table->string('status')->default('pending'); // Trạng thái: pending, approved
            
            // Thông tin giao dịch (Giữ nguyên của bạn)
            $table->decimal('amount', 12, 2); 
            $table->enum('payment_method', ['cash', 'bank_transfer', 'momo'])->default('cash'); 
            $table->string('transaction_code')->nullable(); 
            $table->text('notes')->nullable(); 
            
            $table->timestamps();
        });
    }

    public function down(): void
    {
        Schema::dropIfExists('payments');
    }
};