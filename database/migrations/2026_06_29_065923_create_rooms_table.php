<?php

use Illuminate\Database\Migrations\Migration;
use Illuminate\Database\Schema\Blueprint;
use Illuminate\Support\Facades\Schema;

return new class extends Migration
{
    /**
     * Run the migrations.
     */
    public function up(): void
    {
        Schema::create('rooms', function (Blueprint $table) {
            $table->id();
            $table->string('name'); // Tên phòng: A1, B2...
            $table->decimal('price', 12, 2); // Giá tiền
            $table->integer('area')->nullable(); // Diện tích
            $table->enum('status', ['empty', 'occupied', 'maintenance'])->default('empty'); // Trạng thái
            $table->text('description')->nullable();
            $table->timestamps();
        });
    }

    /**
     * Reverse the migrations.
     */
    public function down(): void
    {
        Schema::dropIfExists('rooms');
    }
};