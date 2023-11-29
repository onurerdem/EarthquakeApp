package com.onurerdem.earthquakeapp.presentation.earthquakes

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseAuth
import com.onurerdem.earthquakeapp.domain.model.Earthquake
import com.onurerdem.earthquakeapp.domain.use_case.get_earthquakes.GetEarthquakeUseCase
import com.onurerdem.earthquakeapp.presentation.Screen
import com.onurerdem.earthquakeapp.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class EarthquakesViewModel @Inject constructor(
    private val getEarthquakeUseCase: GetEarthquakeUseCase
) : ViewModel() {
    private val _state = mutableStateOf<EarthquakesState>(EarthquakesState())
    val state: State<EarthquakesState> = _state

    private val _isRefreshing = MutableStateFlow(false)
    val isRefreshing: StateFlow<Boolean>
        get() = _isRefreshing.asStateFlow()

    private var job: Job? = null

    private val TAG = EarthquakesViewModel::class.simpleName

    val isUserLoggedIn: MutableLiveData<Boolean> = MutableLiveData()

    val emailId: MutableLiveData<String> = MutableLiveData()

    init {
        getEarthquakes("")
    }

    private fun getEarthquakes(search: String) {
        job?.cancel()

        job = getEarthquakeUseCase.executeGetEarthquakes(search).onEach {
            when (it) {
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
                    _state.value = EarthquakesState(
                        error = it.message ?: "Error!"
                    )
                }

                is Resource.Loading -> {
                    _state.value = EarthquakesState(isLoading = true)
                }
            }

            _isRefreshing.emit(false)
        }.launchIn(viewModelScope)
    }

    fun onEvent(event: EarthquakesEvent) {
        when (event) {
            is EarthquakesEvent.Search -> {
                getEarthquakes(event.searchString)
            }
        }
    }

    fun logout(navController: NavController) {

        val firebaseAuth = FirebaseAuth.getInstance()

        firebaseAuth.signOut()

        val authStateListener = FirebaseAuth.AuthStateListener {
            if (it.currentUser == null) {
                Log.d(TAG, "Inside sign outsuccess")
                navController.navigate(Screen.RegisterScreen.route)
            } else {
                Log.d(TAG, "Inside sign out is not complete")
            }
        }

        firebaseAuth.addAuthStateListener(authStateListener)

    }

    fun checkForActiveSession() {
        if (FirebaseAuth.getInstance().currentUser != null) {
            Log.d(TAG, "Valid session")
            isUserLoggedIn.value = true
        } else {
            Log.d(TAG, "User is not logged in")
            isUserLoggedIn.value = false
        }
    }

    fun getUserData() {
        FirebaseAuth.getInstance().currentUser?.also {
            it.email?.also { email ->
                emailId.value = email
            }
        }
    }
}