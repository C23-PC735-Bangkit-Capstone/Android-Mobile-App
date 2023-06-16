package com.example.tambakapp.worker

import android.content.Context
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.tambakapp.data.db.*
import com.example.tambakapp.data.response.ResponseDeviceItem
import com.example.tambakapp.data.response.ResponsePondItem
import com.example.tambakapp.data.retrofit.ApiConfig
import com.example.tambakapp.ui.kondisi.KondisiFragment
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
    }

    override suspend fun doWork(): Result {
        try {
            responseDatabase = ResponseDatabase.getInstance(applicationContext)
            pondDao = responseDatabase.pondResponseDao()
            deviceDao = responseDatabase.deviceResponseDao()
            userDao = responseDatabase.userResponseDao()

            pondDao.clearTable()
            deviceDao.clearTable()

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

            return Result.success()
        } catch (e: Exception) {
            return Result.failure()
        }
    }

}