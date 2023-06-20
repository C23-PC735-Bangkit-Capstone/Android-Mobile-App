package com.example.tambakapp.ui.sunting

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.tambakapp.AddSensorActivity
import com.example.tambakapp.databinding.FragmentSuntingBinding

class SuntingFragment : Fragment() {

    private lateinit var binding: FragmentSuntingBinding
    private var _binding: FragmentSuntingBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    // private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSuntingBinding.inflate(inflater, container, false)

        val suntingViewModel =
            ViewModelProvider(this).get(SuntingViewModel::class.java)

        _binding = FragmentSuntingBinding.inflate(inflater, container, false)
        val root: View = binding.root

        /*
        val textView: TextView = binding.textSunting
        suntingViewModel.text.observe(viewLifecycleOwner) {
            textView.text = it
        }
         */
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnAddSensor.setOnClickListener {
            val intent = Intent(requireContext(), AddSensorActivity::class.java)
            startActivity(intent)
        }

        binding.btnEdtDevice.setOnClickListener {
            val intent = Intent(requireContext(), DeviceSettingsActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}