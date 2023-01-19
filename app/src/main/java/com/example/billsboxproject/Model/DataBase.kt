package com.example.billsboxproject.Model

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.billsboxproject.Model.Bill.Bills
import com.example.billsboxproject.Model.Bill.BillsDao
import com.example.billsboxproject.Model.User.Users
import com.example.billsboxproject.Model.User.UsersDao

@Database(entities = [Users::class, Bills::class], version = 1, exportSchema = false)
abstract class DataBase: RoomDatabase() {
    abstract fun userDao(): UsersDao
    abstract fun billDao(): BillsDao
    companion object{
        @Volatile  // writes to this field are immediately visible to other threads
        private var INSTANCE: DataBase? = null

        fun getDatabase(context: Context): DataBase{
            val tempInstance = INSTANCE
            if(tempInstance != null){
                return tempInstance
            }

            synchronized(this){  // protection from concurrent execution on multiple threads
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    DataBase::class.java,
                    "projectDB"
                ) //.fallbackToDestructiveMigration()  // Destroys old database on version change
                    .build()
                INSTANCE = instance
                return instance
            }
        }
    }
}