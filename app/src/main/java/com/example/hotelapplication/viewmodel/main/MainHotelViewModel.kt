package com.example.hotelapplication.viewmodel.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.hotelapplication.data.Hotel
import com.example.hotelapplication.util.Resource
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
@HiltViewModel
class MainHotelViewModel @Inject constructor(
    private var firestore: FirebaseFirestore
): ViewModel() {
    private val _hotelDetails =
        MutableStateFlow<Resource<List<Hotel>>>(Resource.Unspecified())
    val hotelDetail: StateFlow<Resource<List<Hotel>>> = _hotelDetails

    init {
        fetchHotelDetails()
    }

    fun fetchHotelDetails() {
        viewModelScope.launch {
            _hotelDetails.emit(Resource.Loading())
        }
        firestore.collection("Hotels")
            .get().addOnSuccessListener { result ->
                val hotel = result.toObjects(Hotel::class.java)
                viewModelScope.launch {
                    _hotelDetails.emit(Resource.Success(hotel))
                }
            }.addOnFailureListener {
                viewModelScope.launch {
                    _hotelDetails.emit(Resource.Error(it.message.toString()))
                }
            }
    }
}