package com.example.hotelapplication.fragment.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.hotelapplication.adapter.BookingAdapter
import com.example.hotelapplication.adapter.TypeRoomAdapter
import com.example.hotelapplication.adapter.TyroomAdapter
import com.example.hotelapplication.databinding.FragmentOrderDetailBinding
import com.example.hotelapplication.viewmodel.main.MainHotelViewModel
import java.text.SimpleDateFormat
import java.util.*

class OrderFragmentDetail:Fragment() {
    private lateinit var binding:FragmentOrderDetailBinding
    private val args by navArgs<OrderFragmentDetailArgs>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding= FragmentOrderDetailBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val book = args.booking
        binding.apply {
            tvOrderId.text=book.idbooking
            tvNameBooking.text="Tên người đặt"+book.namebooking
            tvAddress.text="${book.namehotel}"
            tvCmnd.text="CMND"+book.cmnd
            val formattedDate = SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault())
            tvcheckin.text="Thời gian checkin:"+formattedDate.format(book.dateCheckin)
            tvcheckout.text="Thời gian checkout:"+formattedDate.format(book.dateCheckout)
            tvcheckday.text="Thời gian :"+ book.numberdate +"ngày"
            tvTotalprice.text=book.totalPrice.toString()
        }
        val tyroomAdapter=TyroomAdapter(book.hotelbooking)
        binding.rvBooking.apply {
            adapter= tyroomAdapter
            layoutManager= LinearLayoutManager(requireContext(),LinearLayoutManager.VERTICAL,false)
        }
        binding.imgCloseOrder.setOnClickListener {
            findNavController().navigateUp()
        }
    }
}