package com.example.shopingapp.modules.cart.repository

import android.content.Context
import androidx.core.content.ContextCompat
import com.example.shopingapp.R
import com.example.shopingapp.modules.cart.models.Cart
import com.example.shopingapp.modules.cart.models.CartCalculation
import com.example.shopingapp.modules.cart.models.CartHeader
import com.example.shopingapp.modules.cart.models.CartItem
import com.example.shopingapp.modules.cart.models.CartProceed
import com.example.shopingapp.modules.cart.ui.adapter.CartAdapter

class CartRepositoryImpl : CartRepository {


    override fun getCartData(): ArrayList<Cart> {
        val list = arrayListOf<Cart>()
        list.add(CartHeader(CartAdapter.CART_HEADER))
        var price = 0
        for (i in 0 until 3) {
            val mrp = (i + 1) * 1000
            list.add(
                CartItem(
                    CartAdapter.CART_ITEM,
                    i,
                    R.drawable.iv_cart_item_image,
                    "Burberry T-shirt",
                    mrp,
                    mrp
                )
            )
            price += mrp * 2
        }
        list.add(
            CartCalculation(
                CartAdapter.CART_CALCULATION,
                price.toDouble(),
                0.0,
                0.0,
                (price.toDouble() * 18 / 100),
                (price + (price.toDouble() * 18 / 100))
            )
        )
        list.add(
            CartProceed(
                CartAdapter.CART_PROCEED_TO_CHECK,
            )
        )
        return list
    }

}