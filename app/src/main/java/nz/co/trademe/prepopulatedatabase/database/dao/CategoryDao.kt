package nz.co.trademe.prepopulatedatabase.database.dao

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Query
import io.reactivex.Single
import nz.co.trademe.prepopulatedatabase.database.tables.Category

@Dao
interface CategoryDao {
    @Query("SELECT * from category ORDER BY id ASC")
    fun getAll(): Single<List<Category>>
}