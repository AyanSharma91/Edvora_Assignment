package com.world.edvora_assignment.Networking

import com.world.edvora_assignment.Models.Rides
import com.world.edvora_assignment.Models.User
import retrofit2.Response
import retrofit2.http.GET


interface Destination {

    @GET("rides")
    suspend fun  get_rides() : Response<Rides>

    @GET("user")
    suspend fun  get_user() : Response<User>


}
