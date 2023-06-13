package com.example.tambakapp.ui.rincian

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.app.NotificationCompat
import androidx.core.content.getSystemService
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.tambakapp.*
import com.example.tambakapp.databinding.FragmentRincianBinding
import com.example.tambakapp.ui.kondisi.KondisiViewModel

class RincianFragment : Fragment() {

    companion object {
        private const val NOTIFICATION_ID = 1
        private const val CHANNEL_ID = "channel_01"
        private const val CHANNEL_NAME = "TambakApp Channel"
    }

    private var _binding: FragmentRincianBinding? = null
    private lateinit var binding: FragmentRincianBinding

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
        binding = FragmentRincianBinding.inflate(inflater, container, false)

        val rincianViewModel =
            ViewModelProvider(this).get(RincianViewModel::class.java)

        _binding = FragmentRincianBinding.inflate(inflater, container, false)
        val root: View = binding.root

        /*
        val textView: TextView = binding.textKondisi
        kondisiViewModel.text.observe(viewLifecycleOwner) {
            textView.text = it
        }*/
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnNotification.setOnClickListener {
            sendNotification()
        }
    }

    fun sendNotification() {
        val title = "TambakApp"
        val contentText = "test notifikasi: content text"
        val subText = "test notifikasi: sub text"
        val mNotificationManager = requireContext().getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val mBuilder = NotificationCompat.Builder(requireContext(), CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_notifications_black_24dp)
            .setLargeIcon(BitmapFactory.decodeResource(resources, R.drawable.ic_notifications_black_24dp))
            .setContentTitle(title)
            .setContentText(contentText)
            .setSubText(subText)
            .setAutoCancel(true)

        // untuk Oreo ke atas
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // create or update
            val channel = NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT)
            channel.description = CHANNEL_NAME
            mBuilder.setChannelId(CHANNEL_ID)
            mNotificationManager.createNotificationChannel(channel)
        }

        val notification = mBuilder.build()
        mNotificationManager.notify(NOTIFICATION_ID, notification)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}