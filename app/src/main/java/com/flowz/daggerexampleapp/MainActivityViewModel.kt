package com.flowz.daggerexampleapp

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.flowz.daggerexampleapp.di.RetroServiceInterface
import com.flowz.daggerexampleapp.loginandsavetoken.LoginStatus
import com.flowz.daggerexampleapp.loginandsavetoken.models.LoginRequest
import com.flowz.daggerexampleapp.loginandsavetoken.models.LoginResponse
import com.flowz.daggerexampleapp.model.RecyclerList
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject


enum class  apiStatus {LOADING, ERROR, DONE}

data class Resource<out T>(val status: apiStatus, val data: T?, val message: String?){

    companion object{

        fun <T> success(data: T?) : Resource<T>{

            return  Resource(apiStatus.DONE, data, null)
        }

        fun <T> error (msg: String, data: T?) : Resource<T>{
            return  Resource(apiStatus.ERROR, data, msg)
        }

        fun <T> loading(data: T?): Resource<T>{
            return Resource(apiStatus.LOADING, data, null)
        }

    }
}

//CHANGED AS WE NOW USE DAGGERHILT TO INJECT VIEWMODELS
//class MainActivityViewModel(application: Application) : AndroidViewModel(application) {

@HiltViewModel()
class MainActivityViewModel @Inject constructor (private val mservice: RetroServiceInterface)  : ViewModel() {

//    @Inject
//    lateinit var mservice: RetroServiceInterface

//    private lateinit var liveDataList: MutableLiveData<RecyclerList>
      val apiResponse = MutableLiveData<Resource<RecyclerList>>()

//    init {
//        (application as MyApplication).getRetroComponent().inject(this)
//        liveDataList = MutableLiveData()
//    }

//    fun getLiveDataObserver():MutableLiveData<RecyclerList>{
//        return  liveDataList
//    }

    fun fetchData(){
        viewModelScope.launch(Dispatchers.IO) {
            try {
                withContext(Dispatchers.Main){
//                    loginConnectionStatus.value = LoginStatus.LOADING
                    apiResponse.postValue(Resource.loading(null))
                }

//                 loginResponse.postValue(loginRepository.loginUser(loginRequest))
                apiResponse.postValue(Resource.success(mservice.getDataFromApi("atl")))

//                withContext(Dispatchers.Main){
//                    loginConnectionStatus.value = LoginStatus.DONE
//                }
            }catch (e:Exception){
                e.printStackTrace()
                withContext(Dispatchers.Main){
//                    loginConnectionStatus.value = LoginStatus.ERROR
                    apiResponse.postValue(Resource.error(e.toString(), null))
                }
            }
        }
    }

//    fun makeApiCall(){
//       val call: Call<RecyclerList>? = mservice.getDataFromApi("atl")
//        call?.enqueue(object: Callback<RecyclerList>{
//            override fun onFailure(call: Call<RecyclerList>, t: Throwable) {
//                liveDataList.postValue(null)
//
//            }
//
//            override fun onResponse(call: Call<RecyclerList>, response: Response<RecyclerList>) {
//               if (response.isSuccessful){
//                   liveDataList.postValue(response.body())
//               }else{
//                   liveDataList.postValue(null)
//               }
//
//            }
//        })
//    }
}