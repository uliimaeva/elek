package pp.dair.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SortedList
import pp.dair.R
import pp.dair.adapters.JournalAdapter
import pp.dair.models.JournalFinalMark
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

    var s = 0

    fun getCurrentSemester(): Pair<Int, Int> {
        val month = calendar.get(Calendar.MONTH)
        if (arrayListOf(7, 8, 9, 10, 11).contains(month)) {
            return Pair(Common.studentInfo!!.group.course, 1)
        }

        return Pair(Common.studentInfo!!.group.course, 2)
    }

    fun getAcademicYear1(): Int {
        if (semester/2 == 0) {
            return calendar.get(Calendar.YEAR) - Common.studentInfo!!.group.course + course
        }
        else{
            return calendar.get(Calendar.YEAR) - Common.studentInfo!!.group.course + course + 1
        }
    }

    fun getAcademicYear2(): Int {
        return calendar.get(Calendar.YEAR) - Common.studentInfo!!.group.course + course
    }

    fun showToast(text: String) {
        Toast.makeText(this, text, Toast.LENGTH_LONG).show()
    }

    fun setSemester() {
        when (getCurrentSemester()) {
            Pair(1,1) -> {
                s = 1
                semester = 1
                course = 1
                loadSemesterMarks()
            }

            Pair(1,2)  -> {
                s = 2
                semester = 2
                course = 1
                loadSemesterMarks()
            }

            Pair(2,1)  -> {
                s = 3
                semester = 1
                course = 2
                loadSemesterMarks()
            }

            Pair(2,2)  -> {
                s = 4
                semester = 2
                course = 2
                loadSemesterMarks()
            }

            Pair(3,1)  -> {
                s = 5
                semester = 1
                course = 3
                loadSemesterMarks()
            }

            Pair(3,2) -> {
                s = 6
                semester = 2
                course = 3
                loadSemesterMarks()
            }

            Pair(4,1) -> {
                s = 7
                semester = 1
                course = 4
                loadSemesterMarks()
            }

            Pair(4,2) -> {
                s = 8
                semester = 2
                course = 4
                loadSemesterMarks()
            }
        }
    }

    fun getSemester(){
        when (s) {
            1-> {
                semester = 1
                course = 1
                loadSemesterMarks()
            }

            2  -> {
                semester = 2
                course = 1
                loadSemesterMarks()
            }

            3  -> {
                semester = 1
                course = 2
                loadSemesterMarks()
            }

            4  -> {
                semester = 2
                course = 2
                loadSemesterMarks()
            }

            5  -> {
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

        adapter = JournalAdapter(emptyMap(), arrayListOf(),this)
        //val (course, semester) = getCurrentSemester()
        this.course = course
        this.semester = semester
        header = findViewById(R.id.j_date)
        backButton = findViewById(R.id.j_back)
        recyclerView = findViewById(R.id.r_recycler)
        recyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        recyclerView.adapter = adapter
        leftButton = findViewById(R.id.j_left)
        rightButton = findViewById(R.id.j_right)

        setSemester()

        leftButton.setOnClickListener {
            while (s != 1){
                s--
                break
            }
            getSemester()
        }
        rightButton.setOnClickListener {
            while (s != 8){
                s++
                break
            }
            getSemester()
        }

        backButton.setOnClickListener { openSchedule() }
        loadSemesterMarks()

    }


    fun loadSemesterMarks() {
        header.text = String.format("%d семестр, %d курс (%d)", semester, course, getAcademicYear2())
        viewModel.getFinalSemesterMarks(getAcademicYear2(), semester, object: Callback<ArrayList<JournalFinalMark>> {
            override fun onResponse(
                call: Call<ArrayList<JournalFinalMark>>,
                response: Response<ArrayList<JournalFinalMark>>
            ) {
                if (response.isSuccessful) {
                    Log.d("SUCCESS", response.body()!!.toString())
                    adapter.setFinalArray(response.body()!!)
                } else {
                    Log.d("FAILED", "Failed to get final marks")
                }
            }

            override fun onFailure(call: Call<ArrayList<JournalFinalMark>>, t: Throwable) {
                showToast("Ошибка получения итоговых оценок!")
            }
        })

        viewModel.getSegmentedSemesterMarks(
            getAcademicYear1(),
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