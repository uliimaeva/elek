package pp.dair.viewmodels

import pp.dair.models.Note
import pp.dair.retrofit.Common
import retrofit2.Callback
import retrofit2.Response

class NoteViewModel {
    fun getNotes(callback: Callback<ArrayList<Note>>) {
        Common.retrofitService.getNotes(Common.sessionId!!).enqueue(callback)
    }

    fun createNote(data: Note, callback: Callback<Note>) {
        Common.retrofitService.createNote(Common.sessionId!!, data).enqueue(callback)
    }

    fun deleteNote(noteId: Int, callback: Callback<Response<Void>>) {
        Common.retrofitService.deleteNote(Common.sessionId!!, noteId).enqueue(callback)
    }
}