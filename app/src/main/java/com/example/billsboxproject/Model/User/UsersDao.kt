package com.example.billsboxproject.Model.User

import androidx.room.*

@Dao
interface UsersDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addUser(user: Users)

    @Query("SELECT * FROM users ORDER BY pk ASC")
    fun getUser(): List<Users>

    @Update
    suspend fun updateUser(user: Users)

    @Delete
    suspend fun deleteUser(user: Users)
}