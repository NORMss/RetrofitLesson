package com.norm.myretrofitlesson

import android.os.Bundle
import android.widget.SearchView.OnQueryTextListener
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.norm.myretrofitlesson.adapter.ProductAdapter
import com.norm.myretrofitlesson.databinding.ActivityMainBinding
import com.norm.myretrofitlesson.retrofit.AuthRequest
import com.norm.myretrofitlesson.retrofit.MainAPI
import com.norm.myretrofitlesson.retrofit.User
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity() {

    private lateinit var adapter: ProductAdapter
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.title = "Guest"

        adapter = ProductAdapter()
        binding.rcView.layoutManager = LinearLayoutManager(this)
        binding.rcView.adapter = adapter

        val interceptor = HttpLoggingInterceptor()
        interceptor.level = HttpLoggingInterceptor.Level.BODY

        val client = OkHttpClient.Builder().addInterceptor(interceptor).build()

        val retrofit = Retrofit.Builder().baseUrl("https://dummyjson.com").client(client)
            .addConverterFactory(GsonConverterFactory.create()).build()
        val mainAPI = retrofit.create(MainAPI::class.java)

        var user: User? = null

        CoroutineScope(Dispatchers.IO).launch {
            user = mainAPI.auth(
                AuthRequest(
                    username = "kmeus4", password = "aUTdmmmbH"
                )
            )
            runOnUiThread {
                supportActionBar?.title = user?.firstName
            }
        }

        binding.sv.setOnQueryTextListener(object : OnQueryTextListener {
            override fun onQueryTextSubmit(p0: String?): Boolean {
                return true
            }

            override fun onQueryTextChange(text: String?): Boolean {
                CoroutineScope(Dispatchers.IO).launch {
                    val list = text?.let {
                        mainAPI.getProductsByNameAuth(user?.token ?: "", it)
                    }
                    runOnUiThread {
                        binding.apply {
                            adapter.submitList(list?.products)
                        }
                    }
                }
                return true
            }
        })
    }
}