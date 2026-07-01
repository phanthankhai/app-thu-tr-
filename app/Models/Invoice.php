<?php

namespace App\Models;

use Illuminate\Database\Eloquent\Factories\HasFactory;
use Illuminate\Database\Eloquent\Model;

class Invoice extends Model
{
    use HasFactory;
    
    protected $fillable = [
        'room_id', 'billing_month', 
        'electricity_old', 'electricity_new', 
        'water_old', 'water_new', 
        'total_amount', 'status'
    ];

    // 1 Hóa đơn thuộc về 1 Phòng
    public function room()
    {
        return $this->belongsTo(Room::class);
    }
    public function payments()
    {
        return $this->hasMany(Payment::class);
    }
}