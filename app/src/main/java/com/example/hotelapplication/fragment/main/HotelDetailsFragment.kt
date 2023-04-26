package com.example.hotelapplication.fragment.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.hotelapplication.adapter.TypeRoomAdapter
import com.example.hotelapplication.adapter.ViewPager2Images
import com.example.hotelapplication.databinding.FragmentHotelDetailsBinding
import com.example.hotelapplication.util.hideBottomNavigationView
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HotelDetailsFragment:Fragment() {
    private val args by navArgs<HotelDetailsFragmentArgs>()
    private lateinit var bingding:FragmentHotelDetailsBinding
    private val viewPageAdapter by lazy{ ViewPager2Images() }
    private val typeRoomAdapter by lazy{ TypeRoomAdapter() }
    private var selectedRoom:String?=null
//    private val viewModel by viewModels<DetailsViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        hideBottomNavigationView()
        bingding=FragmentHotelDetailsBinding.inflate(inflater)
        return bingding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val hotel = args.hotel
        setupTypeRv()
        setupViewpager()
        bingding.imageClose.setOnClickListener {
            findNavController().navigateUp()
        }

        typeRoomAdapter.onItemClick={
            selectedRoom = it
        }
//        bingding.buttonAddToCart.setOnClickListener {
//            viewModel.addUpdateProductInCart(CartProduct(product,1,selectedColor,selectedSize))
//        }
//        lifecycleScope.launchWhenStarted {
//            viewModel.addToCart.collectLatest {
//                when(it){
//                    is Resource.Loading ->{
//                        bingding.buttonAddToCart.startAnimation()
//                    }
//                    is Resource.Success ->{
//                        bingding.buttonAddToCart.revertAnimation()
//                        bingding.buttonAddToCart.setBackgroundColor(resources.getColor(R.color.black))
//                    }
//                    is Resource.Error ->{
//                        bingding.buttonAddToCart.stopAnimation()
//                        Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()
//                    }
//                    else ->Unit
//                }
//            }
//        }
        bingding.apply {
            tvHotelName.text=hotel.name
            tvHotelPrice.text=" ${hotel.price} VND/Ngày"
            tvHotelDescription.text=hotel.description
            tvHotelAdresss.text=hotel.address
            if(hotel.typeRoom.isNullOrEmpty()){
                tvTypeRoom.visibility=View.INVISIBLE

            }
        }
        viewPageAdapter.differ.submitList(hotel.images)
        typeRoomAdapter.differ.submitList(hotel.typeRoom)

    }

    private fun setupTypeRv() {
        bingding.rvTypeRoom.apply{
            adapter= typeRoomAdapter
            layoutManager= LinearLayoutManager(requireContext(),LinearLayoutManager.HORIZONTAL,false)
        }
    }

    private fun setupViewpager() {
        bingding.apply {
            viewPagerProductImages.adapter=viewPageAdapter
        }
    }



}