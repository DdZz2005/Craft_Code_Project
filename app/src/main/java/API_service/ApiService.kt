import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiService {

    // POST запрос
    @POST("R/addUser/")
    fun postUser(@Body data: UserProfileModel): Call<UserProfileModel>

    @POST("R/loginUser/")
    fun loginUser(@Body loginData: UserLoginRequestForm): Call<UserLoginRequestForm>
}
