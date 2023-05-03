package com.example.hotelapplication.fragment.main

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.hotelapplication.R
import com.example.hotelapplication.adapter.BookingAdapter
import com.example.hotelapplication.databinding.FragmentOrdersBinding
import com.example.hotelapplication.util.Resource
import com.example.hotelapplication.viewmodel.main.MainHotelViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
private val TAG="OrderFragment"
@AndroidEntryPoint
class OrderFragment: Fragment() {
    private lateinit var binding:FragmentOrdersBinding
    private lateinit var bookingAdapter: BookingAdapter
    private val viewModel by viewModels<MainHotelViewModel>()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding= FragmentOrdersBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupBookingHotelRv()
        bookingAdapter.onClick={
            val b= Bundle().apply {
                putParcelable("booking",it)
            }
            findNavController().navigate(R.id.action_OrderFragment_to_orderFragmentDetail,b)
        }

        lifecycleScope.launchWhenStarted {
            viewModel.hotelBooking.collectLatest {
                when(it){
                    is Resource.Loading->{
                    }
                    is Resource.Success->{
                        bookingAdapter.differ.submitList(it.data)
                    }
                    is Resource.Error->{
                        Log.e(TAG,it.message.toString())
                        Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()
                    }
                    else -> Unit
                }
            }
        }


    }
    private fun setupBookingHotelRv() {
        bookingAdapter= BookingAdapter()
        binding.rvAllBooking.apply {
            layoutManager= LinearLayoutManager(requireContext(),
                LinearLayoutManager.VERTICAL,false)
            adapter=bookingAdapter
        }
    }
}