package com.example.billsboxproject

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.billsboxproject.Model.DataBase
import com.example.billsboxproject.Model.User.Users
import com.example.billsboxproject.View.HomeActivity
import com.example.billsboxproject.databinding.ActivitySignUpBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SignUpActivity : AppCompatActivity() {
    lateinit var binding: ActivitySignUpBinding
    private val userDao by lazy { DataBase.getDatabase(this).userDao() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.tvSignInLink.setOnClickListener { signInActivity() }

        binding.btnSignUp.setOnClickListener {
            if (binding.etNameSignUp.text.isNotEmpty() && binding.etEmailSignUp.text.isNotEmpty() &&
                binding.etPasswordSignUp.text.isNotEmpty() && binding.etPhoneSignUp.text.isNotEmpty()){
                userSignUp()
            }
            else {
                Toast.makeText(this, "All field are required", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun signInActivity() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }

    private fun userSignUp() {
        val name = binding.etNameSignUp.text.toString()
        val email = binding.etEmailSignUp.text.toString()
        val password = binding.etPasswordSignUp.text.toString()
        val phone = binding.etPhoneSignUp.text.toString()

        CoroutineScope(Dispatchers.IO).launch {
            userDao.addUser(Users(0, name, email, password, phone))
        }
        Toast.makeText(this, "Sign Up successfully", Toast.LENGTH_LONG).show()
        signInActivity()
    }

}