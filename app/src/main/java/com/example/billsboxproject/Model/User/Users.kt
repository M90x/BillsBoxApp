package com.example.billsboxproject.Model.User

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "users")
data class Users(
    @PrimaryKey(autoGenerate = true)
    val pk: Int,
    var name: String,
    var email: String,
    var password: String,
    var phone: String
): Serializable
