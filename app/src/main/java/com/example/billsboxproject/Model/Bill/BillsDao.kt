package com.example.billsboxproject.Model.Bill

import androidx.room.*

@Dao
interface BillsDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addBill(bill: Bills)

    @Query("SELECT * FROM bills ORDER BY pk ASC")
    fun getBill(): List<Bills>

    @Update
    suspend fun updateBill(bill: Bills)

    @Delete
    suspend fun deleteBill(bill: Bills)
}