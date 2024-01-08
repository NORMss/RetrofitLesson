package com.norm.myretrofitlesson

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.norm.myretrofitlesson.databinding.ContentBaseBinding

class BaseActivity : AppCompatActivity() {
    private lateinit var binding: ContentBaseBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ContentBaseBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}