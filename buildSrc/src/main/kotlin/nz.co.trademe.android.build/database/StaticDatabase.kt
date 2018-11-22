package nz.co.trademe.android.build.database

import groovy.sql.Sql
import nz.co.trademe.android.build.*
import org.apache.commons.io.IOUtils
import org.gradle.api.logging.Logger
import org.json.JSONObject
import java.io.Closeable
import java.io.File

class StaticDatabase private constructor(private val sql: Sql, private val logger: Logger) : Closeable {

    fun insert(queryProvider: SqlQuery) {
        queryProvider.parameters.forEach {
            sql.executeInsert(queryProvider.query, it)
        }
    }

    private fun logTableCount(name: String) {
        val count = sql.firstRow("SELECT count(*) FROM $name", arrayListOf()).toString()
        logger.lifecycle("$name: $count")
    }

    override fun close() {
        logTableCount("Category")
        sql.close()
    }

    companion object {
        private fun openSQL(dbFile: File, schemaFile: File, logger: Logger) = Sql.newInstance("jdbc:sqlite:$dbFile", "org.sqlite.JDBC").apply {
            val schema = JSONObject(IOUtils.toString(schemaFile.reader()))
            schema.getJSONObject("database")
                    .optJSONArray("entities")
                    ?.filterIsInstance(JSONObject::class.java)
                    ?.find { it.getString("tableName") == "Category" }
                    ?.run {
                        // only main createSql script is needed, others are being executed by Room on
                        // first init (indices for example)
                        val tableName = getString("tableName")
                        logger.lifecycle("Creating table $tableName")
                        val sql = getString("createSql").replace("\${TABLE_NAME}", tableName)
                        logger.debug(sql)
                        execute(sql)
                    }
        }

        fun initialize(dbFile: File, schemaFile: File, logger: Logger) = StaticDatabase(openSQL(dbFile, schemaFile, logger), logger)
    }

    fun <R> useInTransaction(block: (StaticDatabase) -> R) = use {
        sql.withTransaction(closureOf<Any> {
            block(this@StaticDatabase)
        })
    }
}