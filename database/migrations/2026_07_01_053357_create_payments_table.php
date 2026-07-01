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
            // Liên kết với hóa đơn
            $table->foreignId('invoice_id')->constrained('invoices')->onDelete('cascade');
            
            // Thông tin giao dịch
            $table->decimal('amount', 12, 2); // Số tiền đóng lần này
            $table->enum('payment_method', ['cash', 'bank_transfer', 'momo'])->default('cash'); // Hình thức
            $table->string('transaction_code')->nullable(); // Mã giao dịch (nếu chuyển khoản)
            $table->text('notes')->nullable(); // Ghi chú thêm
            
            $table->timestamps();
        });
    }

    public function down(): void
    {
        Schema::dropIfExists('payments');
    }
};