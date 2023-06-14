package com.example.tambakapp.data.retrofit

import com.example.tambakapp.data.response.ResponseDeviceItem
import com.example.tambakapp.data.response.ResponsePondItem
import retrofit2.Call
import retrofit2.http.*

interface ApiService {

    @GET("ponds/{user_id}")
    fun getListTambak(
        @Path("user_id") user_id: Int
    ) : Call<List<ResponsePondItem>>

    @GET("device/{pond_id}")
    fun getListKincir(
        @Path("pond_id") pond_id: Int
    ) : Call<List<ResponseDeviceItem>>

    /*
    @GET("users/{login}")
    fun getUserDetails(
        @Path("login") login: String
    ) : Call<GithubDetailsResponse>

    @GET("users/{login}/followers")
    fun getUserFollowers(
        @Path("login") login: String
    ) : Call<List<GithubFollowResponseItem>>

    @GET("users/{login}/following")
    fun getUserFollowing(
        @Path("login") login: String
    ) : Call<List<GithubFollowResponseItem>>
     */
}