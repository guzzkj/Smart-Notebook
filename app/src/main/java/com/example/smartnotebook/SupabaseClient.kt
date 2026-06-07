package com.example.smartnotebook

import android.content.Context
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

// Cliente Supabase via Retrofit — substitui o antigo cliente que apontava para XAMPP/PHP
// Toda chamada precisa do header "apikey" (chave do projeto) e "Authorization" (Bearer):
//   - sem sessão: usa a própria anon key (acesso público controlado por RLS)
//   - com sessão: usa o access_token do usuário logado, para o PostgREST aplicar RLS via auth.uid()
object SupabaseClient {
    private const val BASE_URL = BuildConfig.SUPABASE_URL + "/"
    private const val ANON_KEY = BuildConfig.SUPABASE_ANON_KEY

    private lateinit var appContext: Context

    fun init(context: Context) {
        appContext = context.applicationContext
    }

    private val authHeaderInterceptor = Interceptor { chain ->
        val token = if (::appContext.isInitialized) SessionManager.getAccessToken(appContext) else null
        val request = chain.request().newBuilder()
            .addHeader("apikey", ANON_KEY)
            .addHeader("Authorization", "Bearer ${token ?: ANON_KEY}")
            .build()
        chain.proceed(request)
    }

    private val httpClient: OkHttpClient by lazy {
        OkHttpClient.Builder()
            .addInterceptor(authHeaderInterceptor)
            .build()
    }

    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(httpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val authApi: AuthApi by lazy { retrofit.create(AuthApi::class.java) }
    val restApi: RestApi by lazy { retrofit.create(RestApi::class.java) }
}
