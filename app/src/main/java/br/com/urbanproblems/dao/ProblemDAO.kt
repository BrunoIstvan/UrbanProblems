package br.com.urbanproblems.dao

import android.arch.lifecycle.LiveData
import android.arch.persistence.room.*
import br.com.urbanproblems.models.Problem

@Dao
interface ProblemDAO {

    @Insert
    fun insert(problem: Problem)

    @Query("SELECT * FROM Problem")
    fun listAll(): LiveData<List<Problem>>

    @Query("SELECT * FROM Problem WHERE userId = :userId")
    fun listAllByUser(userId: String): LiveData<List<Problem>>

    @Update
    fun update(problem: Problem)

    @Delete
    fun remove(problem: Problem)

    @Query("SELECT * FROM Problem WHERE id = :id")
    fun getProblemById(id: Int) : LiveData<Problem>

}