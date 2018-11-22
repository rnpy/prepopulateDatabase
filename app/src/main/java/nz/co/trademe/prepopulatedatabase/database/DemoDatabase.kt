package nz.co.trademe.prepopulatedatabase.database

import android.arch.persistence.db.SupportSQLiteDatabase
import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase
import nz.co.trademe.prepopulatedatabase.database.dao.CategoryDao
import nz.co.trademe.prepopulatedatabase.database.tables.Category
import android.arch.persistence.room.Room
import android.content.Context
import java.io.File

const val DATABASE_FILE = "database.db"
const val STATIC_DATABASE_FILE = "static.db"

@Database(
        version = 1,
        entities = [Category::class]
)
abstract class DemoDatabase : RoomDatabase() {
    abstract fun categoryDao(): CategoryDao

    companion object {
        private var INSTANCE: DemoDatabase? = null

        fun getDatabase(context: Context): DemoDatabase {
            INSTANCE?.let { return it } ?:
                    synchronized(RoomDatabase::class.java) {
                        if (!context.getDatabasePath(DATABASE_FILE).exists()) {
                            // Read static database
                            val source = context.applicationContext.assets.open(STATIC_DATABASE_FILE).use {
                                it.readBytes()
                            }
                            // Write to Room database
                            with(context.getDatabasePath(DATABASE_FILE)) {
                                val folder = path.substringBeforeLast(File.separator)
                                File(folder).mkdirs()
                                writeBytes(source)
                            }
                        }
                        val instance = Room.databaseBuilder(context.applicationContext,
                                DemoDatabase::class.java, DATABASE_FILE)
                                .build()
                        INSTANCE = instance
                        return instance
                    }
        }
    }
}