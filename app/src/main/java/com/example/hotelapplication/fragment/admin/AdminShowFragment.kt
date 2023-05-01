package com.example.hotelapplication.fragment.admin

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.hotelapplication.R
import com.example.hotelapplication.adapter.ShowHotelAdapter
import com.example.hotelapplication.data.Admin
import com.example.hotelapplication.databinding.FragmentAdminShowBinding
import com.example.hotelapplication.dialog.setupBottomSheetDialog
import com.example.hotelapplication.util.Resource
import com.example.hotelapplication.viewmodel.admin.AdminViewModel
import com.example.hotelapplication.viewmodel.main.MainHotelViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest

private val TAG="AdminShowFragment"

@AndroidEntryPoint
class AdminShowFragment:Fragment() {
    private lateinit var binding:FragmentAdminShowBinding
    private lateinit var showhotelAdapter: ShowHotelAdapter
    private val viewModel by viewModels<MainHotelViewModel>()
    private val viewsModel by viewModels<AdminViewModel>()
    private lateinit var imageActivityResultLauncher: ActivityResultLauncher<Intent>
    private var imageUri: Uri?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        imageActivityResultLauncher=
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
                imageUri=it.data?.data
                Glide.with(this).load(imageUri).into(binding.imageUser)
            }
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding= FragmentAdminShowBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        showhotelAdapter=ShowHotelAdapter()
        binding.rvshowhotel.apply {
            layoutManager= LinearLayoutManager(requireContext(),
                LinearLayoutManager.VERTICAL,false)
            adapter= showhotelAdapter
        }
        showhotelAdapter.onClick={
            val b= Bundle().apply {
                putParcelable("hotelshow",it)
            }
            findNavController().navigate(R.id.action_adminShowFragment_to_updateDeleteAdminFragment,b)
        }

            lifecycleScope.launchWhenStarted {
            viewModel.hotelshow.collectLatest {
                when(it){
                    is Resource.Loading->{
                    }
                    is Resource.Success->{
                        showhotelAdapter.differ.submitList(it.data)

                    }
                    is Resource.Error->{
                        Log.e(TAG,it.message.toString())
                        Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()
                    }
                    else -> Unit
                }
            }
        }
            lifecycleScope.launchWhenStarted {
                viewsModel.adminup.collectLatest {
                    when(it){
                        is Resource.Loading ->{
                            showAdminLoading()
                        }
                        is Resource.Success ->{
                            hideAdminLoading()
                            showAdminInfomtion(it.data!!)
                        }
                        is Resource.Error ->{
                            Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()

                        }
                        else -> Unit
                    }
                }
            }
            lifecycleScope.launchWhenStarted {
                viewsModel.updateAdminInfo.collectLatest {
                    when(it){
                        is Resource.Loading ->{
                            binding.buttonSave.startAnimation()
                        }
                        is Resource.Success ->{
                            binding.buttonSave.revertAnimation()
                            findNavController().navigateUp()
                        }
                        is Resource.Error ->{
                            Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()

                        }
                        else -> Unit
                    }

                }
            }
            binding.tvUpdatePassword.setOnClickListener {
                setupBottomSheetDialog {  }
            }
            binding.imageCloseAdminAccount.setOnClickListener {
                findNavController().navigate(R.id.adminShowFragment)
            }
            binding.buttonSave.setOnClickListener {
                binding.apply {
                    val fistName= edFirstName.text.toString().trim()
                    val lastName= edLastName.text.toString().trim()
                    val email= edEmail.text.toString().trim()
                    val phone= edPhone.text.toString().trim()
                    val admin= Admin(fistName,lastName,email,phone,"admin")
                    viewsModel.updateAdmin(admin,imageUri)

                }


            }
            binding.imageEdit.setOnClickListener {
                val intent=Intent(Intent.ACTION_GET_CONTENT)
                intent.type="img/*"
                imageActivityResultLauncher.launch(intent)
            }
        }

        private fun showAdminInfomtion(data: Admin) {
            binding.apply {
                Glide.with(this@AdminShowFragment).load(data.imgPath).error(ColorDrawable(Color.BLACK)).into(imageUser)
                edFirstName.setText(data.firstName)
                edLastName.setText(data.lastName)
                edEmail.setText(data.email)
                edPhone.setText(data.phone)
            }
        }

        private fun hideAdminLoading() {
            binding.apply {
                progressbarAccount.visibility=View.GONE
                imageUser.visibility=View.VISIBLE
                imageEdit.visibility=View.VISIBLE
                edFirstName.visibility=View.VISIBLE
                edLastName.visibility=View.VISIBLE
                edEmail.visibility=View.VISIBLE
                tvUpdatePassword.visibility=View.VISIBLE
                buttonSave.visibility=View.VISIBLE
            }
        }

        private fun showAdminLoading() {
            binding.apply {
                progressbarAccount.visibility=View.VISIBLE
                imageUser.visibility=View.INVISIBLE
                imageEdit.visibility=View.INVISIBLE
                edFirstName.visibility=View.INVISIBLE
                edLastName.visibility=View.INVISIBLE
                edEmail.visibility=View.INVISIBLE
                tvUpdatePassword.visibility=View.INVISIBLE
                buttonSave.visibility=View.INVISIBLE

            }
        }
        //




}