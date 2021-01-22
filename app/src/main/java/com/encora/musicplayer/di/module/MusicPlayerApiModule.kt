package com.encora.musicplayer.di.module

import android.content.Context
import com.encora.musicplayer.BuildConfig
import com.encora.musicplayer.data.remote.api.MusicPlayerService
import com.encora.musicplayer.di.module.MusicPlayerApiModule_ProvideOkHttpClientFactory.provideOkHttpClient
import com.tickaroo.tikxml.TikXml
import com.tickaroo.tikxml.converter.htmlescape.HtmlEscapeStringConverter
import com.tickaroo.tikxml.retrofit.TikXmlConverterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import java.util.concurrent.TimeUnit
import javax.inject.Singleton


@InstallIn(SingletonComponent::class)
@Module
class MusicPlayerApiModule {

    @Singleton
    @Provides
    fun provideRetrofitService(): MusicPlayerService = Retrofit.Builder()
        .baseUrl(MusicPlayerService.MUSIC_PLAYER_API_URL)
        .client(provideOkHttpClient())
        .addConverterFactory(TikXmlConverterFactory.create(
            TikXml.Builder()
                .exceptionOnUnreadXml(false)
                .addTypeConverter(String::class.java, HtmlEscapeStringConverter())
                .build()))
        .build()
        .create(MusicPlayerService::class.java)


    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient {
        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
        val okHttpBuilder = OkHttpClient.Builder()
        okHttpBuilder.connectTimeout(120, TimeUnit.SECONDS)
            .readTimeout(120, TimeUnit.SECONDS)
            .writeTimeout(120, TimeUnit.SECONDS)
//        okHttpBuilder.addInterceptor(RequestInterceptor())
        if (BuildConfig.DEBUG) {
            okHttpBuilder.addInterceptor(loggingInterceptor)
        }
        return okHttpBuilder.build()
    }


}
