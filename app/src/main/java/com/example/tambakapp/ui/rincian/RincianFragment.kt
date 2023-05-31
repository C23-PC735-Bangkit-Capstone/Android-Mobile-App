package com.example.tambakapp.ui.rincian

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.tambakapp.*
import com.example.tambakapp.databinding.FragmentRincianBinding
import com.example.tambakapp.ui.kondisi.KondisiViewModel

class RincianFragment : Fragment() {

    private lateinit var rvTambak: RecyclerView
    private val listTambak = ArrayList<TambakData>()
    private val listKincir = ArrayList<KincirData>()
    private var _binding: FragmentRincianBinding? = null
    private lateinit var binding: FragmentRincianBinding

    // This property is only valid between onCreateView and
    // onDestroyView.
    // private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        listTambak.add(TambakData(1, "Tambak 1", 20, 100))
        listTambak.add(TambakData(2, "Tambak 2", 99, 26))
        listTambak.add(TambakData(3, "Tambak 3", 1, 55))
        listKincir.add(KincirData("Kincir 1a",1, 100, "Baik", "Baik"))
        listKincir.add(KincirData("Kincir 1b",1, 45, "Rusak", "Inkonsisten"))
        listKincir.add(KincirData("Kincir 2a",2, 100, "Baik", "Baik"))
        listKincir.add(KincirData("Kincir 2b",2, 45, "Rusak", "Inkonsisten"))
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentRincianBinding.inflate(inflater, container, false)
        rvTambak = binding.rvTambakDetail

        val rincianViewModel =
            ViewModelProvider(this).get(RincianViewModel::class.java)

        _binding = FragmentRincianBinding.inflate(inflater, container, false)
        val root: View = binding.root


        showRecyclerList()

        /*
        val textView: TextView = binding.textKondisi
        kondisiViewModel.text.observe(viewLifecycleOwner) {
            textView.text = it
        }*/
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun showRecyclerList() {
        rvTambak.layoutManager = LinearLayoutManager(requireContext())
        val listTambakDetailAdapter = ListTambakDetailAdapter(listTambak, listKincir, requireContext())
        rvTambak.adapter = listTambakDetailAdapter
    }
}