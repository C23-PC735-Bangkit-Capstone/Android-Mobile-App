package com.example.tambakapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.example.tambakapp.databinding.ActivityAddSensorBinding

class AddSensorActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var binding: ActivityAddSensorBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddSensorBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.title = "Tambah Sensor"

    }

    override fun onClick(view: View) {

    }
}