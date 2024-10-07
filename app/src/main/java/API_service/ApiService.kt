import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path

interface ApiService {

    // POST запрос для авторизации
    @POST("R/api/login/")
    fun loginUser(@Body loginData: UserLoginRequestForm): Call<UserLoginResponse>

    // GET запрос для получения товара по серийному номеру
    @GET("stock/items/{serial_number}/")
    fun getItemBySerialNumber(@Path("serial_number") serialNumber: String): Call<ItemResponse>

    // PATCH запрос для обновления местоположения товара
    @PATCH("stock/items/{serial_number}/update-warehouse/")
    fun updateItemLocation(
        @Path("serial_number") serialNumber: String,
        @Body locationRequest: UpdateLocationRequest
    ): Call<Void>

    // GET запрос для получения заявок на инвентаризацию
    @GET("api/inventory-requests/")
    fun getPendingRequests(): Call<List<InventoryRequest>>

    // GET запрос для получения информации о пользователе
    @GET("accounts/api/user/")
    fun getUserDetails(): Call<UserProfileModel>

    // GET запрос для получения информации о компании
    @GET("accounts/api/company/")
    fun getCompanyDetails(): Call<CompanyModel>
}
