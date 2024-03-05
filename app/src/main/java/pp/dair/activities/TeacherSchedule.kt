package pp.dair.activities

import android.annotation.SuppressLint
import android.app.NotificationManager
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.DatePicker
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.NotificationCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.navigation.NavigationView
import pp.dair.R
import pp.dair.adapters.ScheduleAdapter
import pp.dair.adapters.ScheduleTeacherAdapter
import pp.dair.models.LessonWithGroup
import pp.dair.models.LessonWithMark
import pp.dair.viewmodels.StaffViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.Calendar

class TeacherSchedule : AppCompatActivity() {

    private  var viewModel: StaffViewModel = StaffViewModel()
    private lateinit var recyclerView: RecyclerView
    lateinit var adapter: ScheduleTeacherAdapter
    private val calendar = Calendar.getInstance()
    lateinit var header: TextView
    private lateinit var left_button: ImageButton
    private lateinit var  right_button: ImageButton
    private lateinit var journalButton: ImageButton
    private lateinit var datePicker: DatePicker
    private lateinit var calendarButton: ImageButton
    private lateinit var checkButton: ImageButton

    var id = 0
    var day = calendar.get(Calendar.DAY_OF_MONTH)
    var year = calendar.get(Calendar.YEAR)
    var month = calendar.get(Calendar.MONTH) + 1
    var day_of_year = calendar.get(Calendar.DAY_OF_YEAR)


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
        val builder: NotificationCompat.Builder = NotificationCompat.Builder(this)
            .setSmallIcon(R.mipmap.ic_launcher)
            .setContentTitle("Title")
            .setContentText("Notification text")

        val notification = builder.build()

        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(1, notification)
        Toast.makeText(this, text, Toast.LENGTH_LONG).show()
    }

    fun setData() {
        val today = Calendar.getInstance()
        datePicker.init(
            today.get(Calendar.YEAR), today.get(Calendar.MONTH),
            today.get(Calendar.DAY_OF_MONTH)

        ) { _, year, month, day ->
            val month = month + 1
            calendar.set(datePicker.year, datePicker.month, datePicker.dayOfMonth)
            header.text = String.format(
                "%d.%d.%d %s",
                day,
                month,
                year,
                numberToWeekDay(calendar.get(Calendar.DAY_OF_WEEK))
            )
            this.day = datePicker.dayOfMonth
            this.year = datePicker.year
            this.month = datePicker.month + 1

        }
    }

    fun loadSchedule() {
        viewModel.getTeacherSchedule(id, year, month, day, object : Callback<ArrayList<LessonWithGroup>> {
            override fun onResponse(
                call: Call<ArrayList<LessonWithGroup>>,
                response: Response<ArrayList<LessonWithGroup>>,
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

            override fun onFailure(call: Call<ArrayList<LessonWithGroup>>, t: Throwable) {
                adapter.setArray(ArrayList())
                showToast("Технические шоколадки!")
            }
        })
    }

    fun setMenuData(){

    }

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_teacher_schedule)

        val navigationView = findViewById<NavigationView>(R.id.navigationView)
        recyclerView = findViewById(R.id.t_sheadule)

        adapter = ScheduleTeacherAdapter(ArrayList(), this, this)
        recyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        recyclerView.adapter = adapter

        header.text = String.format(
            "%d.%d.%d %s",
            day,
            month,
            year,
            numberToWeekDay(calendar.get(Calendar.DAY_OF_WEEK))
        )

        left_button.setOnClickListener {
            calendar.add(Calendar.DAY_OF_MONTH, -1)
            this.day = calendar.get(Calendar.DAY_OF_MONTH)
            this.year = calendar.get(Calendar.YEAR)
            this.month = calendar.get(Calendar.MONTH) + 1
            header.text = String.format(
                "%d.%d.%d %s",
                day,
                month,
                year,
                numberToWeekDay(calendar.get(Calendar.DAY_OF_WEEK))
            )
            loadSchedule()
        }
        right_button.setOnClickListener {
            calendar.add(Calendar.DAY_OF_MONTH, 1)
            this.day = calendar.get(Calendar.DAY_OF_MONTH)
            this.year = calendar.get(Calendar.YEAR)
            this.month = calendar.get(Calendar.MONTH) + 1
            header.text = String.format(
                "%d.%d.%d %s",
                day,
                month,
                year,
                numberToWeekDay(calendar.get(Calendar.DAY_OF_WEEK))
            )

            loadSchedule()
        }

        calendarButton.setOnClickListener {
            datePicker.visibility = View.VISIBLE
            checkButton.visibility = View.VISIBLE
            calendarButton.visibility = View.GONE
            setData()
        }
        checkButton.setOnClickListener {
            datePicker.visibility = View.GONE
            checkButton.visibility = View.GONE
            calendarButton.visibility = View.VISIBLE
            loadSchedule()
        }

        val navListener = NavigationView.OnNavigationItemSelectedListener { item ->

            when (item.itemId) {
                R.id.p_Back -> {
                    startActivity(Intent(this, JournalActivity::class.java))
                }
                R.id.p_Exit -> {
                    val manager = supportFragmentManager
                    val myDialogFragment = MyDialogFragment()
                    myDialogFragment.show(manager, "myDialog")
                }

            }
            true
        }
        navigationView.setNavigationItemSelectedListener(navListener)
    }

}