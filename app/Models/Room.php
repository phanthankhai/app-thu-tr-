<?php

namespace App\Models;

use Illuminate\Database\Eloquent\Model;

class Room extends Model
{
    protected $fillable = [
        'name', 'price', 'area', 'status', 'description','image'
    ];
    public function services()
    {
        return $this->belongsToMany(Service::class, 'room_service');
    }
    public function bills()
    {
        return $this->hasMany(Bill::class, 'room_id'); // Đảm bảo 'room_id' là khóa ngoại trong bảng bills của bạn
    }

    public function users()
    {
        return $this->hasMany(User::class, 'room_id', 'id');
    }

    public function contracts()
    {
        return $this->hasMany(Contract::class);
    }
    
}
