package de.max.trailblazerfrontendv1.Api

import com.google.android.gms.maps.model.LatLng
import de.max.trailblazerfrontendv1.Util.UserConstants
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.ResponseBody
import retrofit2.Call
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

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
        @Header("X-Login-Type") loginType: String,
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
    @GET("/api/v1/locations/merged")
    //@GET("/api/v1/locations/all")
    suspend fun getTiles(
        @Query("latitude") latitude: Double,
        @Query("longitude") longitude: Double,
        @Query("zoomLevel") zoomLevel: Byte
    ) : List<TileData>
}

interface VisitService {
    @POST("/api/v1/location")
    suspend fun postTile(
        @Query("latitude") latitude: Double,
        @Query("longitude") longitude: Double
    ) : ResponseBody
}

interface StatsService {
    @GET("api/v1/stats")
    suspend fun getStats() : List<CountyStats>
}

interface FriendIdService{
    @GET("/api/v1/friends")
    suspend fun getFriendsId() : List<Friend>
}

interface FriendInviteService{
    @GET("/api/v1/invites")
    suspend fun getFriendInvites() : ResponseBody
}

interface ProfilePictureService{
    @GET("/api/v1/files/profile/picture")
    suspend fun getProfilePicture(): ResponseBody
}

interface  AddFriendService{
    @POST("/api/v1/friend/email/{email}")
    suspend fun addFriend(@Path("email") email: String): ResponseBody
}

interface UpdateFriendService{
    @POST("/api/v1/friend/update")
    suspend fun acceptFriend(
        @Query("accepted") accepted: Boolean,
        @Query("uuid") uuid: String
    ): ResponseBody
}

object UpdateFriendApi{
    private val httpClient = OkHttpClient.Builder()
        .addInterceptor(TokenInterceptor())
        .retryOnConnectionFailure(true)
        .build()
    private val retrofit = Retrofit.Builder()
        .baseUrl("http://195.201.42.22:8080/")
        .addConverterFactory(GsonConverterFactory.create())
        .client(UpdateFriendApi.httpClient)
        .build()
    val updateFriendService : UpdateFriendService = retrofit.create(UpdateFriendService::class.java)
}

object AddFriendApi{
    private val httpClient = OkHttpClient.Builder()
        .addInterceptor(TokenInterceptor())
        .retryOnConnectionFailure(true)
        .build()
    private val retrofit = Retrofit.Builder()
        .baseUrl("http://195.201.42.22:8080/")
        .addConverterFactory(GsonConverterFactory.create())
        .client(AddFriendApi.httpClient)
        .build()
    val addFriendService : AddFriendService = retrofit.create(AddFriendService::class.java)
}

object FriendInviteApi{
    private val httpClient = OkHttpClient.Builder()
        .addInterceptor(TokenInterceptor())
        .retryOnConnectionFailure(true)
        .build()
    private val retrofit = Retrofit.Builder()
        .baseUrl("http://195.201.42.22:8080/")
        .addConverterFactory(GsonConverterFactory.create())
        .client(FriendInviteApi.httpClient)
        .build()
    val friendInviteService: FriendInviteService = retrofit.create(FriendInviteService::class.java)
}
object ProfilePictureApi{
    private val httpClient = OkHttpClient.Builder()
        .addInterceptor(TokenInterceptor())
        .retryOnConnectionFailure(true)
        .build()
    private val retrofit = Retrofit.Builder()
        .baseUrl("http://195.201.42.22:8080/")
        .addConverterFactory(GsonConverterFactory.create())
        .client(ProfilePictureApi.httpClient)
        .build()
    val profilePictureService: ProfilePictureService = retrofit.create(ProfilePictureService::class.java)
}
object FriendIdApi{
    private val httpClient = OkHttpClient.Builder()
        .addInterceptor(TokenInterceptor())
        .retryOnConnectionFailure(true)
        .build()
    private val retrofit = Retrofit.Builder()
        .baseUrl("http://195.201.42.22:8080/")
        .addConverterFactory(GsonConverterFactory.create())
        .client(FriendIdApi.httpClient)
        .build()
    val friendIdService: FriendIdService = retrofit.create(FriendIdService::class.java)
}

object TileApi{
    private val httpClient = OkHttpClient.Builder()
        .addInterceptor(TokenInterceptor())
        .addInterceptor(HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        })
        .retryOnConnectionFailure(true)
        .build()
    private val retrofit = Retrofit.Builder()
        .baseUrl("http://195.201.42.22:8080/")
        .addConverterFactory(GsonConverterFactory.create())
        .client(TileApi.httpClient)
        .build()
    val tileService: TileService = retrofit.create(TileService::class.java)
}

object VisitApi {
    private val httpClient = OkHttpClient.Builder()
        .addInterceptor(TokenInterceptor())
        .addInterceptor(HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        })
        .retryOnConnectionFailure(true)
        .build()
    private val retrofit = Retrofit.Builder()
        .baseUrl("http://195.201.42.22:8080/")
        .client(httpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    val visitService:VisitService= retrofit.create(VisitService::class.java)
}

object StatsApi {
    private val httpClient = OkHttpClient.Builder()
        .addInterceptor(TokenInterceptor())
        .addInterceptor(HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        })
        .retryOnConnectionFailure(true)
        .build()
    private val retrofit = Retrofit.Builder()
        .baseUrl("http://195.201.42.22:8080/")
        .client(httpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    val statsService:StatsService= retrofit.create(StatsService::class.java)
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