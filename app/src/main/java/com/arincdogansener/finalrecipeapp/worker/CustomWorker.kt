package com.arincdogansener.finalrecipeapp

import android.content.Context
import android.util.Log
import androidx.work.Data
import androidx.work.ListenableWorker
import androidx.work.Worker
import androidx.work.WorkerParameters

//STE2-1: implement constructor and call base class constructor
class CustomWorker(var context: Context, var workerParams: WorkerParameters):Worker (context, workerParams){
    val tagforLogcat = "WorkerEx"

    override fun doWork(): ListenableWorker.Result {
        //STEP 7: get input
        val num: Int = getInputData().getInt("num", 1)
        val name: String = getInputData().getString("name").toString()
        //context = getApplicationContext();
        return try {
            Log.d(tagforLogcat, "doWork Called, inputs: $num $name")
            var sum = 0
            for (i in 1..num) {
                sum += i
                try {
                    Thread.sleep(100)
                } catch (e: InterruptedException) {
                    throw RuntimeException(e)
                }
            }
            //create the output of worker
            val outputData = Data.Builder().putInt("result", sum).build()
            Utils.sendNotification(context, sum.toString() + "")
            Log.d(tagforLogcat, "End of worker")

            ListenableWorker.Result.success(outputData) //this will return
        } catch (throwable: Throwable) {
            Log.d(tagforLogcat, "Error Sending Notification" + throwable.message)

            ListenableWorker.Result.failure() //this will return
        }
    }
}