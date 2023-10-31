package pp.dair.activities

import android.annotation.SuppressLint
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.DatePicker
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.view.menu.ActionMenuItemView
import androidx.appcompat.widget.Toolbar
import androidx.constraintlayout.widget.ConstraintSet.Layout
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.navigation.NavigationView
import pp.dair.R
import pp.dair.adapters.ScheduleAdapter
import pp.dair.models.LessonWithMark
import pp.dair.retrofit.Common
import pp.dair.viewmodels.MarksViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.Calendar
import kotlin.system.exitProcess


class MainActivity : AppCompatActivity() {


    companion object {
        const val NOTIFICATION_ID = 101
        const val CHANNEL_ID = "channelID"
    }



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



    @SuppressLint("ResourceType")
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

//        val layout: View? = findViewById(R.layout.header_layout)
//        val name: TextView = layout!!.findViewById(R.id.profile_name)
//        val group: TextView = layout!!.findViewById(R.id.profile_group)
//        name.text = Common.studentInfo!!.name
//        group.text = Common.studentInfo!!.group.toString()


    }

    fun loadSchedule() {
        viewModel.getDaySchedule(year, month, day, object : Callback<ArrayList<LessonWithMark>> {
            override fun onResponse(
                call: Call<ArrayList<LessonWithMark>>,
                response: Response<ArrayList<LessonWithMark>>,
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
        val builder: NotificationCompat.Builder = NotificationCompat.Builder(this)
            .setSmallIcon(R.mipmap.ic_launcher)
            .setContentTitle("Title")
            .setContentText("Notification text")

        val notification = builder.build()

        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(1, notification)
        Toast.makeText(this, text, Toast.LENGTH_LONG).show()
    }

    fun openJournal() {
        startActivity(Intent(this, JournalActivity::class.java))
    }

    @SuppressLint("UseCompatLoadingForDrawables", "ResourceType")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        this.onBackPressedDispatcher.addCallback(this, object :
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
        val navigationView = findViewById<NavigationView>(R.id.navigationView)


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

        recyclerView = findViewById(R.id.r_recycler)

        adapter = ScheduleAdapter(ArrayList(), this, this)
        recyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        recyclerView.adapter = adapter
        loadSchedule()

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
                R.id.p_journal -> {
                    startActivity(Intent(this, JournalActivity::class.java))
                }

                R.id.p_diplom -> {
                    startActivity(Intent(this, DiplomActivity::class.java))
                }

                R.id.p_Note -> {
                    startActivity(Intent(this, NotesActivity::class.java))
                }

                R.id.p_ExitProfile -> {
                    startActivity(Intent(this, AuthActivity::class.java))
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