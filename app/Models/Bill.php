<?php

namespace App\Models;

use Illuminate\Database\Eloquent\Model;

class Bill extends Model
{
    protected $fillable = [
        'room_id', 'billing_month', 
        'old_electric', 'new_electric', 'electric_price',
        'old_water', 'new_water', 'water_price', 
        'room_price', 'status'
    ];

    // Tự động nhét thêm 2 trường tính toán động này vào chuỗi JSON trả về Frontend
    protected $appends = ['services_cost', 'total_amount'];

    // Mối quan hệ: Hóa đơn thuộc về một phòng trọ
    public function room()
    {
        return $this->belongsTo(Room::class);
    }

    // Bộ lọc tự động tính tổng tiền dịch vụ cố định của phòng tại thời điểm truy vấn
    public function getServicesCostAttribute()
    {
        return $this->room ? $this->room->services->sum('price') : 0;
    }

    // Bộ não tự động cộng dồn: Tiền phòng + Điện + Nước + Tổng dịch vụ đăng ký
    public function getTotalAmountAttribute()
    {
        $electricCost = ($this->new_electric - $this->old_electric) * $this->electric_price;
        $waterCost = ($this->new_water - $this->old_water) * $this->water_price;
        
        return $this->room_price + $electricCost + $waterCost + $this->services_cost;
    }
}