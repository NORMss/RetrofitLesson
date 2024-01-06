package com.norm.myretrofitlesson

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.norm.myretrofitlesson.databinding.ActivityMainBinding
import com.norm.myretrofitlesson.retrofit.AuthRequest
import com.norm.myretrofitlesson.retrofit.MainAPI
import com.squareup.picasso.Picasso
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        val interceptor = HttpLoggingInterceptor()
        interceptor.level = HttpLoggingInterceptor.Level.BODY

        val client = OkHttpClient.Builder()
            .addInterceptor(interceptor)
            .build()

        val retrofit = Retrofit
            .Builder()
            .baseUrl("https://dummyjson.com").client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val mainAPI = retrofit.create(MainAPI::class.java)
        binding.btnSingin.setOnClickListener {
            CoroutineScope(Dispatchers.IO).launch {
                val user = mainAPI.auth(
                    AuthRequest(
                        binding.username.text.toString(),
                        binding.password.text.toString(),
                    )
                )
                runOnUiThread {
                    binding.apply {
                        Picasso.get().load(user.image).into(iv)
                        firstName.text = user.firstName
                        lastName.text = user.lastName
                    }
                }
            }
        }
    }
}