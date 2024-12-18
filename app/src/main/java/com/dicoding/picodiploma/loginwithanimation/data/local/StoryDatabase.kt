package com.dicoding.picodiploma.loginwithanimation.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.dicoding.picodiploma.loginwithanimation.data.local.dao.RemoteKeysDao
import com.dicoding.picodiploma.loginwithanimation.data.local.dao.StoryDao
import com.dicoding.picodiploma.loginwithanimation.data.local.entity.RemoteKeys
import com.dicoding.picodiploma.loginwithanimation.data.local.entity.StoryEntity


@Database(entities = [StoryEntity::class,RemoteKeys::class], version = 2, exportSchema = false)
abstract class StoryDatabase : RoomDatabase() {

    abstract fun storyDao() : StoryDao
    abstract fun remoteKeysDao() : RemoteKeysDao

    companion object {
        @Volatile
        private var instance: StoryDatabase? = null
        fun getInstance(context: Context): StoryDatabase =
            instance ?: synchronized(this) {
                instance ?: Room.databaseBuilder(
                    context.applicationContext,
                    StoryDatabase::class.java, "StoryDatabase.db"
                ).build()
            }
    }
}