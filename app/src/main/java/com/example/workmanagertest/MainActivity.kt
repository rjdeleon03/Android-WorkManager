package com.example.workmanagertest

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.work.Constraints
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkManager
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val networkConstraint = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()
        val workRequest = OneTimeWorkRequest.Builder(MyWorker::class.java)
            .setConstraints(networkConstraint)
            .build()

        buttonEnqueue.setOnClickListener {
            WorkManager.getInstance(this).enqueue(workRequest)
        }

        buttonCancel.setOnClickListener {
            WorkManager.getInstance(this).cancelWorkById(workRequest.id)
        }

        WorkManager.getInstance(this).getWorkInfoByIdLiveData(workRequest.id)
            .observe(this, Observer { workInfo ->
                textViewStatus.append(workInfo.state.name + "\n")
            })

    }
}
