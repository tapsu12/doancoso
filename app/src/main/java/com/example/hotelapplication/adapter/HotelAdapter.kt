package com.example.hotelapplication.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.hotelapplication.data.Hotel
import com.example.hotelapplication.databinding.HotelItemBinding


class HotelAdapter:RecyclerView.Adapter<HotelAdapter.HotelsViewHolder>() {
    inner class HotelsViewHolder(private var binding:HotelItemBinding ): RecyclerView.ViewHolder(binding.root){

        fun blind(hotel: Hotel){
            binding.apply{

                Glide.with(itemView).load(hotel.images[0]).into(imgHotel)
//                product.offerPercentage?.let {
//                    val remainingPricePercentage=1f-it
//                    val priceAterOffer= remainingPricePercentage*product.price
//                    tvNewPrice.text="$ ${String.format("%.2f",priceAterOffer)}"
//                }
                tvPrice.text="${hotel.price} VND/Ngày"
                tvHotelName.text=hotel.name
                tvAddress.text=hotel.address
            }

        }
    }
    private val diffCallback= object: DiffUtil.ItemCallback<Hotel>(){
        override fun areItemsTheSame(oldItem: Hotel, newItem: Hotel): Boolean {
            return oldItem.id==newItem.id
        }

        override fun areContentsTheSame(oldItem: Hotel, newItem:Hotel): Boolean {
            return oldItem ==newItem
        }
    }
    val differ= AsyncListDiffer(this,diffCallback)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HotelsViewHolder {
        return HotelsViewHolder(
            HotelItemBinding.inflate(
                LayoutInflater.from(parent.context),parent,false
            )
        )
    }

    override fun onBindViewHolder(holder: HotelsViewHolder, position: Int) {
        val hotel=differ.currentList[position]
        holder.blind(hotel)
        holder.itemView.setOnClickListener{
            onClick?.invoke(hotel)
        }
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }
    var onClick: ((Hotel) -> Unit)? =null
}