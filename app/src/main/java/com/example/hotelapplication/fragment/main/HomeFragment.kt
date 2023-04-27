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
import com.example.hotelapplication.adapter.HotelAdapter
import com.example.hotelapplication.databinding.FragmentHomeBinding
import com.example.hotelapplication.util.Resource
import com.example.hotelapplication.viewmodel.main.MainHotelViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
private val TAG="HomeFragment"
@AndroidEntryPoint
class HomeFragment:Fragment(R.layout.fragment_home) {
    private lateinit var binding: FragmentHomeBinding
    private lateinit var hotelAdapter: HotelAdapter
    private val viewModel by viewModels<MainHotelViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViewHotelRv()
        //xử lí adapter
        hotelAdapter.onClick={
            val b= Bundle().apply {
                putParcelable("hotel",it)
            }
           findNavController().navigate(R.id.action_homeFragment3_to_hotelDetailsFragment,b)
        }

        lifecycleScope.launchWhenStarted {
            viewModel.hotelDetail.collectLatest {
                when(it){
                    is Resource.Loading->{
//                        binding.bestProductsProgressbar.visibility = View.VISIBLE
                    }
                    is Resource.Success->{
                       hotelAdapter.differ.submitList(it.data)
//                        binding.bestProductsProgressbar.visibility = View.GONE


                    }
                    is Resource.Error->{
                        Log.e(TAG,it.message.toString())
                        Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()
//                        binding.bestProductsProgressbar.visibility = View.GONE
                    }
                    else -> Unit
                }
            }
        }
    }

    private fun setupViewHotelRv() {
        hotelAdapter= HotelAdapter()
        binding.rvViewHotel.apply {
            layoutManager= LinearLayoutManager(requireContext(),
                LinearLayoutManager.VERTICAL,false)
            adapter=hotelAdapter
        }
    }
}