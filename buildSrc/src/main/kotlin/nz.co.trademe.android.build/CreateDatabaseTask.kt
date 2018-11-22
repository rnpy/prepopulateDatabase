package nz.co.trademe.android.build

import nz.co.trademe.android.build.database.StaticDataInitializer
import nz.co.trademe.android.build.database.StaticDatabase
import org.gradle.api.DefaultTask
import org.gradle.api.tasks.OutputFile
import org.gradle.api.tasks.InputFile
import org.gradle.api.tasks.TaskAction
import java.io.File

open class CreateDatabaseTask : DefaultTask() {
    @get:OutputFile
    lateinit var dbFile: File

    @get:InputFile
    lateinit var schema: File

    @TaskAction
    fun update() {
        dbFile.delete()

        StaticDatabase.initialize(dbFile, schema, logger).useInTransaction { database ->
            database.insert(StaticDataInitializer().getData(10))
            logger.lifecycle("database updated")
        }
    }
}