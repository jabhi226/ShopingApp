package com.example.shopingapp.modules.cart.ui.adapter

import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.shopingapp.R
import com.example.shopingapp.databinding.ItemCalculationBinding
import com.example.shopingapp.databinding.ItemCardHeaderBinding
import com.example.shopingapp.databinding.ItemCardItemBinding
import com.example.shopingapp.databinding.ItemCardProceedToCheckBinding
import com.example.shopingapp.modules.cart.models.Cart
import com.example.shopingapp.modules.cart.models.CartCalculation
import com.example.shopingapp.modules.cart.models.CartItem
import com.example.shopingapp.modules.utils.Utils


class CartAdapter(
    private val applyCoupon: (String, Int) -> Unit,
    private val removeCoupon: (Int) -> Unit,
    private val deleteItem: (Cart, Int) -> Unit,
) :
    ListAdapter<Cart, RecyclerView.ViewHolder>(object : DiffUtil.ItemCallback<Cart>() {
        override fun areItemsTheSame(oldItem: Cart, newItem: Cart): Boolean {

            return if (oldItem is CartItem && newItem is CartItem) {
                (oldItem.id == newItem.id
                        && oldItem.sellingPrice == newItem.sellingPrice
                        && oldItem.isCouponApplied == newItem.isCouponApplied
                        && oldItem.isShowRedeemCoupon == newItem.isShowRedeemCoupon
                        && oldItem.couponText == newItem.couponText)
            } else if (oldItem is CartCalculation && newItem is CartCalculation) {
                (oldItem.price == newItem.price
                        && oldItem.couponDiscount == newItem.couponDiscount
                        && oldItem.tax == newItem.tax
                        && oldItem.total == newItem.total)
            } else {
                oldItem.viewType == newItem.viewType
            }
        }

        override fun areContentsTheSame(oldItem: Cart, newItem: Cart): Boolean {
            return oldItem.equals(newItem)
        }

    }) {

    companion object {
        const val CART_HEADER = 101
        const val CART_ITEM = 102
        const val CART_CALCULATION = 103
        const val CART_PROCEED_TO_CHECK = 104


        @JvmStatic
        @BindingAdapter("android:src")
        fun ImageView.setImageSrc(src: Int) {
            this.setImageDrawable(ContextCompat.getDrawable(this.context, src))
        }
    }

    override fun getItemViewType(position: Int): Int {
        return getItem(position).viewType
    }

    inner class CartHeaderViewHolder(binding: ItemCardHeaderBinding) :
        RecyclerView.ViewHolder(binding.root)

    inner class CartItemViewHolder(private val binding: ItemCardItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun binding(item: Cart?) {
            binding.cartItem = item as CartItem?
            binding.clickEvents = View.OnClickListener {
                if (it.id == binding.tvApplyCoupon.id) {
                    binding.tvApplyCoupon.visibility = View.GONE
                    binding.textInputLayout.visibility = View.VISIBLE
                    binding.textInputLayout.editText?.let { it1 -> Utils.showKeyBoard(it1) }
                } else if (it.id == binding.ivDelete.id) {
                    item?.let { it1 -> deleteItem(it1, adapterPosition) }
                }
            }
            binding.textInputLayout.setEndIconOnClickListener {
                if (item?.isCouponApplied == true) {
                    removeCoupon(adapterPosition)
                    binding.textInputLayout.editText?.let { it1 -> Utils.hideKeyBoard(it1) }
                } else {
                    applyCoupon(binding.etCoupon.text.toString(), adapterPosition)
                    binding.textInputLayout.editText?.let { it1 -> Utils.hideKeyBoard(it1) }
                }
            }

            binding.tvMrpPreDiscount.paintFlags =
                binding.tvMrpPreDiscount.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
        }
    }

    inner class CartCalculationViewHolder(private val binding: ItemCalculationBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bindData(item: Cart?) {
            binding.cartCalculation = item as CartCalculation?
        }

    }

    inner class CartProceedViewHolder(binding: ItemCardProceedToCheckBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        when (viewType) {
            CART_HEADER -> {
                return CartHeaderViewHolder(
                    DataBindingUtil.inflate(
                        LayoutInflater.from(parent.context),
                        R.layout.item_card_header,
                        parent,
                        false
                    )
                )
            }

            CART_ITEM -> {
                return CartItemViewHolder(
                    DataBindingUtil.inflate(
                        LayoutInflater.from(parent.context),
                        R.layout.item_card_item,
                        parent,
                        false
                    )
                )
            }

            CART_CALCULATION -> {
                return CartCalculationViewHolder(
                    DataBindingUtil.inflate(
                        LayoutInflater.from(parent.context),
                        R.layout.item_calculation,
                        parent,
                        false
                    )
                )
            }

            CART_PROCEED_TO_CHECK -> {
                return CartProceedViewHolder(
                    DataBindingUtil.inflate(
                        LayoutInflater.from(parent.context),
                        R.layout.item_card_proceed_to_check,
                        parent,
                        false
                    )
                )
            }

            else -> return CartHeaderViewHolder(
                DataBindingUtil.inflate(
                    LayoutInflater.from(parent.context),
                    R.layout.item_card_header,
                    parent,
                    false
                )
            )
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is CartHeaderViewHolder -> {
            }

            is CartItemViewHolder -> {
                holder.binding(getItem(position))
            }

            is CartCalculationViewHolder -> {
                holder.bindData(getItem(position))
            }

            is CartProceedViewHolder -> {}
        }
    }


    override fun submitList(list: List<Cart>?) {
        super.submitList(if (list == this.currentList) ArrayList<Cart>(list) else list)
    }
}