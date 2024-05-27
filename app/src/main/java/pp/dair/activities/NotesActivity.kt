package pp.dair.activities

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TableLayout
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.ViewPager
import com.google.android.material.navigation.NavigationView
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import pp.dair.PageAdapter
import pp.dair.R
import pp.dair.adapters.MainNoteAdapter
import pp.dair.adapters.NoteAdapter
import pp.dair.adapters.ScheduleAdapter
import pp.dair.models.LessonWithMark
import pp.dair.models.Note
import pp.dair.viewmodels.NoteViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.Calendar

class NotesActivity : AppCompatActivity() {

    private lateinit var viewPager: ViewPager
    private lateinit var tabLayout: TabLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notes)

        viewPager = findViewById(R.id.view_pager)
        viewPager.adapter = PageAdapter(supportFragmentManager)

        tabLayout = findViewById(R.id.tabLayout)
        tabLayout.setupWithViewPager(viewPager)

        val navigationView = findViewById<NavigationView>(R.id.navigationView)


        val navListener = NavigationView.OnNavigationItemSelectedListener { item ->

            when (item.itemId) {
                R.id.p_teacher -> {
                    startActivity(Intent(this, TeacherSchedule::class.java))
                }
                R.id.p_groups -> {
                    startActivity(Intent(this, GroupSchedule::class.java))
                }
                R.id.p_hours -> {
                    startActivity(Intent(this, HoursActivity::class.java))
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