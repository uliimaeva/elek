package pp.dair.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import pp.dair.R
import pp.dair.adapters.MainNoteAdapter
import pp.dair.adapters.TeacherGroupedNoteAdapter
import pp.dair.models.Note
import pp.dair.models.TeacherNote
import pp.dair.retrofit.Common
import pp.dair.utils.sortNotesByGroupAndSubject
import pp.dair.viewmodels.NoteViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PrivateNotesFragment : Fragment() {

    private var viewModel: NoteViewModel = NoteViewModel()
    private lateinit var recyclerView: RecyclerView
    lateinit var adapter: MainNoteAdapter
    lateinit var teacherAdapter: TeacherGroupedNoteAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        loadNotes()
    }

    fun loadNotes() {
        Log.d("NOTES", "Loading notes!")
        if (!Common.isTeacher) {
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
        } else {
            viewModel.getTeacherNotes(object: Callback<ArrayList<TeacherNote>> {
                override fun onResponse(
                    p0: Call<ArrayList<TeacherNote>>,
                    p1: Response<ArrayList<TeacherNote>>,
                ) {
                    if (p1.isSuccessful) {
                        teacherAdapter.setArray(ArrayList(p1.body()!!.filter { !it.public }))
                    } else {
                        Log.d("ERR", "Шоколадки 2 (t)")
                        teacherAdapter.setArray(arrayListOf())
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
        recyclerView = rootView.findViewById(R.id.privateNoteRecycler)
        recyclerView.layoutManager = GridLayoutManager(requireContext(), 1)

        if (!Common.isTeacher) {
            adapter = MainNoteAdapter(emptyMap(), requireActivity(), requireContext())
            adapter.listener = { loadNotes() }
            recyclerView.adapter = adapter
        } else {
            teacherAdapter = TeacherGroupedNoteAdapter(arrayListOf(), requireActivity(), requireContext()) {
                sortNotesByGroupAndSubject(it)
            }
            recyclerView.adapter = teacherAdapter
        }

        return rootView
    }

    companion object {
    }
}