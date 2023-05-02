package com.example.hotelapplication.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.hotelapplication.R
import com.example.hotelapplication.data.TypeRoom
import com.example.hotelapplication.databinding.TypeRvItemBinding
class TypeRoomAdapter: RecyclerView.Adapter<TypeRoomAdapter.TypeRoomViewHolder>() {
    private var selectedPositions = mutableSetOf<Int>()
    inner class TypeRoomViewHolder(private val binding:TypeRvItemBinding) : RecyclerView.ViewHolder(binding.root)
    {
        fun bind(typeroom: TypeRoom, position: Int){
            binding.tvnameroom.text = typeroom.name
            binding.tvdescriptionType.text=typeroom.description
            binding.tvpricetype.text="${typeroom.price} VND/ngày"
            binding.edquatitybooking.setText(typeroom.quantitybooking.toString())

            if (selectedPositions.contains(position)) {
                binding.check.visibility = View.VISIBLE
            } else {
                binding.check.visibility = View.INVISIBLE
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


    fun toggleSelection(position: Int, view: View) {
        if (selectedPositions.contains(position)) {
            selectedPositions.remove(position)
            differ.currentList[position].quantitybooking = 0
            view.findViewById<ImageView>(R.id.check).visibility = View.INVISIBLE
        } else {
            selectedPositions.add(position)
            val quantitybook = view.findViewById<EditText>(R.id.edquatitybooking).text.toString().toInt()
            if (quantitybook > 0) {
                differ.currentList[position].quantitybooking = quantitybook
                view.findViewById<ImageView>(R.id.check).visibility = View.VISIBLE
            } else {
                differ.currentList[position].quantitybooking = 1
            }
        }
        notifyItemChanged(position)

    }
    override fun onBindViewHolder(holder: TypeRoomViewHolder, position: Int) {
        val typeroom = differ.currentList[position]

        holder.bind(typeroom, position)

        holder.itemView.setOnClickListener {
            toggleSelection(position,it)
            // gọi hàm callback để thông báo danh sách đã thay đổi
            onItemClick?.invoke(getSelectedItems())

        }

    }

    fun getSelectedItems(): List<TypeRoom> {
        return selectedPositions.map { differ.currentList[it] }
    }

    var onItemClick: ((List<TypeRoom>) -> Unit)? = null

}
