package com.example.billsboxproject.View

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.billsboxproject.Model.Bill.Bills
import com.example.billsboxproject.Model.Bill.BillsAdapter
import com.example.billsboxproject.Model.DataBase
import com.example.billsboxproject.Model.User.Users
import com.example.billsboxproject.R
import com.example.billsboxproject.Related.BillDetailsActivity
import com.example.billsboxproject.Related.NotificationsActivity
import com.example.billsboxproject.Related.UserProfileActivity
import com.example.billsboxproject.databinding.ActivityHomeBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.coroutines.*

class HomeActivity : AppCompatActivity() {
    lateinit var binding: ActivityHomeBinding
    private val billDao by lazy { DataBase.getDatabase(this).billDao() }
    private lateinit var userData : Users
    private var billsList: ArrayList<Bills> = arrayListOf()

    /** Recycler View declare */
    private var checkBillsList: List<Bills> = listOf()
    private lateinit var rvAdapter: BillsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        userData = intent.getSerializableExtra("userData") as Users

        /** Bottom Navigation declare */
        binding.bottomNavigation.setSelectedItemId(R.id.homePage)
        bottomNavigation()


        ////////READ DATA FROM DB////////
        billsList = arrayListOf()
        getAllBills()
        //////////////////////////////////


        ////////SHOW DATA ON RV////////
        rvAdapter = BillsAdapter(this, billsList)
        binding.rvBills.adapter = rvAdapter
        binding.rvBills.layoutManager = LinearLayoutManager(this)
        //////////////////////////////////
        //////////////////////////////////

        binding.icUserProfile.setOnClickListener { userProfileActivity() }
        binding.icNotification.setOnClickListener { notificationsActivity() }

    }


    private fun getAllBills() {
        CoroutineScope(Dispatchers.IO).launch {
            billsList.clear()
            val billData = async {
                billDao.getBill() }.await()

            if(billData.isNotEmpty()){
                checkBillsList = billData
                withContext(Dispatchers.Main){
                    for (i in 0..checkBillsList.size-1) {
                        if (checkBillsList[i].userId == userData.pk) {
                            billsList.add(checkBillsList[i])
                        }
                    }
                    rvAdapter.updateBillsList(billsList)
                }
            }else{
                Log.e("HomeActivity", "Unable to get data", )
            }
        }

    }


    fun updateBill(bill: Bills){
        CoroutineScope(Dispatchers.IO).launch {
            billDao.updateBill(bill)
            getAllBills()
            autoScroll()
        }
    }


    fun billDetails(bill: Bills) {
        val intent = Intent(this, BillDetailsActivity::class.java)
        intent.putExtra("userData", userData)
        intent.putExtra("billData", bill)
        startActivity(intent)
    }


    private fun userProfileActivity() {
        val intent = Intent(this, UserProfileActivity::class.java)
        intent.putExtra("userData", userData)
        startActivity(intent)
    }

    private fun notificationsActivity() {
        val intent = Intent(this, NotificationsActivity::class.java)
        intent.putExtra("userData", userData)
        startActivity(intent)
    }


    private fun autoScroll(){
        if (billsList.size != 0) {
            rvBills.smoothScrollToPosition(billsList.size - 1)
        }
    }


    private fun bottomNavigation() {
        // Perform item selected listener
        binding.bottomNavigation.setOnNavigationItemSelectedListener(BottomNavigationView.OnNavigationItemSelectedListener { item ->
            when (item.itemId) {

                R.id.favorites -> {
                    val favIntent = Intent(applicationContext, FavoriteBillsActivity::class.java)
                    favIntent.putExtra("userData", userData)
                    startActivity(favIntent)
                    overridePendingTransition(0, 0)
                    return@OnNavigationItemSelectedListener true
                }

                R.id.homePage -> return@OnNavigationItemSelectedListener true

                R.id.addBill -> {
                    val addIntent = Intent(applicationContext, AddBillActivity::class.java)
                    addIntent.putExtra("userData", userData)
                    startActivity(addIntent)
                    overridePendingTransition(0, 0)
                    return@OnNavigationItemSelectedListener true
                }
            }
            false
        })

    }

}