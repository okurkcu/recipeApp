package com.arincdogansener.finalrecipeapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.arincdogansener.finalrecipeapp.databinding.ActivitySplashBinding
import com.arincdogansener.finalrecipeapp.entities.Category
import com.arincdogansener.finalrecipeapp.interfaces.GetDataService
import com.arincdogansener.finalrecipeapp.retrofitclient.RetrofitClientInstance
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.create
import kotlin.coroutines.CoroutineContext

open class BaseActivity : AppCompatActivity(), CoroutineScope {
    //private lateinit var binding : ActivityBaseBinding
    private lateinit var job: Job
    override val coroutineContext:CoroutineContext
        get() = job + Dispatchers.Main

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        binding = ActivitySplashBinding.inflate(layoutInflater)
//        val view = binding.root
//        setContentView(view)

        job = Job()
    }

    override fun onDestroy() {
        super.onDestroy()
        job.cancel()
    }


}