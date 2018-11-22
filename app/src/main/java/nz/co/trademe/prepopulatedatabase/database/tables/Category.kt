package nz.co.trademe.prepopulatedatabase.database.tables

import android.arch.persistence.room.Entity

@Entity(primaryKeys = ["id"])
data class Category(
        var id: Int? = 0,
        var title: String = ""
)