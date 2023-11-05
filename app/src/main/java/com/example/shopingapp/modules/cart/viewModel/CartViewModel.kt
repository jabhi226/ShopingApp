package com.example.shopingapp.modules.cart.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.shopingapp.modules.cart.models.Cart
import com.example.shopingapp.modules.cart.models.CartCalculation
import com.example.shopingapp.modules.cart.models.CartItem
import com.example.shopingapp.modules.cart.repository.CartRepository
import com.example.shopingapp.modules.cart.ui.adapter.CartAdapter
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class CartViewModel @Inject constructor(
    private val repository: CartRepository,
) : ViewModel() {

    private val _cartData = MutableLiveData<ArrayList<Cart>>()
    val cartData = _cartData as LiveData<ArrayList<Cart>>

    init {
        getCardData()
    }

    private fun getCardData() {
        _cartData.value = repository.getCartData()
    }

    fun applyCoupon(txt: String, position: Int) {
        val list = _cartData.value
        var mrp = 0
        var coupon = 0.0
        var total = 0.0
//        var tax = 0.0

        val l = list?.mapIndexed { index, cart ->
            if (cart is CartItem) {
                if (index == position) {
                    cart.isCouponApplied = isRemoveCoupon(txt)
                    cart.isShowRedeemCoupon = isRemoveCoupon(txt)
                    cart.couponText = txt
                    cart.sellingPrice = if (isRemoveCoupon(txt)) {
                        cart.mrp / 2
                    } else {
                        cart.mrp
                    }
                }
                mrp += (cart.mrp * cart.qty)
                coupon += ((cart.mrp - cart.sellingPrice) * cart.qty)
                total += (cart.sellingPrice * cart.qty)
            } else if (cart is CartCalculation) {
                cart.price = mrp.toDouble()
                cart.couponDiscount = coupon
                cart.total = total
            }
            cart
        } as ArrayList<Cart>
        _cartData.value = l
    }

    private fun isRemoveCoupon(txt: String): Boolean {
        return txt.isNotEmpty()
    }

    fun deleteItem(deleteItem: Cart) {
        var mrp = 0
        var coupon = 0.0
        var total = 0.0
        _cartData.value =
            _cartData.value?.filterIndexed { index, cart ->
                if (cart is CartItem) {
                    if (
                        deleteItem is CartItem
                        && deleteItem.id != cart.id
                    ) {
                        mrp += (cart.mrp * cart.qty)
                        coupon += ((cart.mrp - cart.sellingPrice) * cart.qty)
                        total += (cart.sellingPrice * cart.qty)
                        true
                    } else {
                        false
                    }
                } else if (cart is CartCalculation) {
                    cart.price = mrp.toDouble()
                    cart.couponDiscount = coupon
                    cart.total = total
                    true
                } else {
                    true
                }
            } as ArrayList<Cart>
    }

}