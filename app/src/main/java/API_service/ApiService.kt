import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path

interface ApiService {

    // POST запрос
    @POST("R/api/register/")
    fun postUser(@Body data: UserProfileModel): Call<UserProfileModel>

    @POST("R/api/login/")
    fun loginUser(@Body loginData: UserLoginRequestForm): Call<UserLoginRequestForm>

    @GET("stock/items/{serial_number}/")
    fun getItemBySerialNumber(@Path("serial_number") serialNumber: String): Call<ItemResponse>

    @PATCH("stock/itemsUpdate/{serial_number}/location/")
    fun updateItemLocation(
        @Path("serial_number") serialNumber: String,
        @Body locationRequest: UpdateLocationRequest
    ): Call<Void>
}


