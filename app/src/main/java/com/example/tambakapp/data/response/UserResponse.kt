package com.example.tambakapp.data.response

data class UserResponse(
	val userResponse: List<UserResponseItem>
)

data class UserResponseItem(
	val userInfos: String,
	val alarmSound: String,
	val userId: Int,
	val notificationSound: String,
	val contacts: String
)

