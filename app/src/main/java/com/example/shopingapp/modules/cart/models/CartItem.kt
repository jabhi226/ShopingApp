package com.example.shopingapp.modules.cart.models

import android.graphics.drawable.Drawable
import androidx.databinding.BaseObservable
import androidx.databinding.ObservableField
import androidx.lifecycle.MutableLiveData

data class CartItem(
    override val viewType: Int,
    val id: Int,
    val image: Int,
    val name: String,
    var mrp: Int,
    var sellingPrice: Int,
    val size: SizeEnum = SizeEnum.Medium,
    var qty: Int = 2,
    val storeName: String = "Blueberry store",
    val deliveryEtaMin: Int = 5,
    val deliveryEtaMax: Int = 7,
    var isCouponApplied: Boolean = (false),
    var isShowRedeemCoupon: Boolean = (false),
    var couponText: String = (""),
) :
    Cart(viewType) {

    enum class SizeEnum {
        Small,
        Medium,
        Large
    }

    fun getMrpAmount(): Int {
        return mrp * qty
    }

    fun getCouponAmount(): Int {
        return (mrp - sellingPrice) * qty
    }

    fun getTotalAmount(): Int {
        return (sellingPrice * qty)
    }

    fun getTaxAmount(): Int {
        return ((sellingPrice * 18 / 100) * qty)
    }

}
