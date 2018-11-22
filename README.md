# Room Database pre-population during build

details to come later



# demolib:

This uses a small library to demonstrate shared code between gradle task and Android, the entire code of the library is:

```kotlin
import java.util.*
const val TABLE = "Category"

data class SqlQuery(val query: String, val parameters: List<Array<out Any?>>)
class StaticDataInitializer {
    fun getData(count: Int): SqlQuery {
        val results = (1..count).mapTo(arrayListOf(), { arrayOf(it, "$it ${Date()}") })
        return SqlQuery("INSERT INTO $TABLE (id, title) VALUES (?,?)", results)
    }
}
```

demolib.jar contains the above code compiled using `.\kotlinc demolib.kt -d demolib.jar`