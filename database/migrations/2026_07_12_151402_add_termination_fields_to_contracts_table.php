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
    Schema::table('contracts', function (Blueprint $table) {
        // Thêm 2 cột đánh dấu trạng thái duyệt
        $table->boolean('admin_approved')->default(false)->after('status');
        $table->boolean('tenant_approved')->default(false)->after('admin_approved');
    });
}

    /**
     * Reverse the migrations.
     */
    public function down(): void
    {
        Schema::table('contracts', function (Blueprint $table) {
            //
        });
    }
};
