<?php

namespace App\Models;

use Illuminate\Database\Eloquent\Factories\HasFactory;
use Illuminate\Foundation\Auth\User as Authenticatable;
use Illuminate\Notifications\Notifiable;
use PHPOpenSourceSaver\JWTAuth\Contracts\JWTSubject; // Thêm thư viện này

// Bổ sung implements JWTSubject
class User extends Authenticatable implements JWTSubject 
{
    use HasFactory, Notifiable;

    protected $fillable = [
        'name',
        'phone',
        'role',
        'room_id',
        'password',
        'is_first_login',
    ];

    protected $hidden = [
        'password',
        'remember_token',
    ];

    /**
     * Bắt buộc phải có: Lấy trường định danh để sinh JWT (ở đây là id)
     */
    public function getJWTIdentifier()
    {
        return $this->getKey();
    }

    /**
     * Bắt buộc phải có: Khai báo thêm các thông tin muốn đính kèm vào Token
     */
    public function getJWTCustomClaims()
    {
        return [
            'role' => $this->role,
            'room_id' => $this->room_id
        ];
    }
    public function room()
    {
        return $this->belongsTo(Room::class, 'room_id');
    }
}