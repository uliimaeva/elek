package pp.dair.activities

import android.annotation.SuppressLint
import android.app.NotificationManager
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.DatePicker
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.ListView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.core.app.NotificationCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.navigation.NavigationView
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import pp.dair.R
import pp.dair.adapters.ScheduleTeacherAdapter
import pp.dair.models.LessonWithGroup
import pp.dair.models.Staff
import pp.dair.models.getStaffName
import pp.dair.retrofit.Common
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
    private lateinit var datePicker: DatePicker
    private lateinit var calendarButton: ImageButton
    private lateinit var checkButton: ImageButton
    private lateinit var listView: ListView
    private lateinit var search_TIL: SearchView
    private lateinit var search_TIET: TextInputEditText
    private var teachers: ArrayList<String> = arrayListOf()
    private var teachersStaff: ArrayList<Staff> = arrayListOf()
    private var teacherArray: ArrayList<LessonWithGroup> = ArrayList()
    private lateinit var linearLayout: LinearLayout

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

    fun loadSchedule(id: Int) {
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

    fun filterList(text: String) {
        teachers.clear()
        for (item in teachersStaff) {
            val fio = getStaffName(item)
            Log.d("SEARCH", String.format("Comparing %s and %s", fio, text))
            if (fio.contains(text, ignoreCase = true)) {
                Log.d("SEARCH", String.format("Matched %s", fio));
                teachers.add(getStaffName(item))
            }
        }
        if (teachers.isEmpty()) {
            Toast.makeText(this, "Такого преподавателя нет", Toast.LENGTH_SHORT).show()
        }
        listView.adapter = ArrayAdapter(this, R.layout.menu_item_layout, teachers)
    }



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_teacher_schedule)

        val navigationView = findViewById<NavigationView>(R.id.navigationView)
        val hView = navigationView.getHeaderView(0)
        recyclerView = findViewById(R.id.t_sheadule)
        header = findViewById(R.id.r_date)
        left_button = findViewById(R.id.left)
        right_button = findViewById(R.id.right)
        calendarButton = findViewById(R.id.calendar)
        checkButton = findViewById(R.id.check)
        datePicker = findViewById(R.id.datePicker1)
        search_TIL = hView.findViewById(R.id.search_TIL)
        linearLayout = findViewById(R.id.textText)

        listView = hView.findViewById(R.id.listTeacher)
        listView.adapter = ArrayAdapter(this, R.layout.menu_item_layout, teachers)



        listView.onItemClickListener = object : AdapterView.OnItemClickListener {
            override fun onItemClick(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long,
            ) {
                val selectedTeacherName = teachers.get(position)
                for (teacher in teachersStaff) {
                    if (getStaffName(teacher) == selectedTeacherName) {
                        Common.selectedTeacher = teacher.id
                        Common.selectedTeacher?.let {
                                it1 -> loadSchedule(it1)
                                linearLayout.visibility = View.GONE
                        }
                    }
                }
                if (Common.selectedTeacher == null) {
                    // ошибка что учитель не найден
                } else {
                    // чо-нить открываем делаем
                }
            }
        }

        StaffViewModel().getTeachers(object : Callback<ArrayList<Staff>> {
            override fun onResponse(
                call: Call<ArrayList<Staff>>,
                response: Response<ArrayList<Staff>>,
            ) {
                if (response.isSuccessful) {
                    teachersStaff = response.body()!!
                    teachers.clear()
                    teachers.addAll(ArrayList(response.body()!!.map { getStaffName(it) }))
                    (listView.adapter as ArrayAdapter<*>).notifyDataSetChanged()
                    Log.d("SUCCESS", teachers.toString())
                }
            }

            override fun onFailure(call: Call<ArrayList<Staff>>, t: Throwable) {
                Log.d("ERROR", "pipec")
            }
        })

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
            Common.selectedTeacher?.let { it1 -> loadSchedule(it1) }
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

            Common.selectedTeacher?.let { it1 -> loadSchedule(it1) }
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
            Common.selectedTeacher?.let { it1 -> loadSchedule(it1) }
        }

        val navListener = NavigationView.OnNavigationItemSelectedListener { item ->

            when (item.itemId) {
//                R.id.p_Back -> {
//                    startActivity(Intent(this, MainActivity::class.java))
//                }
//                R.id.p_Exit -> {
//                    val manager = supportFragmentManager
//                    val myDialogFragment = MyDialogFragment()
//                    myDialogFragment.show(manager, "myDialog")
//                }

            }
            true
        }
        navigationView.setNavigationItemSelectedListener(navListener)


        search_TIL.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }
            override fun onQueryTextChange(newText: String?): Boolean {
                if (newText != null) {
                    filterList(newText)
                }
                return false
            }
        })
    }
}