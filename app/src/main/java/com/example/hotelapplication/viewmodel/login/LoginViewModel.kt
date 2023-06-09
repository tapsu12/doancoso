package com.example.hotelapplication.viewmodel.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.hotelapplication.data.User
import com.example.hotelapplication.util.Resource
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val firestore: FirebaseFirestore
) : ViewModel() {

    private val _loginad = MutableSharedFlow<Resource<FirebaseUser>>()
    val loginad = _loginad.asSharedFlow()
    private val _resetPassword = MutableSharedFlow<Resource<String>>()
    val resetPassword = _resetPassword.asSharedFlow()
    private val _login = MutableSharedFlow<Resource<FirebaseUser>>()
    val login = _login.asSharedFlow()

    fun login(email: String, password: String) {
        viewModelScope.launch { _login.emit(Resource.Loading()) }
        firebaseAuth.signInWithEmailAndPassword(email, password).addOnSuccessListener { viewModelScope.launch {
            it.user?.let {
                _login.emit(Resource.Success(it))
            }
        }
        }.addOnFailureListener {
            viewModelScope.launch {
                _login.emit(Resource.Error(it.message.toString()))
            }
        }
    }

    fun loginad(email: String, password: String) {
        viewModelScope.launch { _loginad.emit(Resource.Loading()) }

        firebaseAuth.signInWithEmailAndPassword(email, password).addOnSuccessListener {
            viewModelScope.launch {
                val currentUser = firebaseAuth.currentUser
                if (currentUser != null) {
                    val userId = currentUser.uid
                    val adminDoc = firestore.collection("admin").document(userId).get().await()
                    if (adminDoc.exists()) {
                        viewModelScope.launch {

                            _loginad.emit(Resource.Success(currentUser))
                        }
                    } else {
                        viewModelScope.launch {
                            _loginad.emit(Resource.Error("User not found"))
                        }
                    }
                }
            }
        }.addOnFailureListener {
            viewModelScope.launch {
                _loginad.emit(Resource.Error(it.message.toString()))
            }
        }
    }


    fun resetPassword(email: String) {
        viewModelScope.launch {
            _resetPassword.emit(Resource.Loading())
        }
        firebaseAuth.sendPasswordResetEmail(email).addOnSuccessListener {
            viewModelScope.launch {
                _resetPassword.emit(Resource.Success(email))
            }
        }.addOnFailureListener {
            viewModelScope.launch {
                _resetPassword.emit(Resource.Error(it.message.toString()))
            }
        }
    }
}