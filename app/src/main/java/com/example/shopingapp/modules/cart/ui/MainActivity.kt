package com.example.shopingapp.modules.cart.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.shopingapp.R
import com.example.shopingapp.databinding.ActivityMainBinding
import com.example.shopingapp.modules.cart.ui.adapter.CartAdapter
import com.example.shopingapp.modules.cart.viewModel.CartViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val viewModel by viewModels<CartViewModel>()
    private lateinit var binding: ActivityMainBinding

    private var currentItem = 0

    private val adapter = CartAdapter({ txt, position ->
        currentItem = position
        viewModel.applyCoupon(txt, position)
    }) { position ->
        currentItem = position
        viewModel.applyCoupon("", position)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        setContentView(binding.root)
        observeData()
        initRecyclerView()
    }


    private fun initRecyclerView() {
        binding.rvCart.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = this@MainActivity.adapter
        }

    }

    private fun observeData() {
        viewModel.cartData.observe(this) {
            adapter.submitList(it)
            adapter.notifyItemChanged(currentItem)
            adapter.notifyItemChanged(it.size - 2)
        }
    }
}