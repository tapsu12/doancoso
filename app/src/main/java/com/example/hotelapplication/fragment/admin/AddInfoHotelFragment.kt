package com.example.hotelapplication.fragment.admin

import android.app.Dialog
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.hotelapplication.R
import com.example.hotelapplication.data.Hotel
import com.example.hotelapplication.data.TypeRoom
import com.example.hotelapplication.databinding.FragmentAddInfoHotelBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.io.ByteArrayOutputStream
import java.util.*
@AndroidEntryPoint
class AddInfoHotelFragment : Fragment() {
    private lateinit var binding: FragmentAddInfoHotelBinding
    private var selectedImage = mutableListOf<Uri>()
    private var typeRoomList= mutableListOf<TypeRoom>()
    private val hotelsStorage = Firebase.storage.reference
    private val firestore = Firebase.firestore
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding= FragmentAddInfoHotelBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val selectImagesActivityResult =registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                if (result.resultCode == AppCompatActivity.RESULT_OK) {
                    val intent = result.data

                    if (intent?.clipData != null) {
                        val count = intent.clipData?.itemCount ?: 0
                        (0 until count).forEach {
                            var imageUri = intent.clipData?.getItemAt(it)?.uri
                            imageUri?.let {
                                selectedImage.add(it)
                            }
                        }
                    } else {
                        val imageUri = intent?.data
                        imageUri?.let {
                            selectedImage.add(it)
                        }
                    }
                    updateImages()
                }
            }
        binding.btnAddType.setOnClickListener {
            showAddTypeRoomDialog()
        }
        binding.buttonImagesPicker.setOnClickListener {
            val intent = Intent(Intent.ACTION_GET_CONTENT)
            intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
            intent.type = "image/*"
            selectImagesActivityResult.launch(intent)
        }
        binding.btnSave.setOnClickListener {
            val productionValidation = validationInformation()
            if (!productionValidation) {
                Toast.makeText(requireContext(), "Check Your Inputs", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }
            saveInfoHotel()
        }
    }
    //update ảnh
    private fun updateImages() {
        binding.tvSelectedImages.text = selectedImage.size.toString()
    }

        //Lưu thông tin khách sạn

        private fun saveInfoHotel() {
            val name = binding.edName.text.toString().trim()
            val addetail = binding.edADeitai.text.toString().trim()
            val price = binding.edPrice.text.toString().trim()
            val address = binding.edAddress.text.toString().trim()
            val offerPercentage = binding.offerPercentage.text.toString().trim()
            val description = binding.edDescription.text.toString().trim()
            var imagesByteArrays = getImagesByteArray()
            var images = mutableListOf<String>()
            val id =UUID.randomUUID().toString()
            lifecycleScope.launch {
                withContext(Dispatchers.Main) {
                    showLoading()
                }

                try {
                    async {
                        imagesByteArrays.forEach {
                            val id = UUID.randomUUID().toString()
                            launch {
                                val imagesStorage = hotelsStorage.child("hotels/images/$id")
                                val result = imagesStorage.putBytes(it).await()
                                val downloadUrl = result.storage.downloadUrl.await().toString()
                                images.add(downloadUrl)
                            }
                        }
                    }.await()
                } catch (e: java.lang.Exception) {
                    e.printStackTrace()
                    withContext(Dispatchers.Main) {
                        hideLoading()
                    }
                }

                val hotel = Hotel(
                    id,
                    name,
                    FirebaseAuth.getInstance().currentUser!!.uid,
                    price.toInt(),
                    address,
                    addetail,
                    if (offerPercentage.isEmpty()) null else offerPercentage.toFloat(),
                    if (description.isEmpty()) null else description,
    //                if (selectedColor.isEmpty()) null else selectedColor,
                    typeRoomList,
                    images
                )
                //Thêm lên firestore
                firestore.collection("Hotels").document("${id}").set(hotel).addOnSuccessListener {
                    hideLoading()

                    val adminId  = FirebaseAuth.getInstance().currentUser!!.uid

                    val hotelId = id

                    // Add hotel to admin's list
                    val adminRef = firestore.collection("admin").document("${adminId}")
                    adminRef.update("hotelList", FieldValue.arrayUnion(hotelId))
                        .addOnSuccessListener {
                            Log.d("Add hotel", "Hotel added to admin's list")
                        }
                        .addOnFailureListener {
                            Log.e("Add hotel", "Error adding hotel to admin's list", it)
                        }

                    binding.edName.setText("")
                    binding.edADeitai.setText("")
                    binding.edPrice.setText("")
                    binding.edAddress.setText("")
                    binding.offerPercentage.setText("")
                    binding.edDescription.setText("")

                }.addOnFailureListener {
                    hideLoading()
                    Log.e("Error", it.message.toString())
                }
            }
        }

        private fun hideLoading() {
            binding.progressbar.visibility = View.INVISIBLE
        }

        private fun showLoading() {
            binding.progressbar.visibility = View.VISIBLE
        }

        private fun getImagesByteArray(): List<ByteArray> {
            val imageByteArray = mutableListOf<ByteArray>()
            selectedImage.forEach {
                val stream = ByteArrayOutputStream()
                val imageBmp = MediaStore.Images.Media.getBitmap(requireContext().contentResolver, it)
                if (imageBmp.compress(Bitmap.CompressFormat.JPEG, 100, stream)) {
                    imageByteArray.add(stream.toByteArray())
                }
            }
            return imageByteArray
        }
        private fun showAddTypeRoomDialog() {
            val dialog = Dialog(requireContext())
            dialog.setContentView(R.layout.dialog_add_typeroom)
            dialog.setCancelable(false)

            val editTextName = dialog.findViewById<EditText>(R.id.editTextName)
            val editTextPrice = dialog.findViewById<EditText>(R.id.editTextPrice)
            val editTextDescription = dialog.findViewById<EditText>(R.id.editTextDescription)
            val editTextQuantity = dialog.findViewById<EditText>(R.id.editTextQuantity)

            val saveButton = dialog.findViewById<Button>(R.id.Savebuttom)
            val cancelButton = dialog.findViewById<Button>(R.id.Cancelbutton)

            saveButton.setOnClickListener {
                val name = editTextName.text.toString()
                val price = editTextPrice.text.toString().toLong()
                val description = editTextDescription.text.toString()
                val quantity = editTextQuantity.text.toString().toInt()

                val typeRoom = TypeRoom(name, price, description, quantity)
                typeRoomList.add(typeRoom)

                dialog.dismiss()
            }

            cancelButton.setOnClickListener {
                dialog.dismiss()
            }

            dialog.show()
        }

        private fun validationInformation(): Boolean {
            if (binding.edPrice.text.toString().trim().isEmpty()) return false
            if (binding.edName.text.toString().trim().isEmpty()) return false
            if (typeRoomList.isEmpty())return false
            if (selectedImage.isEmpty()) return false
            return true
        }

    }
