package com.onurerdem.earthquakeapp.presentation.earthquakes

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.onurerdem.earthquakeapp.domain.model.Earthquake
import com.onurerdem.earthquakeapp.domain.use_case.get_earthquakes.GetEarthquakeUseCase
import com.onurerdem.earthquakeapp.util.Resource
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import java.util.Locale
import javax.inject.Inject

class EarthquakesViewModel @Inject constructor(
    private val getEarthquakeUseCase: GetEarthquakeUseCase
) : ViewModel() {
    private val _state = mutableStateOf<EarthquakesState>(EarthquakesState())
    val state : State<EarthquakesState> = _state

    private var job : Job? = null

    init {
        getEarthquakes("")
    }

    private fun getEarthquakes(search: String) {
        job?.cancel()

        job = getEarthquakeUseCase.executeGetEarthquakes(search).onEach {
            when(it) {
                is Resource.Success -> {
                    if (search.isBlank() || search.length < 3) {
                        _state.value = EarthquakesState(earthquakes = it.data ?: emptyList())
                    } else {
                        val searchedList: ArrayList<Earthquake> = ArrayList()
                        val searched = search.trim().lowercase()
                        it.let {
                            it.data!!.forEach {
                                if (it.title.lowercase(Locale.ROOT).contains(searched)) {
                                    searchedList.add(it)
                                }
                            }
                        }
                        _state.value = EarthquakesState(earthquakes = searchedList.toList())
                    }
                }

                is Resource.Error -> {
                    _state.value = EarthquakesState(error = it.message ?: "Error!")
                }

                is Resource.Loading -> {
                    _state.value = EarthquakesState(isLoading = true)
                }
            }
        }.launchIn(viewModelScope)
    }

    fun onEvent(event: EarthquakesEvent) {
        when(event) {
            is EarthquakesEvent.Search -> {
                getEarthquakes(event.searchString)
            }
        }
    }
}