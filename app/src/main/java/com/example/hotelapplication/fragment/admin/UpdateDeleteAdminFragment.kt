package com.example.hotelapplication.fragment.admin

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.hotelapplication.R
import com.example.hotelapplication.databinding.FragmentAdminUpdateDeleteBinding
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions

class UpdateDeleteAdminFragment:Fragment() {
    private lateinit var binding:FragmentAdminUpdateDeleteBinding
    private val args by navArgs<UpdateDeleteAdminFragmentArgs>()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding= FragmentAdminUpdateDeleteBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val hotelshow=args.hotelshow
//        var documentId=""
//        val adapter=ArrayAdapter(this,)
        binding.apply {
            editTextName.setText(hotelshow.name)
            editTextAddress.setText(hotelshow.address)
            editTextAddressDetail.setText(hotelshow.addressdetail)
            editTextPrice.setText("${hotelshow.price}")

            editTextDescription.setText(hotelshow.description)

        }
        binding.btnDelete.setOnClickListener {
            val builder = AlertDialog.Builder(requireContext())
            builder.setTitle("Delete Confirmation")
            builder.setMessage("Are you sure you want to delete this hotel?")
            builder.setPositiveButton("Yes") { dialog, which ->
                // Xóa hotelshow ở đây
                val db = FirebaseFirestore.getInstance()
                val collectionRef = db.collection("Hotels")

                collectionRef.whereEqualTo("id", "${hotelshow.id}")
                    .get()
                    .addOnSuccessListener { documents ->
                        for (document in documents) {
                            document.reference.delete()
                        }
                    }
                    .addOnFailureListener { exception ->
                        Log.d(TAG, "Error getting documents: ", exception)
                    }
                Toast.makeText(requireContext(), "Hotel deleted", Toast.LENGTH_SHORT).show()
                findNavController().navigateUp()
            }
            builder.setNegativeButton("No") { dialog, which ->
                dialog.dismiss()
            }
            val dialog = builder.create()
            dialog.show()
        }
        binding.btnUpdate.setOnClickListener {
            val upname=binding.editTextName.text.toString()
            val upadress=binding.editTextAddress.text.toString()
            val upadressdetail=binding.editTextAddressDetail.text.toString()
            val updecription=binding.editTextDescription.text.toString()
            val upPrice=binding.editTextPrice.text.toString().toInt()

            val updatehotelcurrent = FirebaseFirestore.getInstance().collection("Hotels").document("${hotelshow.id}")
            val hotelupdates = hashMapOf(
                "name" to "${upname}",
                "price" to upPrice ,
                "address" to "${upadress}",
                "addressdetail" to "${upadressdetail}",
                "description" to "${updecription}"

            )
            updatehotelcurrent.set(hotelupdates, SetOptions.merge())
            findNavController().navigate(R.id.adminShowFragment)
        }


    }
}