package com.onurerdem.earthquakeapp.domain.use_case.get_earthquake_detail

import com.onurerdem.earthquakeapp.data.remote.dto.toEarthquakeDetail
import com.onurerdem.earthquakeapp.domain.model.EarthquakeDetail
import com.onurerdem.earthquakeapp.domain.repository.EarthquakeRepository
import com.onurerdem.earthquakeapp.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOError
import javax.inject.Inject

class GetEarthquakeDetailsUseCase @Inject constructor(private val repository: EarthquakeRepository) {
    fun executeGetEarthquakeDetails(earthquakeId : String) : Flow<Resource<EarthquakeDetail>> = flow {
        try {
            emit(Resource.Loading())
            val earthquakeDetail = repository.getEarthquakeDetail(earthquakeId = earthquakeId)
            emit(Resource.Success(earthquakeDetail.toEarthquakeDetail()))
        } catch (e: IOError) {
            emit(Resource.Error(message = "No internet connection!"))
        } catch (e: HttpException) {
            emit(Resource.Error(message = e.localizedMessage ?: "Error!"))
        }
    }
}