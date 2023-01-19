package com.example.billsboxproject.Related

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.billsboxproject.Model.User.Users
import com.example.billsboxproject.View.HomeActivity
import com.example.billsboxproject.databinding.ActivityNotificationsBinding

class NotificationsActivity : AppCompatActivity() {
    lateinit var binding: ActivityNotificationsBinding
    private lateinit var userData : Users


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNotificationsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        userData = intent.getSerializableExtra("userData") as Users
        binding.icBack.setOnClickListener { homeActivity() }
    }

    private fun homeActivity() {
        val intent = Intent(this, HomeActivity::class.java)
        intent.putExtra("userData", userData)
        startActivity(intent)
    }
}