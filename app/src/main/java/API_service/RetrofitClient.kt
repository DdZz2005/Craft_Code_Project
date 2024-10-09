import com.google.gson.GsonBuilder
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {
    private var retrofit: Retrofit? = null

    val baseUrl = "http://192.168.0.105:8080/"


    // Функция для получения клиента с добавлением токена
    fun getClient(token: String? = null): Retrofit {
        if (retrofit == null) {
            val gson = GsonBuilder()
                .setLenient()
                .create()

            // Добавляем токен, если он есть
            val client = if (token != null) {
                OkHttpClient.Builder()
                    .addInterceptor(Interceptor { chain ->
                        val request = chain.request().newBuilder()
                            .addHeader("Authorization", "Bearer $token")
                            .build()
                        chain.proceed(request)
                    }).build()
            } else {
                OkHttpClient.Builder().build()
            }

            retrofit = Retrofit.Builder()
                .baseUrl(baseUrl)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build()
        }
        return retrofit!!
    }
}