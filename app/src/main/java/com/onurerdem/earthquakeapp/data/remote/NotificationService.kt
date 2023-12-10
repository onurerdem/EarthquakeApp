package com.onurerdem.earthquakeapp.data.remote

import com.onurerdem.earthquakeapp.domain.model.PushNotification
import com.onurerdem.earthquakeapp.util.Constants.CONTENT_TYPE
import com.onurerdem.earthquakeapp.util.Constants.SERVER_KEY
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface NotificationService {

    @Headers("Authorization: key=$SERVER_KEY", "Content-Type:$CONTENT_TYPE")
    @POST("fcm/send")
    suspend fun postNotification(
        @Body notificaiton : PushNotification
    ) : Response<ResponseBody>

}