package com.onurerdem.earthquakeapp.domain.use_case.get_splash

import androidx.annotation.Nullable
import com.onurerdem.earthquakeapp.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOError
import javax.inject.Inject

class GetSplashUseCase @Inject constructor() {
    fun executeGetSplash() : Flow<Resource<Nullable>> = flow {
        try {
            emit(Resource.Loading())
        } catch (e: IOError) {
            emit(Resource.Error(message = "No internet connection!"))
        } catch (e: HttpException) {
            emit(Resource.Error(message = e.localizedMessage ?: "Error!"))
        }
    }
}