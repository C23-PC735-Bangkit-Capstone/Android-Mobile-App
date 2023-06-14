package com.example.tambakapp.data.response

import com.google.gson.annotations.SerializedName

data class ResponseDevice(

	@field:SerializedName("ResponseDevice")
	val responseDevice: List<ResponseDeviceItem>
)

data class ResponseDeviceItem(

	@field:SerializedName("device_status")
	val deviceStatus: Boolean,

	@field:SerializedName("device_id")
	val deviceId: Int,

	@field:SerializedName("battery_strength")
	val batteryStrength: Int,

	@field:SerializedName("pond_id")
	val pondId: Int,

	@field:SerializedName("paddlewheel_condition")
	val paddlewheelCondition: String,

	@field:SerializedName("signal_strength")
	val signalStrength: Int,

	@field:SerializedName("monitor_status")
	val monitorStatus: Boolean
)
