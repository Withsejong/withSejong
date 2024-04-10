package com.withsejong

import android.os.Bundle
import android.widget.EditText
import androidx.activity.ComponentActivity
import androidx.appcompat.widget.AppCompatButton
import com.withsejong.databinding.MainActivityBinding

class MainActivity : ComponentActivity() {

    lateinit var binding:MainActivityBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)



        binding=MainActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)


        }
    }







