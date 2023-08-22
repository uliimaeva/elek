package pp.dair.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
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
    private var semester: Int = -1
    private var course: Int = -1
    private var calendar = Calendar.getInstance()
    private var year = calendar.get(Calendar.YEAR)
    private lateinit var adapter: JournalAdapter
    var viewModel: MarksViewModel = MarksViewModel()

    fun getCurrentSemester(): Pair<Int, Int> {
        val month = calendar.get(Calendar.MONTH)
        if (arrayListOf(8, 9, 10, 11).contains(month)) {
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

    fun setNextSemester() {
        assert(semester != -1)
        assert(course != -1)
        if (semester != 2) {
            semester += 1
            loadSemesterMarks()
            return
        }
        if (course != 4) {
            semester = 1
            course += 1
            loadSemesterMarks()
            return
        }
    }

    fun setPrevSemester() {
        assert(semester != -1)
        assert(course != -1)
        if (semester != 1) {
            semester -= 1
            loadSemesterMarks()
            return
        }
        if (course != 1) {
            course -= 1
            loadSemesterMarks()
            return
        }
    }

    private fun openSchedule() {
        startActivity(Intent(this, MainActivity::class.java))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_journal)
        adapter = JournalAdapter(ArrayList())
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
        leftButton.setOnClickListener { setPrevSemester() }
        rightButton.setOnClickListener { setNextSemester() }
        backButton.setOnClickListener { openSchedule() }
    }



    fun loadSemesterMarks() {
        header.text = String.format("%d семестр, %d курс", semester, course)
        viewModel.getSemesterMarks(getAcademicYear(), semester, object : Callback<ArrayList<JournalMark>> {
            override fun onResponse(
                call: Call<ArrayList<JournalMark>>,
                response: Response<ArrayList<JournalMark>>
            ) {
                if (response.isSuccessful) {
                    adapter.setArray(response.body()!!)
                }
            }

            override fun onFailure(call: Call<ArrayList<JournalMark>>, t: Throwable) {
                showToast("Ошибка!")
            }
        })
    }
}