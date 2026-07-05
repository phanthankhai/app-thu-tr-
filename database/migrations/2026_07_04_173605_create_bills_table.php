<?php

use Illuminate\Database\Migrations\Migration;
use Illuminate\Database\Schema\Blueprint;
use Illuminate\Support\Facades\Schema;

return new class extends Migration
{
    /**
     * Run the migrations.
     */
    public function up()
    {
        Schema::create('bills', function (Blueprint $table) {
            $table->id();
            $table->foreignId('room_id')->constrained('rooms')->onDelete('cascade');
            $table->string('billing_month'); // Ví dụ: "05/2026"
            
            // Chỉ số điện
            $table->integer('old_electric');
            $table->integer('new_electric');
            $table->decimal('electric_price', 10, 2)->default(3500);
            
            // Chỉ số nước
            $table->integer('old_water');
            $table->integer('new_water');
            $table->decimal('water_price', 10, 2)->default(15000);
            
            // Tiền phòng cố định
            $table->decimal('room_price', 15, 2);
            
            // Trạng thái hóa đơn
            $table->enum('status', ['unpaid', 'paid'])->default('unpaid');
            $table->timestamps();
        });
    }

    /**
     * Reverse the migrations.
     */
    public function down(): void
    {
        Schema::dropIfExists('bills');
    }
};