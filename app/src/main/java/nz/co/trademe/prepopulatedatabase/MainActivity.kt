package nz.co.trademe.prepopulatedatabase

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.category_row.view.*
import nz.co.trademe.android.build.database.StaticDataInitializer
import nz.co.trademe.prepopulatedatabase.database.DemoDatabase
import nz.co.trademe.prepopulatedatabase.recyclerview.SingleLayoutAdapter

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        displayDatabaseContent()
        button.setOnClickListener {
            Schedulers.io().scheduleDirect {
                updateDatabaseContent(editText.text.toString().toInt())
            }
        }
    }

    /**
     * This is the most basic version, simply deleting everything and inserting conflict-free
     * data without foreign keys. For more complex insertions, you may need to use disable foreign
     * key validation during insert and enable it again after.
     *
     * see foreign_keys or defer_foreign_keys pragma commands (depending on database support, defer
     * is supported by Lollipop and above)
     *
     * If you're going to re-use this code, make sure to move this call on a background thread. Room
     * main thread validation doesn't work when using openHelper directly.
     */
    private fun updateDatabaseContent(count: Int) {
        val newData = StaticDataInitializer().getData(count)

        with(DemoDatabase.getDatabase(this).openHelper.writableDatabase) {
            try {
                beginTransaction()
                execSQL("DELETE FROM `category`")
                newData.parameters.forEach { execSQL(newData.query, it) }
                setTransactionSuccessful()
            } finally {
                endTransaction()
            }
        }
        displayDatabaseContent()
    }

    private fun displayDatabaseContent() {
        DemoDatabase.getDatabase(this).categoryDao().getAll()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { items ->
                    recyclerView.adapter = SingleLayoutAdapter(R.layout.category_row, items) {
                        idTextView.text = it.id.toString()
                        titleTextView.text = it.title
                    }
                }
    }
}
