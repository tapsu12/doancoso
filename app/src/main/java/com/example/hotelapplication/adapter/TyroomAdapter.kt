package com.example.hotelapplication.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.hotelapplication.R
import com.example.hotelapplication.data.TypeRoom
import com.example.hotelapplication.databinding.BookingtypeRvItemBinding
import com.example.hotelapplication.databinding.TypeRvItemBinding

class TyroomAdapter(private val typeRooms: List<TypeRoom>) :
    RecyclerView.Adapter<TyroomAdapter.TyperoomViewHolder>() {
    inner class TyperoomViewHolder(private val binding: BookingtypeRvItemBinding) : RecyclerView.ViewHolder(binding.root)
    {
        fun bind(typeRoom: TypeRoom) {
            binding.apply {
                tvTypeName.text = typeRoom.name
                tvPriceType.text = "${typeRoom.price} VN đồng"
                tvBookingQuantity.text=typeRoom.quantitybooking.toString()
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TyperoomViewHolder {
        return TyperoomViewHolder(
            BookingtypeRvItemBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: TyperoomViewHolder, position: Int) {
        val typeRoom = typeRooms[position]
        holder.bind(typeRoom)
    }

    override fun getItemCount(): Int {
        return typeRooms.size
    }
}






