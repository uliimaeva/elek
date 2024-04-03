package pp.dair.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import pp.dair.R
import java.util.Calendar

class HoursActivity : AppCompatActivity() {

    lateinit var recyclerView: RecyclerView
    lateinit var leftButton: ImageButton
    lateinit var rightButton: ImageButton
    lateinit var header: TextView
    private var calendar = Calendar.getInstance()
    var month = calendar.get(Calendar.MONTH) + 1

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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_hours)

        recyclerView = findViewById(R.id.h_recycler)
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
//            loadSchedule()
        }
        rightButton.setOnClickListener {
            calendar.add(Calendar.MONTH, 1)
            this.month = calendar.get(Calendar.MONTH) + 1
            header.text = String.format(
                "%s",monthName(month)
            )
//            loadSchedule()
        }


    }
}