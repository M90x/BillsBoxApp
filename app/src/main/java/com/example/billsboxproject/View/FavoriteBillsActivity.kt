package com.example.billsboxproject.View

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.billsboxproject.Model.Bill.Bills
import com.example.billsboxproject.Model.Bill.BillsAdapter
import com.example.billsboxproject.Model.Bill.FavBillsAdapter
import com.example.billsboxproject.Model.DataBase
import com.example.billsboxproject.Model.User.Users
import com.example.billsboxproject.R
import com.example.billsboxproject.Related.BillDetailsActivity
import com.example.billsboxproject.Related.NotificationsActivity
import com.example.billsboxproject.Related.UserProfileActivity
import com.example.billsboxproject.SignUpActivity
import com.example.billsboxproject.databinding.ActivityFavoriteBillsBinding
import com.example.billsboxproject.databinding.ActivityHomeBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.activity_favorite_bills.*
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.coroutines.*

class FavoriteBillsActivity : AppCompatActivity() {
    lateinit var binding: ActivityFavoriteBillsBinding
    private val billDao by lazy { DataBase.getDatabase(this).billDao() }
    private lateinit var userData : Users
    private var billsListFav: ArrayList<Bills> = arrayListOf()

    /** Recycler View declare */
    private var checkBillsListFav: List<Bills> = listOf()
    private lateinit var rvAdapter: FavBillsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFavoriteBillsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        userData = intent.getSerializableExtra("userData") as Users

        /** Bottom Navigation declare */
        binding.bottomNavigation.setSelectedItemId(R.id.favorites)
        bottomNavigation()

        ////////READ DATA FROM DB////////
        billsListFav = arrayListOf()
        getAllBillsFav()
        //////////////////////////////////


        ////////SHOW DATA ON RV////////
        rvAdapter = FavBillsAdapter(this, billsListFav)
        binding.rvBill.adapter = rvAdapter
        binding.rvBill.layoutManager = LinearLayoutManager(this)
        //////////////////////////////////
        //////////////////////////////////

        binding.icUserProfile.setOnClickListener { userProfileActivity() }
        binding.icNotification.setOnClickListener { notificationsActivity() }

    }


    private fun getAllBillsFav() {
        CoroutineScope(Dispatchers.IO).launch {
            billsListFav.clear()
            val billData = async {
                billDao.getBill() }.await()

            if(billData.isNotEmpty()){
                checkBillsListFav = billData
                withContext(Dispatchers.Main){
                    for (i in 0..checkBillsListFav.size-1) {
                        if (checkBillsListFav[i].userId == userData.pk && checkBillsListFav[i].isFav) {
                                billsListFav.add(checkBillsListFav[i])
                        }
                    }
                    rvAdapter.updateBillsList(billsListFav)
                }
            }else{
                Log.e("HomeActivity", "Unable to get data", )
            }
        }

    }

    fun updateBill(bill: Bills){
        CoroutineScope(Dispatchers.IO).launch {
            billDao.updateBill(bill)
            getAllBillsFav()
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
        if (billsListFav.size != 0) {
            rvBill.smoothScrollToPosition(billsListFav.size - 1)
        }
    }


    private fun bottomNavigation() {
        // Perform item selected listener
        binding.bottomNavigation.setOnNavigationItemSelectedListener(BottomNavigationView.OnNavigationItemSelectedListener { item ->
            when (item.itemId) {

                R.id.favorites -> return@OnNavigationItemSelectedListener true

                R.id.homePage -> {
                    val homeIntent = Intent(applicationContext, HomeActivity::class.java)
                    homeIntent.putExtra("userData", userData)
                    startActivity(homeIntent)
                    overridePendingTransition(0, 0)
                    return@OnNavigationItemSelectedListener true
                }

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