package com.flowz.daggerexampleapp.loginandsavetoken

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
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
import com.flowz.daggerexampleapp.MainActivityViewModel
import com.flowz.daggerexampleapp.R
import com.flowz.daggerexampleapp.databinding.ActivityMainBinding
import com.flowz.daggerexampleapp.loginandsavetoken.models.LoginRequest
import com.flowz.daggerexampleapp.loginandsavetoken.preference.UserSessionManager
import com.flowz.daggerexampleapp.util.SAVETOKENKEY
import com.flowz.daggerexampleapp.workmanager.ShowNotificationWorker
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.qualifiers.ActivityContext
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import kotlin.properties.Delegates

@AndroidEntryPoint
class LoginActivity : AppCompatActivity() {
    private val loginViewModel: LoginViewModel by viewModels()
    private lateinit var  myWorkManager: WorkManager

//    @Inject
//    lateinit var userSessionManager: UserSessionManager


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        var username = "08145774619"
        var password = "imevbore1"
        var deviceID = "samsung-sm_a102u-RF8N4064G6X"
        var oneSignalId = "f2afa2ac-a879-465f-af09-1aef39e1c52e"
        val progressBar = findViewById<ProgressBar>(R.id.progressBar1)
        val text = findViewById<TextView>(R.id.textView1)
        val worker_text = findViewById<TextView>(R.id.worker_text)
        val button = findViewById<Button>(R.id.button1)
        val one_time = findViewById<Button>(R.id.one_time)
        val periodic = findViewById<Button>(R.id.periodic)
        val cancelone = findViewById<Button>(R.id.cancel1)
        val cancelPeriodic = findViewById<Button>(R.id.cancel_periodic)


        myWorkManager = WorkManager.getInstance(applicationContext)


        val loginRequest = LoginRequest(username, password, deviceID, oneSignalId)

        one_time.setOnClickListener {
            setOneTimeWorkRequest()
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

            loginViewModel.loginConnectionStatus.observe(this@LoginActivity, Observer {state->

                state?.also {
                    when(it){
                        LoginStatus.ERROR->{
                            text.setText("Error in logging you in")

//                    Snackbar.make(text, "Error in Logging in").show()
                        }
                        LoginStatus.LOADING->{
                            progressBar.visibility = View.VISIBLE
                        }
                        LoginStatus.DONE->{
                            progressBar.visibility = View.GONE
                            Log.e("done", "Request worked")
                            loginViewModel.loginResponse.observe(this@LoginActivity, Observer {it
                                val res = it.user
                                text.text = res.userToken
                                Log.e("test", "${res.toString()}")
                                lifecycleScope.launch {
                                    loginViewModel.saveUserToken( res.userToken!!)
                                }
                                navMainActivity()

//                              Snackbar.make(button, "${res.toString()}").show()
                            })
                        }
                    }
                }

            })
//         navMainActivity()
        }
    }

    private fun setPeriodicWorkRequest() {

        val periodicWorkRequest = PeriodicWorkRequest.Builder(ShowNotificationWorker::class.java, 1, TimeUnit.HOURS)
            .addTag(PERIODIC_WORKER_TAG)
            .build()

//        WorkManager.getInstance(applicationContext).enqueue(periodicWorkRequest)

        myWorkManager.enqueue(periodicWorkRequest)

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
            .setInitialDelay(1, TimeUnit.MINUTES)
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
        val TIME_WORKER_TAG = "TIME WORK REQUEST"
        val PERIODIC_WORKER_TAG = "PERIODIC WORK REQUEST"
    }
}