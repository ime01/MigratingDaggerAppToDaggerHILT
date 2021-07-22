package com.flowz.daggerexampleapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.flowz.daggerexampleapp.adapter.RecyclerViewAdapter
import com.flowz.daggerexampleapp.databinding.ActivityMainBinding
import com.flowz.daggerexampleapp.model.RecyclerList
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var rvAdapter: RecyclerViewAdapter
    private lateinit var mainActivityViewModel: MainActivityViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        initRecyclerView()
        initViewModel()
    }

    private fun initViewModel() {
//     mainActivityViewModel =  ViewModelProvider(this).get(MainActivityViewModel::class.java)

//        Removed Providers and Factory methods as we now use DaggerHilt
        val mainActivityViewModel: MainActivityViewModel by viewModels()

        mainActivityViewModel.getLiveDataObserver().observe(this, object : Observer<RecyclerList>{
            override fun onChanged(t: RecyclerList?) {
                if (t != null){
                  rvAdapter.submitList(t.items)
                }else{
                  Snackbar.make(binding.rvRecyler, "Error in fetching data", Snackbar.LENGTH_LONG).show()

                }
            }
        })
        mainActivityViewModel.makeApiCall()

    }

    private fun initRecyclerView(){
        rvAdapter = RecyclerViewAdapter()
        binding.rvRecyler.layoutManager = LinearLayoutManager(this)
        binding.rvRecyler.adapter = rvAdapter

    }



}