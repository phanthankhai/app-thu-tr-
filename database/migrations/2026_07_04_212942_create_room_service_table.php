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
    Schema::create('room_service', function (Blueprint $table) {
        $table->id();
        // Liên kết với bảng rooms
        $table->foreignId('room_id')->constrained()->onDelete('cascade');
        // Liên kết với bảng services
        $table->foreignId('service_id')->constrained()->onDelete('cascade');
        $table->timestamps();
    });
}

    /**
     * Reverse the migrations.
     */
    public function down(): void
    {
        Schema::dropIfExists('room_service');
    }
};
