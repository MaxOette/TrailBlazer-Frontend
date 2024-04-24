package de.max.trailblazerfrontendv1.Api

import de.max.trailblazerfrontendv1.Util.UserConstants
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.ResponseBody
import retrofit2.Call
//import retrofit2.Response
import okhttp3.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Path

interface HealthService {
    @GET("/dev/health")
    suspend fun getHealth(): Boolean
}

interface RegisterService{
    @POST("/api/v1/auth/register")
    suspend fun postRegisterUser(
        @Body registerUserData: RegisterUserData
    )
}

interface LoginService{
    @POST("/api/v1/auth/login")
    suspend fun postLoginUser(
        @Body loginUserData: LoginUserData
    ): ActiveUserData
}

interface RefreshService{
    @POST("/api/v1/auth/token/refresh")
    suspend fun requestRefreshToken(
    ): ResponseBody
}

interface AuthStatusService{
    @GET("/api/v1/auth/status")
    suspend fun getAuthStatus(): ResponseBody
}

interface LogoutService{
    @POST("/api/v1/auth/logout")
    suspend fun logout()
}

interface TileService{
    @GET("/api/v1/locations")
    suspend fun getTiles() : TileData
}

object TileApi{
    private val httpClient = OkHttpClient.Builder()
        .addInterceptor(TokenInterceptor())
        .retryOnConnectionFailure(true)
        .build()
    private val retrofit = Retrofit.Builder()
        .baseUrl("http://195.201.42.22:8080/")
        .addConverterFactory(GsonConverterFactory.create())
        .client(TileApi.httpClient)
        .build()
    val tileService: TileService = retrofit.create(TileService::class.java)
}

object LogoutAPI{
    private val httpClient = OkHttpClient.Builder()
        .addInterceptor(TokenInterceptor())
        .retryOnConnectionFailure(true)
        .build()
    private val retrofit = Retrofit.Builder()
        .baseUrl("http://195.201.42.22:8080/")
        .addConverterFactory(GsonConverterFactory.create())
        .client(LogoutAPI.httpClient)
        .build()
    val logoutService: LogoutService = retrofit.create(LogoutService::class.java)
}

object AuthStatusApi {
    private val httpClient = OkHttpClient.Builder()
        .addInterceptor(TokenInterceptor())
        .retryOnConnectionFailure(true)
        .build()
    private val retrofit = Retrofit.Builder()
        .baseUrl("http://195.201.42.22:8080/")
        .addConverterFactory(GsonConverterFactory.create())
        .client(httpClient)
        .build()
    val authStatusService: AuthStatusService = retrofit.create(AuthStatusService::class.java)
}
object RefreshApi {
    private val httpClient = OkHttpClient.Builder()
        .addInterceptor(TokenInterceptor())
        .retryOnConnectionFailure(true)
        .build()
    private val retrofit = Retrofit.Builder()
        .baseUrl("http://195.201.42.22:8080/")
        .addConverterFactory(GsonConverterFactory.create())
        .client(httpClient)
        .build()
    val refreshService: RefreshService = retrofit.create(RefreshService::class.java)
}

object HealthApi {
    private val retrofit = Retrofit.Builder()
        .baseUrl("http://195.201.42.22:8080/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    val healthService:HealthService = retrofit.create(HealthService::class.java)
}

object RegisterApi {
    private val retrofit = Retrofit.Builder()
        .baseUrl("http://195.201.42.22:8080/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    val registerService:RegisterService= retrofit.create(RegisterService::class.java)
}

object LoginApi{
    private val retrofit = Retrofit.Builder()
        .baseUrl("http://195.201.42.22:8080/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    val loginService:LoginService= retrofit.create(LoginService::class.java)
}

class TokenInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()
        val modifiedRequest = originalRequest.newBuilder()
            .addHeader("Authorization", "Bearer ${UserConstants.accessToken}")
            .addHeader("content-type", "application/json")
            .addHeader("Connection","close")
            .addHeader("refresh", UserConstants.refreshToken)
            .build()
        return chain.proceed(modifiedRequest)
    }
}