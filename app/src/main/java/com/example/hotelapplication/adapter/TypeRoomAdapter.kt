package com.example.hotelapplication.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.hotelapplication.databinding.TypeRvItemBinding
class TypeRoomAdapter: RecyclerView.Adapter<TypeRoomAdapter.TypeRoomViewHolder>() {
    private var selectedPosition = -1
    inner class TypeRoomViewHolder(private val binding:TypeRvItemBinding) : RecyclerView.ViewHolder(binding.root)
    {
        fun bind(size: String, position: Int){
            binding.tvSize.text = size
            if (position == selectedPosition){// size is selected
                binding.apply {
                    imageShadow.visibility = View.VISIBLE

                }
            } else{ // size not is selected
                binding.apply {
                    imageShadow.visibility = View.INVISIBLE

                }
            }


        }
    }
    // diffCallback là một đối tượng để so sánh hai mục khác nhau trong danh sách các size.
    private val diffCallback =object : DiffUtil.ItemCallback<String>(){
        //Đối tượng này cần phải thực hiện hai phương thức areItemsTheSame và areContentsTheSame.
        override fun areItemsTheSame(oldItem: String, newItem: String): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: String, newItem: String): Boolean {
            return oldItem == newItem

        }

    }
    val differ = AsyncListDiffer(this, diffCallback)
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
        val size = differ.currentList[position]
        holder.bind(size, position)

        holder.itemView.setOnClickListener{
            if (selectedPosition>=0)
                notifyItemChanged(selectedPosition)
            selectedPosition = holder.adapterPosition
            notifyItemChanged(selectedPosition)
            onItemClick?.invoke(size)
        }
    }
    var onItemClick: ((String) -> Unit)? = null
}
