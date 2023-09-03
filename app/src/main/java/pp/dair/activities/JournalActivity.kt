package pp.dair.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SortedList
import pp.dair.R
import pp.dair.adapters.JournalAdapter
import pp.dair.models.JournalMark
import pp.dair.retrofit.Common
import pp.dair.viewmodels.MarksViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*
import kotlin.collections.ArrayList

class JournalActivity : AppCompatActivity() {
    private lateinit var backButton: ImageButton
    private lateinit var recyclerView: RecyclerView
    private lateinit var leftButton: ImageButton
    private lateinit var rightButton: ImageButton
    private lateinit var header: TextView
    private lateinit var marks: TextView
    private var semester: Int = -1
    private var course: Int = -1
    private var calendar = Calendar.getInstance()
    private var year = calendar.get(Calendar.YEAR)
    private lateinit var adapter: JournalAdapter
    var viewModel: MarksViewModel = MarksViewModel()

    var s = 7

    fun getCurrentSemester(): Pair<Int, Int> {
        val month = calendar.get(Calendar.MONTH)
        if (arrayListOf(7, 8, 9, 10, 11).contains(month)) {
            return Pair(Common.studentInfo!!.group.course, 1)
        }
        return Pair(Common.studentInfo!!.group.course, 2)
    }

    fun getAcademicYear(): Int {
        return calendar.get(Calendar.YEAR) - Common.studentInfo!!.group.course + course
    }

    fun showToast(text: String) {
        Toast.makeText(this, text, Toast.LENGTH_LONG).show()
    }

    fun setSemester() {
        when (s) {
            1 -> {
                semester = 1
                course = 1
                loadSemesterMarks()
            }

            2 -> {
                semester = 2
                course = 1
                loadSemesterMarks()
            }

            3 -> {
                semester = 1
                course = 2
                loadSemesterMarks()
            }

            4 -> {
                semester = 2
                course = 2
                loadSemesterMarks()
            }

            5 -> {
                semester = 1
                course = 3
                loadSemesterMarks()
            }

            6 -> {
                semester = 2
                course = 3
                loadSemesterMarks()
            }

            7 -> {
                semester = 1
                course = 4
                loadSemesterMarks()
            }

            8 -> {
                semester = 2
                course = 4
                loadSemesterMarks()
            }
        }
    }

    private fun openSchedule() {
        startActivity(Intent(this, MainActivity::class.java))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_journal)

        adapter = JournalAdapter(emptyMap(), this)
        val (course, semester) = getCurrentSemester()
        this.course = course
        this.semester = semester
        header = findViewById(R.id.j_date)
        backButton = findViewById(R.id.j_back)
        recyclerView = findViewById(R.id.r_recycler)
        recyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        recyclerView.adapter = adapter
        leftButton = findViewById(R.id.j_left)
        rightButton = findViewById(R.id.j_right)

        leftButton.setOnClickListener {
            while (s != 1){
                s--
                break
            }
            setSemester()
        }
        rightButton.setOnClickListener {
            while (s != 8){
                s++
                break
            }
            setSemester()
        }

        backButton.setOnClickListener { openSchedule() }
        loadSemesterMarks()

    }


    fun loadSemesterMarks() {
        header.text = String.format("%d семестр, %d курс", semester, course)
        viewModel.getSegmentedSemesterMarks(
            getAcademicYear(),
            semester,
            object : Callback<Map<String, ArrayList<JournalMark>>> {
                override fun onResponse(
                    call: Call<Map<String, ArrayList<JournalMark>>>,
                    response: Response<Map<String, ArrayList<JournalMark>>>
                ) {
                    if (response.isSuccessful) {
                        adapter.setArray(response.body()!!)
                    }
                }

                override fun onFailure(call: Call<Map<String, ArrayList<JournalMark>>>, t: Throwable) {
                    showToast("Ошибка!")
                }
            })
    }
}