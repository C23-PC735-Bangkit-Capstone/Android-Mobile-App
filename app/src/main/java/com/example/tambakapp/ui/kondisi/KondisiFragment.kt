package com.example.tambakapp.ui.kondisi

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.tambakapp.ListKincirAdapter
import com.example.tambakapp.KincirData
import com.example.tambakapp.ListTambakAdapter
import com.example.tambakapp.TambakData
import com.example.tambakapp.databinding.FragmentKondisiBinding

class KondisiFragment : Fragment() {

    private lateinit var rvTambak: RecyclerView
    private val listKincir = ArrayList<KincirData>()
    private val listTambak = ArrayList<TambakData>()
    private var _binding: FragmentKondisiBinding? = null
    private lateinit var binding: FragmentKondisiBinding

    // This property is only valid between onCreateView and
    // onDestroyView.
    // private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        listTambak.add(TambakData(1, "TAMBAK 1", 20, 100))
        listTambak.add(TambakData(2, "TAMBAK 2", 99, 26))
        listTambak.add(TambakData(3, "TAMBAK 3", 1, 100))
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
        binding = FragmentKondisiBinding.inflate(inflater, container, false)
        rvTambak = binding.rvTambak

        val kondisiViewModel =
            ViewModelProvider(this).get(KondisiViewModel::class.java)

        _binding = FragmentKondisiBinding.inflate(inflater, container, false)
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
        val listTambakAdapter = ListTambakAdapter(listTambak, listKincir, requireContext())
        rvTambak.adapter = listTambakAdapter

        /*
        rvKincir.layoutManager = LinearLayoutManager(requireContext())
        val listKincirAdapter = ListKincirAdapter(listKincir)
        rvKincir.adapter = listKincirAdapter
         */
    }
}