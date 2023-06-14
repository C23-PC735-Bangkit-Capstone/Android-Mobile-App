package com.example.tambakapp.data.response

import com.google.gson.annotations.SerializedName

data class Response(

	@field:SerializedName("Response")
	val response: List<ResponsePondItem>
)

data class ResponsePondItem(

	@field:SerializedName("pond_id")
	val pondId: Int,

	@field:SerializedName("user_id")
	val userId: Int,

	@field:SerializedName("pond_location")
	val pondLocation: String,

	var kincirViewIsVisible: Boolean = false
)
