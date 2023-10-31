package pp.dair.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import pp.dair.R
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

    private var viewModel: NoteViewModel = NoteViewModel()
    private lateinit var recyclerView: RecyclerView
    lateinit var adapter: NoteAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notes)

        recyclerView = findViewById(R.id.noteRecycler)

        adapter = NoteAdapter(ArrayList(), this, this)
        adapter.listener = { loadNotes() }
        recyclerView.layoutManager = GridLayoutManager(this, 2)
        recyclerView.adapter = adapter
        loadNotes()
    }

    fun loadNotes() {
        viewModel.getNotes(object: Callback<ArrayList<Note>> {
            override fun onResponse(
                call: Call<ArrayList<Note>>,
                response: Response<ArrayList<Note>>
            ) {
                if (response.isSuccessful) {
                    adapter.setArray(response.body()!!)
                } else {
                    Log.d("ERR", "Шоколадки 2")
                    adapter.setArray(ArrayList())
                }
            }

            override fun onFailure(call: Call<ArrayList<Note>>, t: Throwable) {
                adapter.setArray(ArrayList())
                Log.d("ERR", t.toString())
            }
        })
    }
}