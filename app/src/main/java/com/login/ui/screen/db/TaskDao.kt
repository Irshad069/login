package com.login.ui.screen.db

import androidx.room.*
import com.login.ui.screen.db.model.Task
import kotlinx.coroutines.flow.Flow

@Dao
interface TaskDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTask(task: Task)

    @Update
    suspend fun updateTask(task: Task)

    @Delete
    suspend fun deleteTask(task: Task)

    @Query("SELECT * FROM tasks ORDER BY priority ASC, dueDate ASC")
    fun getAllTasks(): Flow<List<Task>>
}
