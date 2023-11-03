package com.onurerdem.earthquakeapp.presentation.onboarding

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.onurerdem.earthquakeapp.domain.use_case.get_onboarding.GetOnboardingUseCase
import com.onurerdem.earthquakeapp.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

 @HiltViewModel
class OnboardingViewModel @Inject constructor(
    private val getOnboardingUseCase: GetOnboardingUseCase
) : ViewModel() {

    private val _state = mutableStateOf<OnboardingState>(OnboardingState())
    val state : State<OnboardingState> = _state

    init {
        getOnboarding()
    }

    private fun getOnboarding() {
        getOnboardingUseCase.executeGetOnboarding().onEach {
            when (it) {
                is Resource.Success -> {
                    _state.value = OnboardingState()
                }

                is Resource.Error -> {
                    _state.value = OnboardingState(error = it.message ?: "Error!")
                }

                is Resource.Loading -> {
                    _state.value = OnboardingState(isLoading = true)
                }
            }
        }.launchIn(viewModelScope)
    }
}