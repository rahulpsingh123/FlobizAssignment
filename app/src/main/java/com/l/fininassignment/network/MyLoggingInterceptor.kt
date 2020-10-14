package com.l.fininassignment.network


import com.ihsanbal.logging.LoggingInterceptor
import io.github.inflationx.calligraphy3.BuildConfig
import okhttp3.internal.platform.Platform


internal object MyLoggingInterceptor {
    fun provideOkHttpLogging(): LoggingInterceptor {
        return LoggingInterceptor.Builder()
            .loggable(BuildConfig.DEBUG)
            .log(Platform.INFO)
            .request("Request")
            .response("Response")
            .build()
    }
}
