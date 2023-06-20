package com.example.tambakapp.ui.kondisi

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.work.*
import com.example.tambakapp.*
import com.example.tambakapp.data.db.*
import com.example.tambakapp.data.response.ResponseDeviceItem
import com.example.tambakapp.data.response.ResponsePondItem
import com.example.tambakapp.data.retrofit.ApiConfig
import com.example.tambakapp.databinding.FragmentKondisiBinding
import com.example.tambakapp.worker.ResponseWorker
import kotlinx.coroutines.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.concurrent.TimeUnit

class KondisiFragment : Fragment() {

    companion object {
        private const val TAG = "KondisiFragment"
        var USER_ID = 10001
    }

    private lateinit var rvTambak: RecyclerView
    // private val listTambak = ArrayList<TambakData>()
    // private val listKincir = ArrayList<KincirData>()
    private var _binding: FragmentKondisiBinding? = null
    private lateinit var binding: FragmentKondisiBinding
    private lateinit var tambakViewModel: TambakViewModel
    private lateinit var responseDatabase: ResponseDatabase
    private lateinit var pondDao: PondResponseDao
    private lateinit var deviceDao: DeviceResponseDao
    private lateinit var userDao: UserResponseDao
    private lateinit var workManager: WorkManager
    private lateinit var periodicWorkRequest: PeriodicWorkRequest

    // This property is only valid between onCreateView and
    // onDestroyView.
    // private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentKondisiBinding.inflate(inflater, container, false)
        binding = _binding!!
        rvTambak = binding.rvTambak

        responseDatabase = ResponseDatabase.getInstance(requireContext())
        pondDao = responseDatabase.pondResponseDao()
        deviceDao = responseDatabase.deviceResponseDao()
        userDao = responseDatabase.userResponseDao()

        binding.btnRetry.setOnClickListener {
            showLoadingUnsuccessful(false)
            refreshFragment()
        }

        tambakViewModel = ViewModelProvider(this).get(TambakViewModel::class.java)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.kondisiFragment.setOnRefreshListener {
            getTambakAndKincirData()
            binding.kondisiFragment.isRefreshing = false
        }

        workManager = WorkManager.getInstance(requireContext())
        periodicTask()

        getTambakAndKincirData()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun periodicTask() {
        val repeatInterval = 15
        val timeUnit = TimeUnit.MINUTES
        val workConstraint = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()
        periodicWorkRequest = PeriodicWorkRequest.Builder(ResponseWorker::class.java, repeatInterval.toLong(), timeUnit)
            .setConstraints(workConstraint)
            .build()
        workManager.enqueueUniquePeriodicWork(
            "Attention Work (periodicWorkRequest)",
            ExistingPeriodicWorkPolicy.KEEP,
            periodicWorkRequest
        )
    }

    private fun getTambakAndKincirData() {
        showLoadingUnsuccessful(false)
        showLoading(true)
        // Pond Client
        val pondClient = ApiConfig.getApiService().getListTambak(USER_ID)
        pondClient.enqueue(object : Callback<List<ResponsePondItem>> {
            override fun onResponse(
                call: Call<List<ResponsePondItem>>,
                pondResponse: Response<List<ResponsePondItem>>
            ) {
                showLoading(false)
                if (pondResponse.isSuccessful) {
                    val pondResponseBody = pondResponse.body()
                    val totalCalls = pondResponseBody?.size ?: 0 // detect device calls per pond
                    var completedCalls = 0 // detect device calls per pond
                    val deviceResponses: MutableList<ResponseDeviceItem> = mutableListOf()
                    if (pondResponseBody != null) {
                        for (tambak in pondResponseBody) {
                            showLoading(true)
                            // Device Client
                            val deviceClient = ApiConfig.getApiService().getListKincir(tambak.pondId)
                            deviceClient.enqueue(object : Callback<List<ResponseDeviceItem>> {
                                override fun onResponse(
                                    call: Call<List<ResponseDeviceItem>>,
                                    deviceResponse: Response<List<ResponseDeviceItem>>
                                ) {
                                    showLoading(false)
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
                                        val recyclerPondResponses: MutableList<TambakData> = mutableListOf()
                                        val recyclerDeviceResponses: MutableList<KincirData> = mutableListOf()
                                        lifecycleScope.launch {
                                            withContext(Dispatchers.IO) {
                                                // clear tables
                                                pondDao.clearTable()
                                                deviceDao.clearTable()
                                                // pond responses -> pond entities -> recyclerview-ready pond items
                                                for (pond in sortedPondResponses) {
                                                    pondDao.insertReplace(
                                                        PondResponseEntity(
                                                            pond_id = pond.pondId,
                                                            user_id = pond.userId,
                                                            pond_location = pond.pondLocation
                                                        )
                                                    )
                                                }
                                                for (pond in responseDatabase.pondResponseDao().getAllPondResponses()) {
                                                    recyclerPondResponses.add(
                                                        TambakData(
                                                            pondId = pond.pond_id,
                                                            userId = pond.user_id,
                                                            pondLocation = pond.pond_location,
                                                        )
                                                    )
                                                }
                                                // device responses -> device entities -> recyclerview-ready device items
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
                                                for (kincir in responseDatabase.deviceResponseDao().getAllDeviceResponses()) {
                                                    recyclerDeviceResponses.add(
                                                        KincirData(
                                                            deviceId = kincir.device_id,
                                                            pondId = kincir.pond_id,
                                                            signalStrength = kincir.signal_strength,
                                                            batteryStrength = kincir.battery_strength,
                                                            paddlewheelCondition = kincir.paddlewheel_condition,
                                                            deviceStatus = kincir.device_status,
                                                            monitorStatus = kincir.monitor_status
                                                        )
                                                    )
                                                }
                                            }
                                            showRecyclerList(recyclerPondResponses, recyclerDeviceResponses)
                                        }

                                    }
                                }

                                override fun onFailure(call: Call<List<ResponseDeviceItem>>, t: Throwable) {
                                    showLoading(false)
                                    Log.e(TAG,"onFailure: ${t.message}")
                                    completedCalls++
                                    if (completedCalls == totalCalls) {
                                        val sortedDeviceResponses = deviceResponses.sortedBy { it.deviceId }
                                        val sortedPondResponses = pondResponseBody.sortedBy { it.pondId }
                                        val recyclerPondResponses: MutableList<TambakData> = mutableListOf()
                                        val recyclerDeviceResponses: MutableList<KincirData> = mutableListOf()
                                        lifecycleScope.launch {
                                            withContext(Dispatchers.IO) {
                                                // clear tables
                                                pondDao.clearTable()
                                                deviceDao.clearTable()
                                                // pond responses -> pond entities -> recyclerview-ready pond items
                                                for (pond in sortedPondResponses) {
                                                    pondDao.insertReplace(
                                                        PondResponseEntity(
                                                            pond_id = pond.pondId,
                                                            user_id = pond.userId,
                                                            pond_location = pond.pondLocation
                                                        )
                                                    )
                                                }
                                                for (pond in responseDatabase.pondResponseDao().getAllPondResponses()) {
                                                    recyclerPondResponses.add(
                                                        TambakData(
                                                            pondId = pond.pond_id,
                                                            userId = pond.user_id,
                                                            pondLocation = pond.pond_location,
                                                        )
                                                    )
                                                }
                                                // device responses -> device entities -> recyclerview-ready device items
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
                                                for (kincir in responseDatabase.deviceResponseDao().getAllDeviceResponses()) {
                                                    recyclerDeviceResponses.add(
                                                        KincirData(
                                                            deviceId = kincir.device_id,
                                                            pondId = kincir.pond_id,
                                                            signalStrength = kincir.signal_strength,
                                                            batteryStrength = kincir.battery_strength,
                                                            paddlewheelCondition = kincir.paddlewheel_condition,
                                                            deviceStatus = kincir.device_status,
                                                            monitorStatus = kincir.monitor_status
                                                        )
                                                    )
                                                }
                                            }
                                            showRecyclerList(recyclerPondResponses, recyclerDeviceResponses)
                                        }
                                    }
                                }
                            })
                        }
                        // showRecyclerList(pondResponseBody, deviceResponses)
                    }
                } else {
                    showLoadingUnsuccessful(true)
                    Log.e(TAG,"onFailure: ${pondResponse.message()}")
                }
            }

            override fun onFailure(call: Call<List<ResponsePondItem>>, t: Throwable) {
                showLoadingUnsuccessful(true)
                showLoading(false)
                Log.e(TAG,"onFailure: ${t.message}")
            }
        })
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.progressbar.visibility = View.VISIBLE
        } else {
            binding.progressbar.visibility = View.GONE
        }
    }

    private fun showLoadingUnsuccessful(isLoadingUnsuccessful: Boolean) {
        if (isLoadingUnsuccessful) {
            binding.layoutLoadUnsuccessful.visibility = View.VISIBLE
        } else {
            binding.layoutLoadUnsuccessful.visibility = View.GONE
        }
    }

    private fun refreshFragment() {
        getTambakAndKincirData()
    }

    private suspend fun showRecyclerList(listTambak: List<TambakData>, listKincir: List<KincirData>) {
        rvTambak.layoutManager = LinearLayoutManager(rvTambak.context)
        val listTambakAdapter = ListTambakAdapter(rvTambak.context, tambakViewModel, listTambak, listKincir)
        rvTambak.adapter = listTambakAdapter
    }
}