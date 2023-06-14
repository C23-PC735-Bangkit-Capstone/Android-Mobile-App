package com.example.tambakapp.ui.kondisi

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.tambakapp.*
import com.example.tambakapp.data.response.ResponseDeviceItem
import com.example.tambakapp.data.response.ResponsePondItem
import com.example.tambakapp.data.retrofit.ApiConfig
import com.example.tambakapp.databinding.FragmentKondisiBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

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
    private lateinit var dropDownViewModel: DropDownViewModel

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

        dropDownViewModel = ViewModelProvider(this).get(DropDownViewModel::class.java)
        // dropDownViewModel.setItemList(listTambak)

        // showRecyclerList()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        getTambakAndKincirData()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun getTambakAndKincirData() {
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
                                        Log.e(TAG,"onFailure: ${pondResponse.message()}")
                                    }
                                }

                                override fun onFailure(call: Call<List<ResponseDeviceItem>>, t: Throwable) {
                                    showLoading(false)
                                    Log.e(TAG,"onFailure: ${t.message}")
                                }
                            })
                        }
                        showRecyclerList(pondResponseBody, deviceResponses)
                    }
                } else {
                    Log.e(TAG,"onFailure: ${pondResponse.message()}")
                }
            }

            override fun onFailure(call: Call<List<ResponsePondItem>>, t: Throwable) {
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

    private fun showRecyclerList(listTambak: List<ResponsePondItem>, listKincir: List<ResponseDeviceItem>) {
        rvTambak.layoutManager = LinearLayoutManager(rvTambak.context)
        val listTambakAdapter = ListTambakAdapter(rvTambak.context, dropDownViewModel, listTambak, listKincir)
        rvTambak.adapter = listTambakAdapter
    }
}