package com.example.billsboxproject.Related

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.billsboxproject.Model.User.Users
import com.example.billsboxproject.View.HomeActivity
import com.example.billsboxproject.databinding.ActivityUserProfileBinding

class UserProfileActivity : AppCompatActivity() {
    lateinit var binding : ActivityUserProfileBinding
    private lateinit var userData : Users

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        userData = intent.getSerializableExtra("userData") as Users
        binding.icBack.setOnClickListener { homeActivity() }
        binding.icNotification.setOnClickListener { notificationsActivity() }

    }

    private fun homeActivity() {
        val intent = Intent(this, HomeActivity::class.java)
        intent.putExtra("userData", userData)
        startActivity(intent)
    }

    private fun notificationsActivity() {
        val intent = Intent(this, UserProfileActivity::class.java)
        intent.putExtra("userData", userData)
        startActivity(intent)
    }
}