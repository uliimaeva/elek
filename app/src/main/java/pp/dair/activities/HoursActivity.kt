package pp.dair.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ImageButton
import android.widget.TextView
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import pp.dair.R
import pp.dair.adapters.HoursAdapter
import pp.dair.models.Staff
import pp.dair.models.TeacherLoad
import pp.dair.models.TeacherLoadResponse
import pp.dair.viewmodels.StaffViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.Calendar

class HoursActivity : AppCompatActivity() {

    lateinit var recyclerView: RecyclerView
    lateinit var leftButton: ImageButton
    lateinit var rightButton: ImageButton
    lateinit var header: TextView
    private var calendar = Calendar.getInstance()
    private var viewModel: StaffViewModel = StaffViewModel()
    var month = calendar.get(Calendar.MONTH) + 1
    private val adapter = HoursAdapter(emptyMap(), this, this)

    fun monthName(value: Int): String {
        return when (value) {
            1 -> "Январь"
            2 -> "Февраль"
            3 -> "Март"
            4 -> "Апрель"
            5 -> "Май"
            6 -> "Июнь"
            7 -> "Июль"
            8 -> "Август"
            9 -> "Сентябрь"
            10 -> "Октябрь"
            11 -> "Ноябрь"
            12 -> "Декабрь"
            else -> throw Exception("Нет такого месяца")
        }
    }

    fun loadLoad() {
        viewModel.getTeacherLoad(calendar.get(Calendar.YEAR), month, object: Callback<TeacherLoadResponse> {
            override fun onResponse(
                call: Call<TeacherLoadResponse>,
                response: Response<TeacherLoadResponse>,
            ) {
                if (response.isSuccessful) {
                    adapter.setTotalMap(response.body()!!.total)
                }
            }

            override fun onFailure(call: Call<TeacherLoadResponse>, t: Throwable) {
                Log.d("ERROR", t.toString())
            }

        })
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_hours)

        recyclerView = findViewById(R.id.h_recycler)
        recyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        recyclerView.adapter = adapter

        header = findViewById(R.id.r_date)
        leftButton = findViewById(R.id.left)
        rightButton = findViewById(R.id.right)

        header.text = String.format(
            "%s",monthName(month)
        )

        leftButton.setOnClickListener {
            calendar.add(Calendar.MONTH, -1)
            this.month = calendar.get(Calendar.MONTH) + 1
            header.text = String.format(
                "%s",monthName(month)
            )
            loadLoad()
        }
        rightButton.setOnClickListener {
            calendar.add(Calendar.MONTH, 1)
            this.month = calendar.get(Calendar.MONTH) + 1
            header.text = String.format(
                "%s",monthName(month)
            )
            loadLoad()
        }
        loadLoad()

    }
}