package br.com.urbanproblems.persistences

import android.arch.persistence.room.Database
import android.arch.persistence.room.Room
import android.arch.persistence.room.RoomDatabase
import android.content.Context
import br.com.urbanproblems.dao.ProblemDAO
import br.com.urbanproblems.models.Problem


@Database(entities = [Problem::class], version = 1)
abstract class DatabaseProblem : RoomDatabase() {

    abstract fun problemDAO(): ProblemDAO

    companion object {

        private var INSTANCE: DatabaseProblem? = null

        fun getDatabase(context: Context) : DatabaseProblem? {

            if(INSTANCE == null) {

                INSTANCE = Room.databaseBuilder(context.applicationContext,
                    DatabaseProblem::class.java,
                    "problemdbs").build()

            }

            return INSTANCE

        }

    }

}