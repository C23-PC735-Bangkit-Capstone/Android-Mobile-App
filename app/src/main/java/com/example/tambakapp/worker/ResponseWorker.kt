package com.example.tambakapp.worker

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.media.AudioAttributes
import android.media.RingtoneManager
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.tambakapp.MainActivity
import com.example.tambakapp.R
import com.example.tambakapp.data.db.*
import com.example.tambakapp.data.response.ResponseDeviceItem
import com.example.tambakapp.data.response.ResponsePondItem
import com.example.tambakapp.data.retrofit.ApiConfig
import com.example.tambakapp.ui.rincian.RincianFragment
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ResponseWorker(context: Context, params: WorkerParameters) : CoroutineWorker(context, params) {

    private lateinit var responseDatabase: ResponseDatabase
    private lateinit var pondDao: PondResponseDao
    private lateinit var deviceDao: DeviceResponseDao
    private lateinit var userDao: UserResponseDao

    companion object {
        private const val TAG = "ResponseWorker"
        private const val NOTIFICATION_ID = 1
        private const val CHANNEL_ID = "channel_01"
        private const val CHANNEL_NAME = "Attention Channel"
    }

    override suspend fun doWork(): Result {
        try {
            responseDatabase = ResponseDatabase.getInstance(applicationContext)
            pondDao = responseDatabase.pondResponseDao()
            deviceDao = responseDatabase.deviceResponseDao()
            userDao = responseDatabase.userResponseDao()

            // RETROFIT FOR BACKGROUND PROCESS
            // Pond Client
            val pondClient = ApiConfig.getApiService().getListTambak(10001)
            pondClient.enqueue(object : Callback<List<ResponsePondItem>> {
                override fun onResponse(
                    call: Call<List<ResponsePondItem>>,
                    pondResponse: Response<List<ResponsePondItem>>
                ) {
                    if (pondResponse.isSuccessful) {
                        val pondResponseBody = pondResponse.body()
                        val totalCalls = pondResponseBody?.size ?: 0 // detect device calls per pond
                        var completedCalls = 0 // detect device calls per pond
                        val deviceResponses: MutableList<ResponseDeviceItem> = mutableListOf()
                        if (pondResponseBody != null) {
                            for (tambak in pondResponseBody) {
                                // Device Client
                                val deviceClient = ApiConfig.getApiService().getListKincir(tambak.pondId)
                                deviceClient.enqueue(object : Callback<List<ResponseDeviceItem>> {
                                    override fun onResponse(
                                        call: Call<List<ResponseDeviceItem>>,
                                        deviceResponse: Response<List<ResponseDeviceItem>>
                                    ) {
                                        if (deviceResponse.isSuccessful) {
                                            val deviceResponseBody = deviceResponse.body()
                                            if (deviceResponseBody != null) {
                                                for (device in deviceResponseBody) {
                                                    deviceResponses.add(device)
                                                }
                                            }
                                        } else {
                                            Log.e(TAG,"onFailure: ${deviceResponse.message()}")
                                        }
                                        completedCalls++
                                        if (completedCalls == totalCalls) {
                                            val sortedDeviceResponses = deviceResponses.sortedBy { it.deviceId }
                                            val sortedPondResponses = pondResponseBody.sortedBy { it.pondId }
                                            CoroutineScope(Dispatchers.IO).launch {
                                                pondDao.clearTable()
                                                deviceDao.clearTable()
                                                for (pond in sortedPondResponses) {
                                                    pondDao.insertReplace(
                                                        PondResponseEntity(
                                                            pond_id = pond.pondId,
                                                            user_id = pond.userId,
                                                            pond_location = pond.pondLocation
                                                        )
                                                    )
                                                }
                                                for (device in sortedDeviceResponses) {
                                                    deviceDao.insertReplace(
                                                        DeviceResponseEntity(
                                                            device_id = device.deviceId,
                                                            pond_id = device.pondId,
                                                            signal_strength = device.signalStrength,
                                                            battery_strength = device.batteryStrength,
                                                            paddlewheel_condition = device.paddlewheelCondition,
                                                            device_status = device.deviceStatus,
                                                            monitor_status = device.monitorStatus
                                                        )
                                                    )
                                                }
                                            }

                                            // showRecyclerList(sortedPondResponses, sortedDeviceResponses)
                                        }
                                    }

                                    override fun onFailure(call: Call<List<ResponseDeviceItem>>, t: Throwable) {
                                        Log.e(TAG,"onFailure: ${t.message}")
                                        completedCalls++
                                        if (completedCalls == totalCalls) {
                                            val sortedDeviceResponses = deviceResponses.sortedBy { it.deviceId }
                                            val sortedPondResponses = pondResponseBody.sortedBy { it.pondId }
                                            CoroutineScope(Dispatchers.IO).launch {
                                                pondDao.clearTable()
                                                deviceDao.clearTable()
                                                for (pond in sortedPondResponses) {
                                                    pondDao.insertReplace(
                                                        PondResponseEntity(
                                                            pond_id = pond.pondId,
                                                            user_id = pond.userId,
                                                            pond_location = pond.pondLocation
                                                        )
                                                    )
                                                }
                                                for (device in sortedDeviceResponses) {
                                                    deviceDao.insertReplace(
                                                        DeviceResponseEntity(
                                                            device_id = device.deviceId,
                                                            pond_id = device.pondId,
                                                            signal_strength = device.signalStrength,
                                                            battery_strength = device.batteryStrength,
                                                            paddlewheel_condition = device.paddlewheelCondition,
                                                            device_status = device.deviceStatus,
                                                            monitor_status = device.monitorStatus
                                                        )
                                                    )
                                                }
                                            }
                                        }
                                    }
                                })
                            }
                            // showRecyclerList(pondResponseBody, deviceResponses)
                        }
                    } else {
                        Log.e(TAG,"onFailure: ${pondResponse.message()}")
                    }
                }

                override fun onFailure(call: Call<List<ResponsePondItem>>, t: Throwable) {
                    Log.e(TAG,"onFailure: ${t.message}")
                }
            })

            val tambakAttentions: MutableList<Int> = mutableListOf()
            val kincirAttentions: MutableList<Int> = mutableListOf()
            for (attention in responseDatabase.deviceResponseDao().getAllDeviceResponses()) {
                if (attention.paddlewheel_condition == "Bad" ||
                    attention.battery_strength <= 60 ||
                    attention.signal_strength <= 60) {
                    if (!kincirAttentions.contains(attention.device_id)) kincirAttentions.add(attention.device_id)
                    if (!tambakAttentions.contains(attention.pond_id)) tambakAttentions.add(attention.pond_id)
                }
            }

            if (!(tambakAttentions.size == 0 && kincirAttentions.size == 0)) {
                sendNotification(tambakAttentions.size, kincirAttentions.size)
            } else {
                Log.e(TAG, "ERROR: ${tambakAttentions.size} | ${kincirAttentions.size}\n\n${responseDatabase.deviceResponseDao().getAllDeviceResponses()}")
            }
            return Result.success()
        } catch (e: Exception) {
            return Result.failure()
        }


    }

    fun sendNotification(tambakCount: Int, kincirCount: Int) {
        val title = "TambakApp"
        val contentText = "$tambakCount tambak dan $kincirCount kincir butuh perhatian"
        val soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        //val subText = "test notifikasi: sub text"

        val intent = Intent(applicationContext, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP)
        val pendingIntent = PendingIntent.getActivity(
            applicationContext,
            0,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )

        val mNotificationManager = applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val mBuilder = NotificationCompat.Builder(applicationContext, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_notifications_black_24dp)
            .setContentTitle(title)
            .setContentText(contentText)
            .setSound(soundUri) // Set the sound for the notification
            .setPriority(NotificationCompat.PRIORITY_HIGH) // Set the priority of the notification
            .setVibrate(longArrayOf(1000, 1000, 1000, 1000, 1000))
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)

        // untuk Oreo ke atas
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // create or update
            val channel = NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT)
            channel.description = CHANNEL_NAME

            val audioAttributes = AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_NOTIFICATION)
                .build()
            channel.setSound(soundUri, audioAttributes)

            channel.enableVibration(true)
            channel.vibrationPattern = longArrayOf(1000, 1000, 1000, 1000, 1000)

            mBuilder.setChannelId(CHANNEL_ID)
            mNotificationManager.createNotificationChannel(channel)
        }

        val notification = mBuilder.build()
        mNotificationManager.notify(NOTIFICATION_ID, notification)
    }

}