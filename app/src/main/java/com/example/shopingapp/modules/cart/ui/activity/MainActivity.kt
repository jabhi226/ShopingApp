package com.example.shopingapp.modules.cart.ui.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.shopingapp.R
import com.example.shopingapp.databinding.ActivityMainBinding
import com.example.shopingapp.modules.cart.models.CartItem
import com.example.shopingapp.modules.cart.ui.adapter.CartAdapter
import com.example.shopingapp.modules.cart.viewModel.CartViewModel
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val viewModel by viewModels<CartViewModel>()
    private lateinit var binding: ActivityMainBinding

    private val adapter = CartAdapter({ txt, position ->
        viewModel.applyCoupon(txt, position)
    }, { position ->
        viewModel.applyCoupon("", position)
    }) { deleteItem, position ->
        viewModel.deleteItem(deleteItem, position)
        showSnackBar("${(deleteItem as CartItem).name} deleted successfully.")
    }

    private fun showSnackBar(text: String) {
        Snackbar.make(binding.root, text, Snackbar.LENGTH_LONG)
            .setAction("Undo") { viewModel.undoDeletedItem() }
            .show()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        setContentView(binding.root)
        observeData()
        initRecyclerView()
        this.window.statusBarColor = ContextCompat.getColor(this, R.color.nobal_black)
        this.window.navigationBarColor = ContextCompat.getColor(this, R.color.nobal_black)
    }


    private fun initRecyclerView() {
        binding.rvCart.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = this@MainActivity.adapter
        }

    }

    private fun observeData() {
        viewModel.cartData.observe(this) {
            adapter.submitList(it) {
                if (viewModel.totalCount < it.size) {
                    adapter.notifyItemInserted(viewModel.currentItem)
                } else if (viewModel.totalCount == it.size) {
                    adapter.notifyItemChanged(viewModel.currentItem)
                }
                adapter.notifyItemChanged(it.size - 2)
                binding.rvCart.scrollToPosition(viewModel.currentItem)
            }
        }
    }
}