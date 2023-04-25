package com.example.hotelapplication.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import com.example.hotelapplication.R
import com.example.hotelapplication.viewmodel.login.LoginViewModel
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await
import java.time.LocalDate
import java.time.YearMonth
import java.time.Month


class TestActivity : AppCompatActivity() {
    val viewModel by viewModels<LoginViewModel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test)

        val db = Firebase.firestore
        val bookingCollection = db.collection("Booking")

// Lấy danh sách các ngày trong tháng
        val month = YearMonth.of(2023, Month.MAY)
        val dates = month.atEndOfMonth().dayOfMonth
        val dateList = (1..dates).map { LocalDate.of(2023, Month.MAY, it) }

// Lấy danh sách tất cả các phòng
        val roomList = listOf("Room 1", "Room 2", "Room 3", "Room 4", "Room 5")

// Tạo collection "Booking" với id là các ngày trong tháng
        for (date in dateList) {
            val bookingDoc = bookingCollection.document(date.toString())
            val bookingData = mutableMapOf<String, List<String>>()
            for (room in roomList) {
                bookingData[room] = listOf()
            }
            bookingDoc.set(bookingData)
                .addOnSuccessListener {
                    println("Booking collection for $date created successfully")
                }
                .addOnFailureListener { e ->
                    println("Error creating booking collection for $date: $e")
                }
        }

//        val db = FirebaseFirestore.getInstance()
//        val usersRef = db.collection("Hotels")
//        usersRef.get().addOnSuccessListener { querySnapshot ->
//            val firstDocSnapshot = querySnapshot.documents.firstOrNull()
//            val firstDocId = firstDocSnapshot?.id
//            // sử dụng firstDocId ở đây
//            val documentRef = FirebaseFirestore.getInstance().collection("Hotels").document("$firstDocId")
//            // Thêm collection con vào document
//            documentRef.collection("subCollection")
//            val data = hashMapOf(
//                "field1" to "value1",
//                "field2" to "value2"
//            )
//
//            documentRef.collection("subCollection").add(data)
//        }.addOnFailureListener { exception ->
//
//        }
//        //Chuyển sang   collection khác
//        val userssRef = FirebaseFirestore.getInstance().collection("user")
//        val usernamesRef = FirebaseFirestore.getInstance().collection("usernames")
//
//        userssRef.get().addOnSuccessListener { querySnapshot ->
//            for (document in querySnapshot.documents) {
//                val name = document.getString("email")
//                val newDocument = usernamesRef.document(document.id)
//                newDocument.set(mapOf("name" to name))
//            }
//        }
//        val documentRef = FirebaseFirestore.getInstance().collection("Hotels").document("QvUPJlwpTEsy3sTcBufU")
//                // Thêm collection con vào document
//        documentRef.collection("subCollection")
//        val data = hashMapOf(
//            "field1" to "value1",
//            "field2" to "value2"
//        )
//        documentRef.collection("subCollection").add(data)


    }
}