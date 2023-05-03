package com.example.hotelapplication.fragment.main
import android.app.DatePickerDialog
import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.hotelapplication.R
import com.example.hotelapplication.adapter.TypeRoomAdapter
import com.example.hotelapplication.adapter.ViewPager2Images
import com.example.hotelapplication.data.TypeRoom
import com.example.hotelapplication.databinding.FragmentHotelDetailsBinding
import com.example.hotelapplication.util.hideBottomNavigationView
import com.google.android.material.datepicker.MaterialDatePicker
import dagger.hilt.android.AndroidEntryPoint
import androidx.fragment.app.FragmentManager
import com.example.hotelapplication.data.Booking
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import java.util.Date

@AndroidEntryPoint
class HotelDetailsFragment:Fragment() {
    private val args by navArgs<HotelDetailsFragmentArgs>()
    private lateinit var bingding:FragmentHotelDetailsBinding
    private val viewPageAdapter by lazy{ ViewPager2Images() }
    private val typeRoomAdapter by lazy{ TypeRoomAdapter() }
    private var selectedRooms: List<TypeRoom> = emptyList()
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

        typeRoomAdapter.onItemClick={
                selectedRooms ->
            this.selectedRooms = selectedRooms

            val pricetyroom =calculatePriceTyperoom()
            bingding.btnBooking.setOnClickListener {
            showPayDialog(hotel.name,pricetyroom,selectedRooms)

            }
        }

        // Hiển thị DatePickerDialog khi người dùng nhấn vào một button nào đó

        bingding.imageClose.setOnClickListener {
            findNavController().navigateUp()
        }

        bingding.apply {
            tvHotelName.text=hotel.name
            tvHotelPrice.text=" ${hotel.price} VND/Ngày"
            tvHotelDescription.text=hotel.description
            tvHotelAdresss.text="${hotel.addressdetail} ,${hotel.address}"
            if(hotel.typeRoom.isNullOrEmpty()){
                tvTypeRoom.visibility=View.INVISIBLE

            }
        }
        viewPageAdapter.differ.submitList(hotel.images)
        typeRoomAdapter.differ.submitList(hotel.typeRoom)

    }
    private fun calculatePriceTyperoom(): Long {
        var totalPrice: Long = 0
        selectedRooms.forEach { typeroom ->
            totalPrice += (typeroom.price * typeroom.quantitybooking.toLong())
        }
        return totalPrice
    }
    private fun setupTypeRv() {
        bingding.rvTypeRoom.apply{
            adapter= typeRoomAdapter
            layoutManager= LinearLayoutManager(requireContext(),LinearLayoutManager.VERTICAL,false)
        }
    }

    private fun setupViewpager() {
        bingding.apply {
            viewPagerProductImages.adapter=viewPageAdapter
        }
    }
    private fun showPayDialog(name:String,total:Long,list:List<TypeRoom>) {
        val dialog = Dialog(requireContext())
        dialog.setContentView(R.layout.dialog_pay)
        dialog.setCancelable(false)

        val edtNamebook = dialog.findViewById<EditText>(R.id.edNameBook)
        val edtCMND = dialog.findViewById<EditText>(R.id.edCmndBook)
        val tvtotalprice = dialog.findViewById<TextView>(R.id.totalPrice)
        val tvCheckin = dialog.findViewById<TextView>(R.id.datein)
        val tvCheckout = dialog.findViewById<TextView>(R.id.dateout)
        val tvCheckinnh = dialog.findViewById<TextView>(R.id.dateinnh)
        val tvCheckoutnh = dialog.findViewById<TextView>(R.id.dateoutnh)
        val tvDay=dialog.findViewById<TextView>(R.id.totalday)
        val selectvDay=dialog.findViewById<Button>(R.id.checkDate)
        val saveButton = dialog.findViewById<Button>(R.id.btnBooking)
        val cancelButton = dialog.findViewById<Button>(R.id.btnCancel)
        selectvDay.setOnClickListener {
            val builder = MaterialDatePicker.Builder.dateRangePicker()
            val picker = builder.build()
            picker.addOnPositiveButtonClickListener { selection ->
                // lấy ngày check-in và check-out
                val startDate = Date(selection.first!!)
                val endDate = Date(selection.second!!)
                // tính số ngày lưu trú
                val numberOfDays = TimeUnit.DAYS.convert(endDate.time - startDate.time, TimeUnit.MILLISECONDS).toInt()
                val formatter = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                tvCheckin.text = formatter.format(startDate)
                tvCheckout.text = formatter.format(endDate)
                tvDay.text = numberOfDays.toString()
                val totalPriceBooking= total*numberOfDays
                tvtotalprice.text= totalPriceBooking.toString()
            }

            picker.show(requireFragmentManager(), "date_picker")
        }
        saveButton.setOnClickListener {
            val totalPriceBooking=tvtotalprice.text.toString().toLong()
            val day=tvDay.text.toString().toInt()
            val namebooking=edtNamebook.text.toString()
            val cmndbooking=edtCMND.text.toString()
            val formatter = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
            val checkin = formatter.parse(tvCheckin.text.toString())
            val checkout = formatter.parse(tvCheckout.text.toString())
            AddBookingtoFirebase(name,namebooking,cmndbooking,totalPriceBooking,checkin,checkout,list,day)
            dialog.dismiss()

        }

        cancelButton.setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()
    }



    fun AddBookingtoFirebase(name:String,namebooking:String,cmnd:String,total: Long,checkin:Date,checkout:Date,hotel:List<TypeRoom>,day:Int){
        val db = Firebase.firestore
        val bookingRef=db.collection("user").document("${FirebaseAuth.getInstance().currentUser!!.uid}").collection("bookings")
//        val bookingRef = db.collection("bookings").document()
        val booking = Booking(
            idbooking = FirebaseAuth.getInstance().currentUser!!.uid ,
            namehotel = name,
            namebooking =  namebooking  ,
            cmnd = cmnd,
            hotelbooking = hotel,
            totalPrice = total,
            date = Date() ,
            dateCheckin = checkin ,
            dateCheckout = checkout,
            numberdate = day
        )
        val timestamp = Timestamp(booking.date)
        val checkinDate = java.sql.Date(booking.dateCheckin.time)
        val checkoutDate = java.sql.Date(booking.dateCheckout.time)


        bookingRef.add(mapOf(
            "idbooking" to booking.idbooking,
            "namehotel" to booking.namehotel,
            "cmnd" to booking.cmnd,
            "hotelbooking" to booking.hotelbooking,
            "totalPrice" to booking.totalPrice,
            "date" to timestamp,
            "dateCheckin" to checkinDate,
            "dateCheckout" to checkoutDate,
            "numberdate" to booking.numberdate
        )).addOnSuccessListener {
            Toast.makeText(requireContext(), "Đặt phòng thành công", Toast.LENGTH_SHORT).show()
        }
    }

}