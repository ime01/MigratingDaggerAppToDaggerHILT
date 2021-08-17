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

data class Resoucre<out T>(val status:LoginStatus, val data: T?, val message: String?){

    companion object{

        fun <T> success(data: T?) : Resoucre<T>{

            return  Resoucre(LoginStatus.DONE, data, null)
        }

        fun <T> error (msg: String, data: T?) : Resoucre<T>{
            return  Resoucre(LoginStatus.ERROR, data, msg)
        }

        fun <T> loading(data: T?): Resoucre<T>{
            return Resoucre(LoginStatus.LOADING, data, null)
        }

    }
}


@HiltViewModel
class LoginViewModel @Inject constructor(private var loginRepository: LoginRepository, private val userSessionManager: UserSessionManager): ViewModel() {

    val loginConnectionStatus = MutableLiveData<LoginStatus>()
    val loginResponse = MutableLiveData<LoginResponse>()
    val apiResponse = MutableLiveData<Resoucre<LoginResponse>>()

    fun LoginUser(loginRequest: LoginRequest){

        viewModelScope.launch(Dispatchers.IO) {
            try {
                withContext(Dispatchers.Main){
//                    loginConnectionStatus.value = LoginStatus.LOADING
                    apiResponse.postValue(Resoucre.loading(null))
                }

//                 loginResponse.postValue(loginRepository.loginUser(loginRequest))
                 apiResponse.postValue(Resoucre.success((loginRepository.loginUser(loginRequest))))

//                withContext(Dispatchers.Main){
//                    loginConnectionStatus.value = LoginStatus.DONE
//                }
            }catch (e:Exception){
                e.printStackTrace()
                withContext(Dispatchers.Main){
//                    loginConnectionStatus.value = LoginStatus.ERROR
                    apiResponse.postValue(Resoucre.error(e.toString(), null))
                }
            }
        }
    }

   suspend fun saveUserToken(userToken : String){
       userSessionManager.SaveUserToken(SAVETOKENKEY,userToken)
    }

    suspend fun readUserToken(key : String): String{
//        with(userToken){
        return userSessionManager.ReadUserToken(SAVETOKENKEY)!!
//        }

    }



}