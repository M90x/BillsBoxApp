package com.example.billsboxproject.View

import android.app.DatePickerDialog
import android.content.Intent
import android.icu.text.SimpleDateFormat
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.annotation.RequiresApi
import com.example.billsboxproject.Model.Bill.Bills
import com.example.billsboxproject.Model.DataBase
import com.example.billsboxproject.Model.User.Users
import com.example.billsboxproject.R
import com.example.billsboxproject.Related.BillDetailsActivity
import com.example.billsboxproject.Related.NotificationsActivity
import com.example.billsboxproject.Related.UserProfileActivity
import com.example.billsboxproject.databinding.ActivityAddBillBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.activity_add_bill.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*
import java.util.Calendar.getInstance

class AddBillActivity : AppCompatActivity() {
    lateinit var binding: ActivityAddBillBinding
    private val billDao by lazy { DataBase.getDatabase(this).billDao() }
    private lateinit var userData : Users
    private var billDateSelected = ""
    private var warrantyDateSelected = ""


    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddBillBinding.inflate(layoutInflater)
        setContentView(binding.root)

        userData = intent.getSerializableExtra("userData") as Users

        /** Bottom Navigation declare */
        binding.bottomNavigation.setSelectedItemId(R.id.addBill)
        bottomNavigation()

        ///////////////// UI Calender ////////////////
        ////////////////////////////////////////////
        val myCanceler = getInstance()
        val datePicker1 = DatePickerDialog.OnDateSetListener { _, year, month, dayOfMonth ->
            myCanceler.set(Calendar.YEAR, year)
            myCanceler.set(Calendar.MONTH, month)
            myCanceler.set(Calendar.DAY_OF_MONTH, dayOfMonth)
            updateLabel(myCanceler, 1)
        }
        val datePicker2 = DatePickerDialog.OnDateSetListener { _, year, month, dayOfMonth ->
            myCanceler.set(Calendar.YEAR, year)
            myCanceler.set(Calendar.MONTH, month)
            myCanceler.set(Calendar.DAY_OF_MONTH, dayOfMonth)
            updateLabel(myCanceler, 2)
        }
        //////////////////////////////////////////////

        //Bill Date
        binding.ivCalender.setOnClickListener {
            DatePickerDialog(this, datePicker1, myCanceler.get(Calendar.YEAR),
                myCanceler.get(Calendar.MONTH), myCanceler.get(Calendar.DAY_OF_MONTH)).show()
        }

        //Warranty expiration date
        binding.ivCalenderWarranty.setOnClickListener {
            DatePickerDialog(this, datePicker2, myCanceler.get(Calendar.YEAR),
                myCanceler.get(Calendar.MONTH), myCanceler.get(Calendar.DAY_OF_MONTH)).show()
        }


        //Save new bill
        binding.btnSave.setOnClickListener {
            if (binding.etStoreName.text.isNotEmpty() && binding.etBillNumber.text.isNotEmpty() &&
                binding.tvBillDate.text.isNotEmpty() && binding.tvWarrantyExp.text.isNotEmpty() &&
                binding.etItems.text.isNotEmpty() && binding.etTotalAmount.text.isNotEmpty() &&
                binding.etBillImage.text.isNotEmpty()) {
                saveBill()
            } else {
                Toast.makeText(this, "All field are required", Toast.LENGTH_SHORT).show()
            }
        }


        binding.icUserProfile.setOnClickListener { userProfileActivity() }
        binding.icNotification.setOnClickListener { notificationsActivity() }


    }


    @RequiresApi(Build.VERSION_CODES.N)
    private fun updateLabel(myCanceler: Calendar?, id: Int) {
        val myFormat = "dd/MM/yyy"
        val sdf = SimpleDateFormat(myFormat, Locale.UK)
        if (id == 1) {
            binding.tvBillDate.text = sdf.format(myCanceler?.time)
            billDateSelected = sdf.format(myCanceler?.time)
        } else {
            binding.tvWarrantyExp.text = sdf.format(myCanceler?.time)
            warrantyDateSelected = sdf.format(myCanceler?.time)
        }
    }


    private fun saveBill() {
        val storeName = binding.etStoreName.text.toString()
        val billNumber = binding.etBillNumber.text.toString()
        val billDate = binding.tvBillDate.text.toString()
        val warrantyExp = binding.tvWarrantyExp.text.toString()
        val items = binding.etItems.text.toString()
        val totalAmount = binding.etTotalAmount.text.toString().toDouble()
        val billImage = binding.etBillImage.text.toString()
        CoroutineScope(Dispatchers.IO).launch {
            billDao.addBill(Bills(0, userData.pk, storeName, billNumber, billDate, warrantyExp, items, totalAmount, billImage, false))
        }
        Toast.makeText(this, "Add bill successfully", Toast.LENGTH_LONG).show()
        homeActivity()
    }


    private fun homeActivity() {
        val intent = Intent(this, HomeActivity::class.java)
        intent.putExtra("userData", userData)
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

                R.id.homePage -> {
                    val homeIntent = Intent(applicationContext, HomeActivity::class.java)
                    homeIntent.putExtra("userData", userData)
                    startActivity(homeIntent)
                    overridePendingTransition(0, 0)
                    return@OnNavigationItemSelectedListener true
                }

                R.id.addBill -> return@OnNavigationItemSelectedListener true
            }
            false
        })
    }


}