package com.example.shopingapp.modules.cart.repository

import android.content.Context
import com.example.shopingapp.modules.cart.models.Cart
import dagger.hilt.android.qualifiers.ApplicationContext

interface CartRepository {

    fun getCartData(): ArrayList<Cart>

}