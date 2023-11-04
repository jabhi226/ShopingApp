package com.example.shopingapp.modules.cart.models

data class CartCalculation(
    override val viewType: Int,
    var price: Double,
    var deliveryCharge: Double,
    var couponDiscount: Double,
    var tax: Double,
    var total: Double
) : Cart(viewType) {
}