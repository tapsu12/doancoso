package com.example.hotelapplication.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.hotelapplication.data.TypeRoom
import com.example.hotelapplication.databinding.TypeRvItemBinding
class TypeRoomAdapter: RecyclerView.Adapter<TypeRoomAdapter.TypeRoomViewHolder>() {
    private var selectedPosition = -1

    inner class TypeRoomViewHolder(private val binding:TypeRvItemBinding) : RecyclerView.ViewHolder(binding.root)
    {
        fun bind(typeroom: TypeRoom, position: Int){
            binding.tvnameroom.text = typeroom.name
            binding.tvdescriptionType.text=typeroom.description
            binding.tvpricetype.text="${typeroom.price} VND/ngày"
            if (position == selectedPosition){// size is selected
                binding.apply {
                    check.visibility = View.VISIBLE

                }
            } else{ // size not is selected
                binding.apply {
                    check.visibility = View.INVISIBLE

                }
            }


        }
    }
    // diffCallback là một đối tượng để so sánh hai mục khác nhau trong danh sách các size.
    private val diffCallback = object : DiffUtil.ItemCallback<TypeRoom>(){
        //Đối tượng này cần phải thực hiện hai phương thức areItemsTheSame và areContentsTheSame.
        override fun areItemsTheSame(oldItem: TypeRoom, newItem: TypeRoom): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: TypeRoom, newItem: TypeRoom): Boolean {
            return oldItem == newItem

        }

    }
    val differ = AsyncListDiffer<TypeRoom>(this, diffCallback)
    // AsyncListDiffer để quản lý danh sách các loại phòng và cung cấp các hàm cập nhật danh sách.
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TypeRoomViewHolder {
        return TypeRoomViewHolder(
            TypeRvItemBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    override fun onBindViewHolder(holder: TypeRoomViewHolder, position: Int) {
        val typeroom = differ.currentList[position]

        holder.bind(typeroom, position)

        holder.itemView.setOnClickListener{
            if (selectedPosition>=0)
                notifyItemChanged(selectedPosition)
            selectedPosition = holder.adapterPosition
            notifyItemChanged(selectedPosition)
            onItemClick?.invoke(typeroom.name)
        }
    }
    var onItemClick: ((String) -> Unit)? = null
}
