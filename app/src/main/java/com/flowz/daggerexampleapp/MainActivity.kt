package com.flowz.daggerexampleapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.flowz.daggerexampleapp.adapter.RecyclerViewAdapter
import com.flowz.daggerexampleapp.databinding.ActivityMainBinding
import com.flowz.daggerexampleapp.loginandsavetoken.LoginViewModel
import com.flowz.daggerexampleapp.loginandsavetoken.preference.UserSessionManager
import com.flowz.daggerexampleapp.model.RecyclerList
import com.flowz.daggerexampleapp.util.SAVETOKENKEY
import com.flowz.daggerexampleapp.util.showToast
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var rvAdapter: RecyclerViewAdapter
    private  var userToken = "fetch it"
    val loginViewModel: LoginViewModel by viewModels()
    val mainActivityViewModel: MainActivityViewModel by viewModels()


//    @Inject
//    lateinit var userSessionManager: UserSessionManager


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        readUserToken()
        initRecyclerView()
        initViewModel()
    }

    private fun readUserToken() {
        lifecycleScope.launch {
          userToken =  loginViewModel.readUserToken(SAVETOKENKEY)
//          userToken =  userSessionManager.ReadUserToken(SAVETOKENKEY)!!
            Log.e("token", " The Current users's token is: $userToken")
        }
        binding.userToken.text = "The Current users's token is:  $userToken"

    }

    private fun initViewModel() {
        //        Removed Providers and Factory methods as we now use DaggerHilt
//     mainActivityViewModel =  ViewModelProvider(this).get(MainActivityViewModel::class.java)


//        mainActivityViewModel.makeApiCall()
        mainActivityViewModel.fetchData()

        mainActivityViewModel.apiResponse.observe(this, Observer {

            mainActivityViewModel.apiResponse.observe(this, Observer {state->
//
                state?.also {
                    when(it.status){

                        apiStatus.ERROR->{
                            showToast(it.message)
                            Log.e("ERROR", "${it.message}")
                        }

                        apiStatus.LOADING->{
                            showToast("loading data")
                            binding.progressBarm.visibility = View.VISIBLE
                        }


                        apiStatus.DONE->{
                            binding.progressBarm.visibility = View.GONE
                            Log.e("FETCHED", "${it.data}")
                            showToast("data fetched")

                            rvAdapter.submitList(it.data?.items)
                        }
                    }
                }

            })
        })


//        mainActivityViewModel.getLiveDataObserver().observe(this, object : Observer<RecyclerList>{
//            override fun onChanged(t: RecyclerList?) {
//                Log.d("VALUES", t.toString())
//                if (t != null){
//                  rvAdapter.submitList(t.items)
//                }else{
//                  Snackbar.make(binding.rvRecyler, "Error in fetching data", Snackbar.LENGTH_LONG).show()
//
//                }
//            }
//        })


    }

    private fun initRecyclerView(){
//        rvAdapter = RecyclerViewAdapter()
        rvAdapter = RecyclerViewAdapter{

            showToast(it.description)
        }
        binding.rvRecyler.layoutManager = LinearLayoutManager(this)
        binding.rvRecyler.adapter = rvAdapter

    }



}