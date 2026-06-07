package com.example.smartnotebook

import android.content.Context
import android.content.Intent
import com.example.smartnotebook.activities.LoginActivity
import okhttp3.Authenticator
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

    private val apiKeyInterceptor = Interceptor { chain ->
        chain.proceed(chain.request().newBuilder().addHeader("apikey", ANON_KEY).build())
    }

    private val authHeaderInterceptor = Interceptor { chain ->
        val token = if (::appContext.isInitialized) SessionManager.getAccessToken(appContext) else null
        val request = chain.request().newBuilder()
            .addHeader("Authorization", "Bearer ${token ?: ANON_KEY}")
            .build()
        chain.proceed(request)
    }

    // Em 401 (access_token expirado), troca o refresh_token por um novo par e repete a chamada.
    // Se a renovação falhar, limpa a sessão e manda o usuário de volta para o Login.
    private val tokenAuthenticator = Authenticator { _, response ->
        if (contagemDeTentativas(response) >= 2 || !::appContext.isInitialized) return@Authenticator null

        val refreshToken = SessionManager.getRefreshToken(appContext)
        val novoPar = refreshToken?.let {
            try {
                refreshAuthApi.renovarToken(RefreshTokenRequest(it)).execute().takeIf { r -> r.isSuccessful }?.body()
            } catch (e: Exception) {
                null
            }
        }

        val novoAccessToken = novoPar?.accessToken
        if (novoAccessToken == null) {
            forcarLogout()
            return@Authenticator null
        }

        SessionManager.atualizarTokens(appContext, novoAccessToken, novoPar.refreshToken)
        response.request().newBuilder()
            .header("Authorization", "Bearer $novoAccessToken")
            .build()
    }

    private fun contagemDeTentativas(response: Response): Int {
        var contagem = 1
        var anterior = response.priorResponse()
        while (anterior != null) {
            contagem++
            anterior = anterior.priorResponse()
        }
        return contagem
    }

    // Sessão expirada e impossível de renovar: limpa dados locais e reabre a tela de Login
    private fun forcarLogout() {
        SessionManager.logout(appContext)
        val intent = Intent(appContext, LoginActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        appContext.startActivity(intent)
    }

    private val httpClient: OkHttpClient by lazy {
        OkHttpClient.Builder()
            .addInterceptor(apiKeyInterceptor)
            .addInterceptor(authHeaderInterceptor)
            .authenticator(tokenAuthenticator)
            .build()
    }

    // Cliente isolado para renovar o token — sem o Authenticator, evita recursão em loop de 401
    private val refreshHttpClient: OkHttpClient by lazy {
        OkHttpClient.Builder()
            .addInterceptor(apiKeyInterceptor)
            .build()
    }

    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(httpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    private val refreshRetrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(refreshHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val authApi: AuthApi by lazy { retrofit.create(AuthApi::class.java) }
    val restApi: RestApi by lazy { retrofit.create(RestApi::class.java) }
    private val refreshAuthApi: AuthApi by lazy { refreshRetrofit.create(AuthApi::class.java) }
}
