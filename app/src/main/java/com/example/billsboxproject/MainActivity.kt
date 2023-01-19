package com.example.billsboxproject


import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.billsboxproject.Model.DataBase
import com.example.billsboxproject.Model.User.Users
import com.example.billsboxproject.View.HomeActivity
import com.example.billsboxproject.databinding.ActivityMainBinding
import kotlinx.coroutines.*


class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    private val userDao by lazy { DataBase.getDatabase(this).userDao() }
    private var usersList: List<Users> = listOf()
    private lateinit var userData : Users

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        userData = Users(-1, "", "", "", "")
        binding.tvSignUpLink.setOnClickListener { signUpActivity() }
        binding.btnSignIn.setOnClickListener {
            if (binding.etEmailLogin.text.isNotEmpty() && binding.etPasswordLogin.text.isNotEmpty()){
                userLogin()
            }
            else {
                Toast.makeText(this, "All field are required", Toast.LENGTH_SHORT).show()
            }
        }

        binding.tvForgotPasswordLink.setOnClickListener { alertDialog() }

    }


    private fun signUpActivity() {
        val intent = Intent(this, SignUpActivity::class.java)
        startActivity(intent)
    }


    private fun userLogin() {
        CoroutineScope(Dispatchers.IO).launch {
            val userData = async {
                userDao.getUser()
            }.await()
            if(userData.isNotEmpty()){
                usersList = userData
                withContext(Dispatchers.Main){
                    checkUser()
                }
            }else{
                Log.e("MainActivity", "Unable to get data", )
            }
        }
    }


    //////////////////////////////////////////////////
    //////////////////CHECK USER//////////////////////
    ////////////////////////////////////////////////
    private fun checkUser(){
        val email = binding.etEmailLogin.text.toString()
        val password = binding.etPasswordLogin.text.toString()
        var exist = false

        for (i in 0 .. usersList.size -1) {
            if (email == usersList[i].email) {
                if (password == usersList[i].password) {
                    userData = Users(usersList[i].pk, usersList[i].name, usersList[i].email, usersList[i].password, usersList[i].phone)
                    exist = true
                }
            }
        }
        if (exist){
            Toast.makeText(this, "Login successfully", Toast.LENGTH_LONG).show()
            homeActivity()
        }else{
            Toast.makeText(this, "Email or Password is not correct", Toast.LENGTH_LONG).show()
        }
    }


    private fun homeActivity() {
        val intent = Intent(this, HomeActivity::class.java)
        intent.putExtra("userData", userData)
        startActivity(intent)
    }


    private fun alertDialog() {
        // Build Alert Dialog
        val dialogBuilder = AlertDialog.Builder(this)
        dialogBuilder.setPositiveButton("Ok",  DialogInterface.OnClickListener {
                dialog, _ ->

        })
            .setNegativeButton("Cancel", DialogInterface.OnClickListener {
                    dialog, id -> dialog.cancel()
            })

        //create dialog box
        val alert = dialogBuilder.create()
        //set title for alert dialog box
        alert.setTitle("We are sorry for that")

        // add the Edit Text
        //alert.setView(layout)

        // Show alert dialog
        alert.show()
    }


}