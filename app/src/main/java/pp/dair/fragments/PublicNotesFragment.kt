package pp.dair.fragments

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.textfield.TextInputEditText
import pp.dair.R
import pp.dair.activities.NoteTeacherCreateFragment
import pp.dair.activities.NoteTeacherEditFragment
import pp.dair.adapters.MainNoteAdapter
import pp.dair.adapters.TeacherGroupedNoteAdapter
import pp.dair.models.Note
import pp.dair.models.TeacherNote
import pp.dair.retrofit.Common
import pp.dair.utils.sortNotesByGroupAndSubject
import pp.dair.utils.sortNotesByTeacherAndSubject
import pp.dair.viewmodels.NoteViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PublicNotesFragment : Fragment() {

    private var viewModel: NoteViewModel = NoteViewModel()
    private lateinit var recyclerView: RecyclerView
    lateinit var adapter: TeacherGroupedNoteAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        loadNotes()
    }

    fun loadNotes() {
        Log.d("NOTES", "Loading notes!")

        if (Common.isTeacher) {
            viewModel.getTeacherNotes(object: Callback<ArrayList<TeacherNote>> {
                override fun onResponse(
                    p0: Call<ArrayList<TeacherNote>>,
                    p1: Response<ArrayList<TeacherNote>>,
                ) {
                    if (p1.isSuccessful) {
                        adapter.setArray(ArrayList(p1.body()!!.filter { it.public }))
                    } else {
                        adapter.setArray(arrayListOf())
                        Log.d("ERR", "Failed to get teacher notes")
                    }
                }

                override fun onFailure(p0: Call<ArrayList<TeacherNote>>, p1: Throwable) {
                    Log.d("ERR", p1.toString())
                }
            })
        } else {
            viewModel.getPublicNotes(object: Callback<ArrayList<TeacherNote>> {
                override fun onResponse(
                    p0: Call<ArrayList<TeacherNote>>,
                    p1: Response<ArrayList<TeacherNote>>,
                ) {
                    if (p1.isSuccessful) {
                        adapter.setArray(p1.body()!!)
                    } else {
                        Log.d("ERR", "Шоколадки 2")
                        adapter.setArray(arrayListOf())
                    }
                }

                override fun onFailure(p0: Call<ArrayList<TeacherNote>>, p1: Throwable) {
                    Log.d("ERR", p1.toString())
                }

            })
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        val rootView = inflater.inflate(R.layout.fragment_private_notes, container, false)
        val search_tiet = rootView.findViewById<TextInputEditText>(R.id.search_TIET)
        recyclerView = rootView.findViewById(R.id.privateNoteRecycler)

        adapter = TeacherGroupedNoteAdapter(arrayListOf(), requireActivity(), requireContext()) {
            if (Common.isTeacher) {
                sortNotesByGroupAndSubject(it)
            } else {
                sortNotesByTeacherAndSubject(it)
            }
        }
        adapter.onOpenCallback = {
            val scheduleDialogFragment = NoteTeacherEditFragment(it, Common.isTeacher) { loadNotes() }
            val manager = (context as AppCompatActivity).supportFragmentManager;
            scheduleDialogFragment.show(manager, "noteEditDialog")
        }
        recyclerView.layoutManager = GridLayoutManager(requireContext(), 1)
        recyclerView.adapter = adapter

        search_tiet.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun afterTextChanged(p0: Editable?) {
                adapter.pattern = p0?.toString() ?: ""
                adapter.updateMap()
            }
        })

        return rootView
    }

    companion object {
    }
}