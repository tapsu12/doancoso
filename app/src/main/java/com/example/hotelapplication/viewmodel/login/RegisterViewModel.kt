package com.example.hotelapplication.viewmodel.login

import androidx.lifecycle.ViewModel
import com.example.hotelapplication.data.Admin
import com.example.hotelapplication.data.User
import com.example.hotelapplication.util.*
import com.example.hotelapplication.util.Constants.ADMIN_COLLECTION
import com.example.hotelapplication.util.Constants.USER_COLLECTION
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val firebaseAuth: FirebaseAuth, private val db: FirebaseFirestore
) : ViewModel() {

    private val _register = MutableStateFlow<Resource<User>>(Resource.Unspecified())
    val register: Flow<Resource<User>> = _register
    private val _adregister = MutableStateFlow<Resource<Admin>>(Resource.Unspecified())
    val adregister: Flow<Resource<Admin>> = _adregister

    private val _validation = Channel<RegisterFieldsState>()
    val validation = _validation.receiveAsFlow()

    fun createAccountWithEmailAndPassword(user: User, password: String) {

        if (checkValidation(user, password)) {
            runBlocking {
                _register.emit(Resource.Loading())
            }
            firebaseAuth.createUserWithEmailAndPassword(user.email, password).addOnSuccessListener {
                it.user?.let {
                    saveUserInfo(it.uid, user)
                }
            }.addOnFailureListener {
                _register.value = Resource.Error(it.message.toString())
            }
        } else {
            val registerFieldsState = RegisterFieldsState(
                validateEmail(user.email), validatePassword(password)
            )
            runBlocking {
                _validation.send(registerFieldsState)
            }
        }
    }
    fun createAdAccountWithEmailAndPassword(admin: Admin, password: String) {

        if (checkValidationAd(admin, password)) {
            runBlocking {
                _adregister.emit(Resource.Loading())
            }
            firebaseAuth.createUserWithEmailAndPassword(admin.email, password).addOnSuccessListener {
                it.user?.let {
                    saveAdminInfo(it.uid, admin)
                }
            }.addOnFailureListener {
                _adregister.value = Resource.Error(it.message.toString())
            }
        } else {
            val registerFieldsState = RegisterFieldsState(
                validateEmail(admin.email), validatePassword(password)
            )
            runBlocking {
                _validation.send(registerFieldsState)
            }
        }
    }

    private fun saveAdminInfo(uidAdmin: String, admin: Admin) {
        db.collection(ADMIN_COLLECTION)
            .document(uidAdmin)
            .set(admin)
            .addOnSuccessListener {
                _adregister.value = Resource.Success(admin)
            }.addOnFailureListener {
                _adregister.value = Resource.Error(it.message.toString())
            }
    }

    private fun checkValidationAd(admin: Admin, password: String): Boolean {
        val emailValidation = validateEmail(admin.email)
        val passwordValidation = validatePassword(password)
        val shouldRegister =
            emailValidation is RegisterValidation.Success && passwordValidation is RegisterValidation.Success

        return shouldRegister
    }

    //lưu tên vào cơ sở dữ liệu
    private fun saveUserInfo(userUid: String, user: User) {
        db.collection(USER_COLLECTION)
            .document(userUid)
            .set(user)
            .addOnSuccessListener {
                _register.value = Resource.Success(user)
            }.addOnFailureListener {
                _register.value = Resource.Error(it.message.toString())
            }
    }
        //kiểm tra điều kiện
    private fun checkValidation(user: User, password: String): Boolean {
        val emailValidation = validateEmail(user.email)
        val passwordValidation = validatePassword(password)
        val shouldRegister =
            emailValidation is RegisterValidation.Success && passwordValidation is RegisterValidation.Success

        return shouldRegister
    }

}