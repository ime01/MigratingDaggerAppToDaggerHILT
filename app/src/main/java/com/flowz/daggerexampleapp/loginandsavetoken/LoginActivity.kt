package com.flowz.daggerexampleapp.loginandsavetoken

import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Looper
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.work.*
import com.flowz.daggerexampleapp.MainActivity
import com.flowz.daggerexampleapp.R
import com.flowz.daggerexampleapp.loginandsavetoken.models.LoginRequest
import com.flowz.daggerexampleapp.util.PermissionUtils
import com.flowz.daggerexampleapp.util.showToast
import com.flowz.daggerexampleapp.workmanager.FetchMyLocationWorker
import com.flowz.daggerexampleapp.workmanager.ShowNotificationWorker
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit

@AndroidEntryPoint
class LoginActivity : AppCompatActivity() {
    private val loginViewModel: LoginViewModel by viewModels()
    private lateinit var  myWorkManager: WorkManager
    private lateinit var  textView: TextView
    private var  userLocationIs = ""

//    @Inject
//    lateinit var userSessionManager: UserSessionManager

    override fun onStart() {
        super.onStart()
        PermissionUtils.requestAccessFineLocationPermission(this, LOCATION_PERMISSION_REQUEST_CODE)
        when{
            PermissionUtils.isAccessFineLocationGranted(this)->{
                when{
                    PermissionUtils.isLocationEnabled(this)->{
                        setUpLocationListener()
                        showToast("Location Tracking triggered")
                    }
                    else->{
                        PermissionUtils.showGPSNotEnabledDialog(this)
                    }
                }
            }
            else->{
                PermissionUtils.requestAccessFineLocationPermission(this, LOCATION_PERMISSION_REQUEST_CODE)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        var username = "08145774619"
        var password = "imevbore1"
        var deviceID = "samsung-sm_a102u-RF8N4064G6X"
        var oneSignalId = "f2afa2ac-a879-465f-af09-1aef39e1c52e"
        val progressBar = findViewById<ProgressBar>(R.id.progressBar1)
        val plainText = findViewById<TextView>(R.id.textView1)
        textView = findViewById<TextView>(R.id.worker_text)
        val button = findViewById<Button>(R.id.button1)
        val one_time = findViewById<Button>(R.id.one_time)
        val periodic = findViewById<Button>(R.id.periodic)
        val cancelone = findViewById<Button>(R.id.cancel1)
        val cancelPeriodic = findViewById<Button>(R.id.cancel_periodic)


        myWorkManager = WorkManager.getInstance(applicationContext)


        val loginRequest = LoginRequest(username, password, deviceID, oneSignalId)

        one_time.setOnClickListener {
//            setOneTimeWorkRequest()
            setLocationUpdateWorkRequest()
        }

        periodic.setOnClickListener {
            setPeriodicWorkRequest()
        }

        cancelone.setOnClickListener {
            myWorkManager.cancelAllWorkByTag(TIME_WORKER_TAG)
            Log.i("ONETIME", "ONETIME WORK REQUEST TERMINATED")
        }


        cancelPeriodic.setOnClickListener {
           myWorkManager.cancelAllWorkByTag(PERIODIC_WORKER_TAG)
            Log.i("PERIODIC", "PERIODIC WORK REQUEST TERMINATED")
        }


        button.setOnClickListener {
            loginViewModel.LoginUser(loginRequest)
//            navMainActivity()

//            loginViewModel.loginConnectionStatus.observe(this@LoginActivity, Observer {state->
            loginViewModel.apiResponse.observe(this@LoginActivity, Observer {state->

                state?.also {
                    when(it.status){
                        LoginStatus.ERROR->{
                            plainText.setText(it.toString())

//                    Snackbar.make(text, "Error in Logging in").show()
                        }
                        LoginStatus.LOADING->{
                            progressBar.visibility = View.VISIBLE
                        }
                        LoginStatus.DONE->{
                            progressBar.visibility = View.GONE
                            Log.e("done", "Request worked")
//                            loginViewModel.loginResponse.observe(this@LoginActivity, Observer {it
                                val res = it.data
                                plainText.text = res?.userToken
                                Log.e("test", "${res.toString()}")

                                lifecycleScope.launch {
                                    res?.user?.userToken?.let { it1 ->
                                        loginViewModel.saveUserToken(
                                            it1
                                        )
                                    }
                                }
//                                navMainActivity()

//                              Snackbar.make(button, "${res.toString()}").show()
//                            })
                        }
                    }
                }

            })
         navMainActivity()
        }
    }




    @SuppressLint("MissingPermission")
    private fun setUpLocationListener() {

        val fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
        // for getting the current location update after every 2 seconds with high accuracy

        val locationRequest = LocationRequest().setInterval(5000).setFastestInterval(5000)
            .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)

        fusedLocationProviderClient.requestLocationUpdates(locationRequest, object: LocationCallback(){

            override fun onLocationResult(locationResult: LocationResult?) {
                super.onLocationResult(locationResult)
                if (locationResult != null) {
                    for (location in locationResult.locations){
                        val lat = location.latitude.toString()
                        val long = location.longitude.toString()
                        userLocationIs = "User Location Is $lat latitude: $long longitude"
                        textView.text = userLocationIs


                    }
                }
            }
        },
        Looper.myLooper())

    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {

        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        when(requestCode){
            LOCATION_PERMISSION_REQUEST_CODE ->{
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    when{
                        PermissionUtils.isLocationEnabled(this)->{
                            setUpLocationListener()
                        }
                        else->{
                            PermissionUtils.showGPSNotEnabledDialog(this)
                        }
                    }
                }else{
                    showToast("Location Perission Not Granted")
                }
            }
        }

    }

    private fun setPeriodicWorkRequest() {

//        val periodicWorkRequest = PeriodicWorkRequest.Builder(ShowNotificationWorker::class.java, 1, TimeUnit.HOURS)
        val periodicWorkRequest = PeriodicWorkRequest.Builder(FetchMyLocationWorker::class.java, 15, TimeUnit.MINUTES)
            .addTag(PERIODIC_WORKER_TAG)
            .build()

//        WorkManager.getInstance(applicationContext).enqueue(periodicWorkRequest)

        myWorkManager.enqueue(periodicWorkRequest)

    }


    private fun setLocationUpdateWorkRequest() {
        val constraints = Constraints.Builder()
            .setRequiresBatteryNotLow(true)
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()

//        val locationWorkRequest = PeriodicWorkRequest.Builder(FetchMyLocationWorker::class.java, 15, TimeUnit.MINUTES)
        val locationWorkRequest = OneTimeWorkRequest.Builder(FetchMyLocationWorker::class.java)
            .addTag(LOCATION_WORKER_TAG)
//            .setConstraints(constraints)
            .build()

//        myWorkManager.enqueueUniquePeriodicWork(LOCATION_WORKER_TAG,ExistingPeriodicWorkPolicy.REPLACE, locationWorkRequest)
        myWorkManager.enqueue(locationWorkRequest)


    }


    private fun setOneTimeWorkRequest() {
        val worker_text = findViewById<TextView>(R.id.worker_text)

//        val myWorkManager = WorkManager.getInstance(applicationContext)

        val constraints = Constraints.Builder()
            .setRequiresBatteryNotLow(true)
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()

        val tellMeTimeWorkRequet = OneTimeWorkRequest.Builder(ShowNotificationWorker::class.java)
            .setConstraints(constraints)
//            .setInitialDelay(1, TimeUnit.MINUTES)
            .addTag(TIME_WORKER_TAG)
            .build()

        myWorkManager.enqueue(tellMeTimeWorkRequet)

        myWorkManager.getWorkInfoByIdLiveData(tellMeTimeWorkRequet.id)
            .observe(this, Observer {
               worker_text.text = it.state.name

                if (it.state.isFinished){
                    Toast.makeText(applicationContext, "Work is Completed", Toast.LENGTH_LONG).show()
                }
            })
    }


    private fun navMainActivity() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

    companion object{
        private const val LOCATION_PERMISSION_REQUEST_CODE = 777
        val TIME_WORKER_TAG = "TIME WORK REQUEST"
        val PERIODIC_WORKER_TAG = "PERIODIC WORK REQUEST"
        val LOCATION_WORKER_TAG = "LOCATION WORK REQUEST"
    }
}