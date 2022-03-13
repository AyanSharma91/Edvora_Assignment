package com.world.edvora_assignment.ui.ViewModel


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.world.edvora_assignment.Networking.DataState
import com.world.edvora_assignment.Models.Rides
import com.world.edvora_assignment.repository.RidesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RidesViewModel
@Inject
constructor(
    private val ridesRepository: RidesRepository
) : ViewModel() {


    private val _dataState = MutableStateFlow<MainStateEvent>(MainStateEvent.None)
    val dataState: StateFlow<MainStateEvent> = _dataState


    fun get_rides_details() {

        viewModelScope.launch {
            _dataState.value = MainStateEvent.Loading
            when (val rideDetails = ridesRepository.get_rides()) {
                is DataState.Error -> _dataState.value =
                    MainStateEvent.Failure(rideDetails.message!!)
                is DataState.Success -> {
                    _dataState.value = MainStateEvent.Success(rideDetails.data!!)
                }
            }
        }
    }


    //Just a separate class to handle the result in view Model
    sealed class MainStateEvent {

        class Success(val result: Rides) : MainStateEvent()
        class Failure(val errorText: String) : MainStateEvent()
        object Loading : MainStateEvent()
        object None : MainStateEvent()
    }
}