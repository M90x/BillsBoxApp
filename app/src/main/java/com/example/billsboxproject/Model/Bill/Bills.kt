package com.example.billsboxproject.Model.Bill

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable
import java.util.*

@Entity(tableName = "bills")
data class Bills(
    @PrimaryKey(autoGenerate = true)
    val pk: Int,
    var userId: Int,
    var storeName: String,
    var billNumber: String,
    var billDate: String,
    //var warranty: Boolean,
    var warrantyExp: String,
    var items: String,
    var totalAmount: Double,
    var image: String,
    var isFav: Boolean = false
): Serializable
