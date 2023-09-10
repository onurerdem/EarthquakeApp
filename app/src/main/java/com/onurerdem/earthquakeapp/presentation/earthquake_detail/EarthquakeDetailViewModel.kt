package com.onurerdem.earthquakeapp.presentation.earthquake_detail

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.onurerdem.earthquakeapp.domain.use_case.get_earthquake_detail.GetEarthquakeDetailsUseCase
import com.onurerdem.earthquakeapp.util.Constants.EARTHQUAKE_ID
import com.onurerdem.earthquakeapp.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class EarthquakeDetailViewModel @Inject constructor(
    private val getEarthquakeDetailsUseCase: GetEarthquakeDetailsUseCase,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _state = mutableStateOf<EarthquakeDetailState>(EarthquakeDetailState())
    val state : State<EarthquakeDetailState> = _state

    init {
        savedStateHandle.get<String>(EARTHQUAKE_ID)?.let {
            getEarthquakeDetail(it)
        }
    }

    private fun getEarthquakeDetail(earthquakeId: String) {
        getEarthquakeDetailsUseCase.executeGetEarthquakeDetails(earthquakeId = earthquakeId).onEach {
            when (it) {
                is Resource.Success -> {
                    _state.value = EarthquakeDetailState(earthquake = it.data)
                }

                is Resource.Error -> {
                    _state.value = EarthquakeDetailState(error = it.message ?: "Error!")

                }

                is Resource.Loading -> {
                    _state.value = EarthquakeDetailState(isLoading = true)
                }
            }
        }.launchIn(viewModelScope)
    }

}