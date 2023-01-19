package com.example.billsboxproject.Related

import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import com.bumptech.glide.Glide
import com.bumptech.glide.Glide.init
import com.example.billsboxproject.Model.Bill.Bills
import com.example.billsboxproject.Model.DataBase
import com.example.billsboxproject.Model.User.Users
import com.example.billsboxproject.View.HomeActivity
import com.example.billsboxproject.databinding.ActivityBillDetailsBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class BillDetailsActivity : AppCompatActivity() {
    lateinit var binding: ActivityBillDetailsBinding
    private val billDao by lazy { DataBase.getDatabase(this).billDao() }
    private lateinit var userData : Users
    private lateinit var billData : Bills

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBillDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)



        userData = intent.getSerializableExtra("userData") as Users
        billData = intent.getSerializableExtra("billData") as Bills
        binding.icBack.setOnClickListener { homeActivity() }
        binding.icDelete.setOnClickListener { alertDialog() }

        getStoreLogo()
        binding.tvStoreName.text = billData.storeName
        binding.tvBillNumber.text = "Bill No. ${billData.billNumber}"
        binding.tvBillDate.text = billData.billDate
        binding.tvWarrantyDate.text = billData.warrantyExp
        binding.tvItem.text = billData.items
        binding.tvTotalAmount.text = "${billData.totalAmount} SR"
        getBillImage()

    }


    private fun homeActivity() {
        val intent = Intent(this, HomeActivity::class.java)
        intent.putExtra("userData", userData)
        startActivity(intent)
    }


    private fun alertDialog() {
        // Build Alert Dialog
        val dialogBuilder = AlertDialog.Builder(this)
        dialogBuilder.setPositiveButton("Yes",  DialogInterface.OnClickListener {
                dialog, _ ->
            deleteBill()
        })
            .setNegativeButton("Cancel", DialogInterface.OnClickListener {
                    dialog, id -> dialog.cancel()
            })

        //create dialog box
        val alert = dialogBuilder.create()
        //set title for alert dialog box
        alert.setTitle("Confirm the deletion ")

        // add the Edit Text
        //alert.setView(layout)

        // Show alert dialog
        alert.show()
    }

    private fun deleteBill(){
        CoroutineScope(Dispatchers.IO).launch {
            billDao.deleteBill(billData)
            homeActivity()
        }
        Toast.makeText(this, "Deleted successfully", Toast.LENGTH_LONG).show()
    }


    private fun getStoreLogo() {
        val logo : String
        val jarir = "https://i.ibb.co/BTQ2GZc/jarir.jpg"
        val extra = "https://i.ibb.co/26q5FYN/extra.png"
        val stc = "https://i.ibb.co/WxmSrcg/STC.png"
        val ikea = "https://i.ibb.co/qxrtQWH/ikea.png"
        val other = "https://i.ibb.co/f9DBmfs/other.png"

        logo = when(billData.storeName){
            "JARIR Store" -> jarir
            "EXTRA Store" -> extra
            "STC Store" -> stc
            "IKEA Store" -> ikea
            else -> other
        }
        try {
            Glide.with(this)
                .load(logo)
                 .override(900, 300)
                .into(binding.ivStoreLogo)
        } catch (e:Exception){
            Log.d("Catch", "No image: $e")
        }
    }

    private fun getBillImage() {
        try {
            Glide.with(this)
                .load(billData.image)
                .override(900, 700)
                .into(binding.ivBillImage)
        } catch (e:Exception){
            Log.d("Catch", "No image: $e")
        }
    }

}