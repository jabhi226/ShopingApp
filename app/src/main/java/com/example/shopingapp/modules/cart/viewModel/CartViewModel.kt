package com.example.shopingapp.modules.cart.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.shopingapp.modules.cart.models.Cart
import com.example.shopingapp.modules.cart.models.CartCalculation
import com.example.shopingapp.modules.cart.models.CartItem
import com.example.shopingapp.modules.cart.repository.CartRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class CartViewModel @Inject constructor(
    private val repository: CartRepository,
) : ViewModel() {

    private val _cartData = MutableLiveData<ArrayList<Cart>>()
    val cartData = _cartData as LiveData<ArrayList<Cart>>

    private var deletedItem: Pair<CartItem, Int>? = null

    var currentItem = 0
    var totalCount = 0

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
        var tax = 0.0

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
                mrp += cart.getMrpAmount()
                coupon += cart.getCouponAmount()
                total += cart.getTotalAmount()
                tax += cart.getTaxAmount()
            } else if (cart is CartCalculation) {
                cart.price = mrp.toDouble()
                cart.couponDiscount = coupon
                cart.tax = tax
                cart.total = total + tax
            }
            cart
        } as ArrayList<Cart>
        _cartData.value = l
        currentItem = position
        totalCount = _cartData.value?.size ?: 0
    }

    private fun isRemoveCoupon(txt: String): Boolean {
        return txt.isNotEmpty()
    }

    fun deleteItem(deleteItem: Cart, position: Int) {
        totalCount = _cartData.value?.size ?: 0
        var mrp = 0
        var coupon = 0.0
        var total = 0.0
        var tax = 0.0
        _cartData.value = _cartData.value?.filterIndexed { index, cart ->
            if (cart is CartItem) {
                if (
                    deleteItem is CartItem
                    && deleteItem.id != cart.id
                ) {
                    mrp += cart.getMrpAmount()
                    coupon += cart.getCouponAmount()
                    total += cart.getTotalAmount()
                    tax += cart.getTaxAmount()
                    true
                } else {
                    deletedItem = Pair(cart, index)
                    false
                }
            } else if (cart is CartCalculation) {
                cart.price = mrp.toDouble()
                cart.couponDiscount = coupon
                cart.tax = tax
                cart.total = total + tax
                true
            } else {
                true
            }
        } as ArrayList<Cart>
        currentItem = deletedItem?.second ?: 0
    }

    fun undoDeletedItem() {
        totalCount = _cartData.value?.size ?: 0

        _cartData.value = deletedItem?.let {
            val l = _cartData.value
            l?.add(it.second, it.first)
            l?.filterIndexed { index, cart ->
                if (cart is CartCalculation) {
                    cart.price += it.first.getMrpAmount()
                    cart.couponDiscount += it.first.getCouponAmount()
                    cart.tax += it.first.getTaxAmount()
                    cart.total += it.first.getTotalAmount() + it.first.getTaxAmount()
                }
                true
            } as ArrayList<Cart>
        }
        currentItem = deletedItem?.second ?: 0
    }


}