package com.example.softpos_payment.di

import com.example.softpos_payment.data.remote.PaymentApi
import com.example.softpos_payment.data.repository.PaymentRepositoryImpl
import com.example.softpos_payment.domain.repository.PaymentRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    private const val BASE_URL =
        "https://payments-api-intern.free.beeceptor.com/"

    @Provides
    @Singleton
    fun provideLoggingInterceptor(): HttpLoggingInterceptor {

        return HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
    }

    @Provides
    @Singleton
    fun provideHeadersInterceptor(): Interceptor {

        return Interceptor { chain ->

            val request = chain.request()
                .newBuilder()
                .header(
                    "Accept",
                    "application/json"
                )
                .build()

            chain.proceed(request)
        }
    }

    @Provides
    @Singleton
    fun provideOkHttpClient(
        loggingInterceptor: HttpLoggingInterceptor,
        headersInterceptor: Interceptor
    ): OkHttpClient {

        return OkHttpClient.Builder()
            .addInterceptor(headersInterceptor)
            .addInterceptor(loggingInterceptor)
            .connectTimeout(
                30,
                TimeUnit.SECONDS
            )
            .readTimeout(
                30,
                TimeUnit.SECONDS
            )
            .writeTimeout(
                30,
                TimeUnit.SECONDS
            )
            .build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(
        okHttpClient: OkHttpClient
    ): Retrofit {

        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(
                GsonConverterFactory.create()
            )
            .build()
    }

    @Provides
    @Singleton
    fun providePaymentApi(
        retrofit: Retrofit
    ): PaymentApi {

        return retrofit.create(
            PaymentApi::class.java
        )
    }

    @Provides
    @Singleton
    fun providePaymentRepository(
        paymentApi: PaymentApi
    ): PaymentRepository {

        return PaymentRepositoryImpl(
            paymentApi
        )
    }
}