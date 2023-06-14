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
    private val listKincir = ArrayList<KincirData>()
    private var _binding: FragmentKondisiBinding? = null
    private lateinit var binding: FragmentKondisiBinding
    private lateinit var dropDownViewModel: DropDownViewModel

    // This property is only valid between onCreateView and
    // onDestroyView.
    // private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        /*
        listTambak.add(TambakData(1, "TAMBAK 1", 20, 100))
        listTambak.add(TambakData(2, "TAMBAK 2", 99, 26))
        listTambak.add(TambakData(3, "TAMBAK 3", 1, 100))
         */
        listKincir.add(KincirData("Kincir 1a",10001001, 100, "Baik", "Baik"))
        listKincir.add(KincirData("Kincir 1b",10001001, 45, "Rusak", "Inkonsisten"))
        listKincir.add(KincirData("Kincir 2a",10001002, 100, "Baik", "Baik"))
        listKincir.add(KincirData("Kincir 2b",10001002, 45, "Rusak", "Inkonsisten"))
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

        getListTambak()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun getListTambak() {
        showLoading(true)
        val client = ApiConfig.getApiService().getListTambak(USER_ID)
        client.enqueue(object : Callback<List<ResponsePondItem>> {
            override fun onResponse(
                call: Call<List<ResponsePondItem>>,
                response: Response<List<ResponsePondItem>>
            ) {
                showLoading(false)
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null) {
                        setTambakData(responseBody)
                    }
                } else {
                    Log.e(TAG,"onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<List<ResponsePondItem>>, t: Throwable) {
                showLoading(false)
                Log.e(TAG,"onFailure: ${t.message}")
            }
        })
    }

    private fun setTambakData(list: List<ResponsePondItem>) {
        // dropDownViewModel.setItemList(list)
        showRecyclerList(list)
        /*
        for (user in userList) {
            listUser.add(
                User(
                    username = user.login,
                    photo = user.avatarUrl
                )
            )
        }
        if (list.size == 0) {
            Toast.makeText(this@MainActivity,"No result for $USERNAME_QUERY", Toast.LENGTH_SHORT).show()
        }
        val adapter = ListUserAdapter(listUser)
        binding.rvUsers.adapter = adapter

        adapter.setOnItemClickCallback(object : ListUserAdapter.OnItemClickCallback {
            override fun onItemClicked(data: User) {
                showSelectedUser(data)
            }
        })

         */
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.progressbar.visibility = View.VISIBLE
        } else {
            binding.progressbar.visibility = View.GONE
        }
    }

    private fun showRecyclerList(list: List<ResponsePondItem>) {
        rvTambak.layoutManager = LinearLayoutManager(rvTambak.context)
        val listTambakAdapter = ListTambakAdapter(listKincir, rvTambak.context, dropDownViewModel, list)
        rvTambak.adapter = listTambakAdapter
    }
}