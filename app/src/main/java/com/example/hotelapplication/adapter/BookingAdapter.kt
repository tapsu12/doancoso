package com.example.hotelapplication.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.hotelapplication.data.Booking
import com.example.hotelapplication.databinding.OrderItemBinding
import java.text.SimpleDateFormat
import java.util.*

class BookingAdapter:RecyclerView.Adapter<BookingAdapter.BookingsViewHolder>() {
    inner class BookingsViewHolder(private var binding: OrderItemBinding): RecyclerView.ViewHolder(binding.root){

        fun blind(booking: Booking){
            binding.apply{
                tvOrderId.text=booking.namehotel
                val date = booking.date
                val formattedDate = SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault()).format(date)
                tvOrderDate.text=formattedDate
            }

        }
    }
    private val diffCallback= object: DiffUtil.ItemCallback<Booking>(){
        override fun areItemsTheSame(oldItem: Booking, newItem: Booking): Boolean {
            return oldItem.idbooking==newItem.idbooking
        }

        override fun areContentsTheSame(oldItem: Booking, newItem: Booking): Boolean {
            return oldItem==newItem
        }
    }
    val differ= AsyncListDiffer(this,diffCallback)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookingsViewHolder {
        return BookingsViewHolder(
            OrderItemBinding.inflate(
                LayoutInflater.from(parent.context),parent,false
            )
        )
    }

    override fun onBindViewHolder(holder: BookingsViewHolder, position: Int) {
        val booking=differ.currentList[position]
        holder.blind(booking)
        holder.itemView.setOnClickListener{
            onClick?.invoke(booking)
        }
    }

    override fun getItemCount(): Int {
        return differ.currentList.size

    }
    var onClick: ((Booking) -> Unit)? =null
}