package pp.dair.viewmodels

import pp.dair.models.Note
import pp.dair.models.NotePatch
import pp.dair.models.TeacherNote
import pp.dair.models.TeacherNoteCreate
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

    fun patchNote(noteId: Int, data: NotePatch, callback: Callback<Note>) {
        Common.retrofitService.patchNote(Common.sessionId!!, noteId, data).enqueue(callback)
    }

    fun getSegmentedNotes(callback: Callback<Map<String, ArrayList<Note>>>) {
        Common.retrofitService.getSegmentedNotes(Common.sessionId!!).enqueue(callback)
    }

    fun getTeacherNotes(callback: Callback<ArrayList<TeacherNote>>) {
        Common.retrofitService.getTeacherNotes(Common.sessionId!!).enqueue(callback)
    }

    fun createTeacherNote(data: TeacherNoteCreate, callback: Callback<TeacherNote>) {
        Common.retrofitService.createTeacherNote(Common.sessionId!!, data).enqueue(callback)
    }

    fun deleteTeacherNote(noteId: Int, callback: Callback<Response<Void>>) {
        Common.retrofitService.deleteTeacherNote(Common.sessionId!!, noteId).enqueue(callback)
    }

    fun patchTeacherNote(noteId: Int, data: NotePatch, callback: Callback<TeacherNote>) {
        Common.retrofitService.patchTeacherNote(Common.sessionId!!, noteId, data).enqueue(callback)
    }

    fun getPublicNotes(callback: Callback<ArrayList<TeacherNote>>) {
        Common.retrofitService.getPublicNotes(Common.sessionId!!).enqueue(callback)
    }
}