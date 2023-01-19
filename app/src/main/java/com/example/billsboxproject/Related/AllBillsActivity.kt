package com.example.billsboxproject.Related

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.billsboxproject.databinding.ActivityAllBillsBinding

class AllBillsActivity : AppCompatActivity() {
    lateinit var binding: ActivityAllBillsBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAllBillsBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}