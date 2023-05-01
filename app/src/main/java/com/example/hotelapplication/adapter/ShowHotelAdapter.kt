package com.example.hotelapplication.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.hotelapplication.data.Hotel
import com.example.hotelapplication.databinding.ItemShowHotelBinding

class ShowHotelAdapter () : RecyclerView.Adapter<ShowHotelAdapter.ViewHolder>() {

    inner class ViewHolder(val binding: ItemShowHotelBinding) : RecyclerView.ViewHolder(binding.root) {
        fun blind(hotel: Hotel) {
            binding.nameht.text = hotel.name
            binding.adressht.text = hotel.address
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
    // Tạo ViewHolder mới
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemShowHotelBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    // Gán giá trị cho ViewHolder
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val hotel=differ.currentList[position]
        holder.blind(hotel)
        holder.itemView.setOnClickListener{
            onClick?.invoke(hotel)
        }
    }

    // Trả về số lượng item trong danh sách
    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    // Interface xử lý sự kiện click
    interface OnItemClickListener {
        fun onItemClick(hotel: Hotel)
    }
    var onClick: ((Hotel) -> Unit)? =null
}
