package com.norm.myretrofitlesson

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.NumberPicker
import android.widget.TextView
import com.norm.myretrofitlesson.retrofit.ProductAPI
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val tv = findViewById<TextView>(R.id.title)
        val b = findViewById<Button>(R.id.btn_get)

        val interceptor = HttpLoggingInterceptor()
        interceptor.level = HttpLoggingInterceptor.Level.BODY

        val client = OkHttpClient.Builder()
            .addInterceptor(interceptor)
            .build()

        val numberPicker = findViewById<NumberPicker>(R.id.numberPicker)

        numberPicker.minValue = 1
        numberPicker.maxValue = 10
        numberPicker.wrapSelectorWheel = false

        var selectId = numberPicker.minValue

        numberPicker.setOnValueChangedListener { picker, oldVal, newVal ->
            selectId = newVal
        }

        val retrofit = Retrofit
            .Builder()
            .baseUrl("https://dummyjson.com").client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val productAPI = retrofit.create(ProductAPI::class.java)
        b.setOnClickListener {
            CoroutineScope(Dispatchers.IO).launch {
                val product = productAPI.getProducts(selectId)
                runOnUiThread {
                    tv.text = product.title
                }
            }
        }
    }
}