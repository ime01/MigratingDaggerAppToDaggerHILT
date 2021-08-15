package com.flowz.daggerexampleapp.loginandsavetoken

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.flowz.daggerexampleapp.loginandsavetoken.models.LoginRequest
import com.flowz.daggerexampleapp.loginandsavetoken.models.LoginResponse
import com.flowz.daggerexampleapp.loginandsavetoken.preference.UserSessionManager
import com.flowz.daggerexampleapp.util.SAVETOKENKEY
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Response
import javax.inject.Inject

enum class  LoginStatus {LOADING, ERROR, DONE}

@HiltViewModel
class LoginViewModel @Inject constructor(private var loginRepository: LoginRepository, private val userSessionManager: UserSessionManager): ViewModel() {

    val loginConnectionStatus = MutableLiveData<LoginStatus>()
    val loginResponse = MutableLiveData<LoginResponse>()

    fun LoginUser(loginRequest: LoginRequest){

        viewModelScope.launch(Dispatchers.IO) {
            try {
                withContext(Dispatchers.Main){
                    loginConnectionStatus.value = LoginStatus.LOADING
                }

                 loginResponse.postValue(loginRepository.loginUser(loginRequest))

                withContext(Dispatchers.Main){
                    loginConnectionStatus.value = LoginStatus.DONE
                }
            }catch (e:Exception){
                e.printStackTrace()
                withContext(Dispatchers.Main){
                    loginConnectionStatus.value = LoginStatus.ERROR
                }
            }
        }
    }

   suspend fun saveUserToken(userToken : String){
        with(userToken){
            userSessionManager.SaveUserToken(SAVETOKENKEY,userToken)
        }
    }

    suspend fun readUserToken(key : String): String{
//        with(userToken){
        return userSessionManager.ReadUserToken(SAVETOKENKEY)!!
//        }

    }



}