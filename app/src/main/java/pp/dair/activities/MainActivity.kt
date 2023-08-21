package pp.dair.activities

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.DatePicker
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.view.menu.ActionMenuItemView
import androidx.core.app.ActivityCompat
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import pp.dair.R
import pp.dair.adapters.ScheduleAdapter
import pp.dair.models.LessonWithMark
import pp.dair.viewmodels.MarksViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.time.DayOfWeek
import java.util.Calendar
import kotlin.system.exitProcess

class MainActivity : AppCompatActivity() {
    private var viewModel: MarksViewModel = MarksViewModel()
    private lateinit var recyclerView: RecyclerView
    lateinit var adapter: ScheduleAdapter
    private var calendar = Calendar.getInstance()
    private lateinit var header: TextView
    private lateinit var left_button: ImageButton
    private lateinit var right_button: ImageButton
    private lateinit var visit_line: ImageView
    private lateinit var journalButton: ActionMenuItemView

    private lateinit var datePicker: DatePicker
    private lateinit var calendarButton: ImageButton
    private lateinit var checkButton: ImageButton


    var day = calendar.get(Calendar.DAY_OF_MONTH)
    var year = calendar.get(Calendar.YEAR)
    var month = calendar.get(Calendar.MONTH) + 1
    var day_of_year = calendar.get(Calendar.DAY_OF_YEAR)


    fun setData() {
        val today = Calendar.getInstance()
        datePicker.init(today.get(Calendar.YEAR), today.get(Calendar.MONTH),
            today.get(Calendar.DAY_OF_MONTH)

        ) { _, year, month, day ->
            val month = month + 1
            calendar.set(datePicker.year, datePicker.month, datePicker.dayOfMonth)
            header.text = String.format("%d.%d.%d %s", day, month, year, numberToWeekDay(calendar.get(Calendar.DAY_OF_WEEK)))
            this.day = datePicker.dayOfMonth
            this.year = datePicker.year
            this.month = datePicker.month
            loadSchedule()
        }
    }

    fun loadSchedule() {
        viewModel.getDaySchedule(year, month, day, object: Callback<ArrayList<LessonWithMark>> {
            override fun onResponse(
                call: Call<ArrayList<LessonWithMark>>,
                response: Response<ArrayList<LessonWithMark>>
            ) {
                if (response.isSuccessful) {
                    Log.d("HUI", "Расписание получено!")
                    for (lesson in response.body()!!) {
                        Log.d("HUI", lesson.toString())
                    }
                    adapter.setArray(response.body()!!)
                } else {
                    showToast("Технические шоколадки 2")
                    adapter.setArray(ArrayList())
                }
            }

            override fun onFailure(call: Call<ArrayList<LessonWithMark>>, t: Throwable) {
                adapter.setArray(ArrayList())
                showToast("Технические шоколадки!")
            }
        })
    }

    fun numberToWeekDay(value: Int): String {
        return when (value) {
            Calendar.MONDAY -> "Понедельник"
            Calendar.TUESDAY -> "Вторник"
            Calendar.WEDNESDAY -> "Среда"
            Calendar.THURSDAY -> "Четверг"
            Calendar.FRIDAY -> "Пятница"
            Calendar.SATURDAY -> "Суббота"
            Calendar.SUNDAY -> "Воскресенье"
            else -> throw Exception("Нет такого дня недели")
        }
    }

    fun showToast(text: String) {
        Toast.makeText(this, text, Toast.LENGTH_LONG).show()
    }

    fun openJournal() {
        startActivity(Intent(this, JournalActivity::class.java))
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        this.onBackPressedDispatcher.addCallback(this, object:
            OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                ActivityCompat.finishAffinity(this@MainActivity)
                exitProcess(0)
            }
        })

        val menu = findViewById<Toolbar>(R.id.toolBarMain)
        journalButton = menu.findViewById(R.id.journal)
        journalButton.setOnClickListener { openJournal() }
        header = findViewById(R.id.r_date)
        datePicker = findViewById(R.id.datePicker1)
        calendarButton = findViewById(R.id.calendar)
        checkButton = findViewById(R.id.check)
        left_button = findViewById(R.id.left)
        right_button = findViewById(R.id.right)

        header.text = String.format("%d.%d.%d %s", day, month, year, numberToWeekDay(calendar.get(Calendar.DAY_OF_WEEK)))

        left_button.setOnClickListener {
            calendar.add(Calendar.DAY_OF_MONTH, -1)
            loadSchedule()
        }
        right_button.setOnClickListener {
            calendar.add(Calendar.DAY_OF_MONTH, 1)
            loadSchedule()
        }

        recyclerView = findViewById(R.id.r_recycler)

        adapter = ScheduleAdapter(ArrayList(), this)
        recyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        recyclerView.adapter = adapter
        loadSchedule()
        choseDate()
    }

    private fun choseDate() {
        calendarButton.setOnClickListener {
            datePicker.visibility = View.VISIBLE
            checkButton.visibility = View.VISIBLE
            calendarButton.visibility = View.GONE
        }
        checkButton.setOnClickListener {
            datePicker.visibility = View.GONE
            checkButton.visibility = View.GONE
            calendarButton.visibility = View.VISIBLE
            Toast.makeText(this, "писька", Toast.LENGTH_SHORT).show()
            setData()
        }
    }
}
