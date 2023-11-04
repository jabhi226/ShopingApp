package com.example.shopingapp.di

import android.content.Context
import com.example.shopingapp.modules.cart.repository.CartRepository
import com.example.shopingapp.modules.cart.repository.CartRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ViewModelScoped

@InstallIn(ViewModelComponent::class)
@Module
class CartModule {


    @Provides
    @ViewModelScoped
    fun provideCartRepository(
        @ApplicationContext context: Context
    ): CartRepository {
        return CartRepositoryImpl()
    }
}