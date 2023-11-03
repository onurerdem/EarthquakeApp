package com.onurerdem.earthquakeapp.presentation.splash

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.onurerdem.earthquakeapp.domain.use_case.get_splash.GetSplashUseCase
import com.onurerdem.earthquakeapp.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val getSplashUseCase: GetSplashUseCase
) : ViewModel() {

    private val _state = mutableStateOf<SplashState>(SplashState())
    val state : State<SplashState> = _state

    init {
        getSplash()
    }

    private fun getSplash() {
        getSplashUseCase.executeGetSplash().onEach {
            when (it) {
                is Resource.Success -> {
                    _state.value = SplashState()
                }

                is Resource.Error -> {
                    _state.value = SplashState(error = it.message ?: "Error!")
                }

                is Resource.Loading -> {
                    _state.value = SplashState(isLoading = true)
                }
            }
        }.launchIn(viewModelScope)
    }
}