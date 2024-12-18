package com.dicoding.picodiploma.loginwithanimation.data.local.dao

import androidx.lifecycle.LiveData
import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.dicoding.picodiploma.loginwithanimation.data.local.entity.StoryEntity

@Dao
interface StoryDao {
    @Query("SELECT * FROM story ORDER BY createdAt DESC")
    fun getStory(): LiveData<List<StoryEntity>>


    @Query("SELECT * FROM story")
    fun getAllStory(): PagingSource<Int, StoryEntity>

    @Query("SELECT * FROM story ORDER BY createdAt DESC")
    suspend fun getWidgetStory(): List<StoryEntity>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertStories(story: List<StoryEntity>)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertStory(story: StoryEntity)



    @Query("DELETE FROM story ")
    suspend fun deleteAllStory()


}