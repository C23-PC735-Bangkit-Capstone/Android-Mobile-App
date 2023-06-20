package com.example.tambakapp.ui.sunting

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.tambakapp.R
import com.example.tambakapp.data.db.DeviceResponseDao
import com.example.tambakapp.data.db.PondResponseDao
import com.example.tambakapp.data.db.ResponseDatabase
import com.example.tambakapp.data.db.UserResponseDao
import com.example.tambakapp.databinding.ActivityDeviceSettingsBinding

class DeviceSettingsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDeviceSettingsBinding
    private lateinit var responseDatabase: ResponseDatabase
    private lateinit var pondDao: PondResponseDao
    private lateinit var deviceDao: DeviceResponseDao
    private lateinit var userDao: UserResponseDao

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDeviceSettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        responseDatabase = ResponseDatabase.getInstance(this)
        pondDao = responseDatabase.pondResponseDao()
        deviceDao = responseDatabase.deviceResponseDao()
        userDao = responseDatabase.userResponseDao()

        binding.btnUser.setOnClickListener {
            val typedText = binding.edtUser.text.toString()
            Toast.makeText(this@DeviceSettingsActivity, typedText, Toast.LENGTH_SHORT).show()
        }

    }
}