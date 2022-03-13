package com.world.edvora_assignment.repository

import com.world.edvora_assignment.Networking.DataState
import com.world.edvora_assignment.Networking.Destination
import com.world.edvora_assignment.Models.Rides
import com.world.edvora_assignment.Models.User


class RidesRepository
constructor(
    private val destination: Destination
) {

    suspend fun get_rides(): DataState<Rides> {
        return try {
            val response = destination.get_rides()
            val result = response.body()
            if (response.isSuccessful) {
                val rides_details = result!!
                DataState.Success(rides_details)
            } else {
                DataState.Error(response.message())
            }
        } catch (e: Exception) {
            DataState.Error(e.message ?: "An error occured")
        }
    }

    suspend fun get_user(): DataState<User> {
        return try {
            val response = destination.get_user()
            val result = response.body()
            if (response.isSuccessful) {
                val rides_details = result!!
                DataState.Success(rides_details)
            } else {
                DataState.Error(response.message())
            }
        } catch (e: Exception) {
            DataState.Error(e.message ?: "An error occured")
        }
    }

}