<?php

namespace App\Models;

use Illuminate\Database\Eloquent\Factories\HasFactory;
use Illuminate\Database\Eloquent\Model;

class Contract extends Model
{
    use HasFactory;

    protected $fillable = [
        'room_id',
        'tenant_name',
        'tenant_phone',
        'tenant_cccd', // Bổ sung CCCD
        'start_date',
        'end_date',    // Bổ sung ngày hết hạn
        'deposit',
        'status'
    ];
    // Khai báo mối quan hệ: 1 Hợp đồng thuộc về 1 Phòng
    public function room()
    {
        return $this->belongsTo(Room::class);
    }
}