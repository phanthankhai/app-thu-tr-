<?php

namespace App\Models;

use Illuminate\Database\Eloquent\Factories\HasFactory;
use Illuminate\Database\Eloquent\Model;

class Payment extends Model
{
    use HasFactory;

    protected $fillable = [
        'invoice_id',
        'bill_id',      // Thêm mới
        'user_id',      // Thêm mới
        'status',       // Thêm mới
        'amount',
        'payment_method',
        'transaction_code',
        'notes'
    ];
    
    public function invoice()
    {
        return $this->belongsTo(Invoice::class);
    }
    public function user()
{
    return $this->belongsTo(User::class, 'user_id');
}

public function bill()
{
    return $this->belongsTo(Bill::class, 'bill_id');
}
}