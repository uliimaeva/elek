package pp.dair.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
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

    private var viewModel: NoteViewModel = NoteViewModel()
    private lateinit var recyclerView: RecyclerView
    lateinit var adapter: MainNoteAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notes)

        recyclerView = findViewById(R.id.noteRecycler)

        adapter = MainNoteAdapter(emptyMap(), this, this)
        adapter.listener = { loadNotes() }
        recyclerView.layoutManager = GridLayoutManager(this, 1)
        recyclerView.adapter = adapter

        loadNotes()
    }

    fun loadNotes() {
        Log.d("NOTES", "Loading notes!")
        viewModel.getSegmentedNotes(object: Callback<Map<String, ArrayList<Note>>> {
            override fun onResponse(
                call: Call<Map<String, ArrayList<Note>>>,
                response: Response<Map<String, ArrayList<Note>>>
            ) {
                if (response.isSuccessful) {
                    adapter.setArray(response.body()!!)
                } else {
                    Log.d("ERR", "Шоколадки 2")
                    adapter.setArray(emptyMap())
                }
            }

            override fun onFailure(call: Call<Map<String, ArrayList<Note>>>, t: Throwable) {
                Log.d("ERR", t.toString())
            }
        })
    }
}