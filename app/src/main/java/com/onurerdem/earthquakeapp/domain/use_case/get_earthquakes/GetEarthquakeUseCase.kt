package com.onurerdem.earthquakeapp.domain.use_case.get_earthquakes

import com.onurerdem.earthquakeapp.data.remote.dto.toEarthquakeList
import com.onurerdem.earthquakeapp.domain.model.Earthquake
import com.onurerdem.earthquakeapp.domain.repository.EarthquakeRepository
import com.onurerdem.earthquakeapp.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOError
import javax.inject.Inject

class GetEarthquakeUseCase @Inject constructor(private val repository: EarthquakeRepository) {
    fun executeGetEarthquakes(search: String) : Flow<Resource<List<Earthquake>>> = flow {
        try {
            emit(Resource.Loading())
            val earthquakeList = repository.getEarthquakes()
            emit(Resource.Success(earthquakeList.toEarthquakeList()))
        } catch (e: IOError) {
            emit(Resource.Error(message = "No internet connection!"))
        } catch (e: HttpException) {
            emit(Resource.Error(message = e.localizedMessage ?: "Error!"))
        }
    }
}