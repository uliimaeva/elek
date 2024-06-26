package pp.dair.retrofit

import okhttp3.ResponseBody
import retrofit2.Call
import pp.dair.models.*;
import retrofit2.Response
import retrofit2.http.*

interface RetrofitService {
    @GET("session/")
    fun getSession(): Call<SessionDetails>

    @POST("login")
    fun login(
        @Header("Session") session: String,
        @Body data: LoginPayload
    ): Call<LoginResponse>

    @GET("captcha/")
    fun getCaptcha(
        @Header("Session") session: String
    ): Call<ResponseBody>

    @GET("marks/day/{year}/{month}/{day}")
    fun getDaySchedule(
        @Header("Session") session: String,
        @Path("year") year: Int,
        @Path("month") month: Int,
        @Path("day") day: Int,
        @Query("subgroup") subgroup: Int = 0
    ): Call<ArrayList<LessonWithMark>>

    @GET("marks/semester")
    fun getSemesterMarks(
        @Header("Session") session: String,
        @Query("year") year: Int,
        @Query("semester") semester: Int
    ): Call<ArrayList<JournalMark>>

    @GET("marks/semester/segmented")
    fun getSegmentedSemesterMarks(
        @Header("Session") session: String,
        @Query("year") year: Int,
        @Query("semester") semester: Int
    ): Call<Map<String, ArrayList<JournalMark>>>

    @GET("student")
    fun getStudentInfo(
        @Header("Session") session: String
    ): Call<StudentInfo>

    @GET("notes/")
    fun getNotes(
        @Header("Session") session: String
    ): Call<ArrayList<Note>>

    @POST("notes/")
    fun createNote(@Header("Session") session: String, @Body data: Note): Call<Note>

    @DELETE("notes/{note_id}")
    fun deleteNote(@Header("Session") session: String, @Path("note_id") noteId: Int): Call<Response<Void>>

    @GET("marks/semester/final/")
    fun getFinalMarks(@Header("Session") session: String, @Query("year") year: Int, @Query("semester") semester: Int): Call<ArrayList<JournalFinalMark>>

    @PATCH("notes/{note_id}")
    fun patchNote(@Header("Session") session: String, @Path("note_id") noteId: Int, @Body data: NotePatch): Call<Note>

    @GET("notes/segmented/")
    fun getSegmentedNotes(@Header("Session") session: String): Call<Map<String, ArrayList<Note>>>

    @GET("staff/teachers")
    fun getTeachers(): Call<ArrayList<Staff>>

    @GET("groups/")
    fun getGroups(): Call<ArrayList<Group>>

    @GET("schedule/{group_id}/{year}/{month}/{day}")
    fun getGroupSchedule(@Path("group_id") groupId: Int, @Path("year") year: Int, @Path("month") month: Int, @Path("day") day: Int): Call<ArrayList<BaseLesson>>

    @GET("staff/schedule/{staff_id}/{year}/{month}/{day}")
    fun getTeacherSchedule(@Path("staff_id") staffId: Int, @Path("year") year: Int, @Path("month") month: Int, @Path("day") day: Int): Call<ArrayList<LessonWithGroup>>

    @GET("staff/load/{staff_id}/{year}/{month}")
    fun getTeacherLoad(@Header("Session") session: String, @Path("staff_id") staffId: Int, @Path("year") year: Int, @Path("month") month: Int): Call<TeacherLoadResponse>

    @GET("notes/teacher/")
    fun getTeacherNotes(@Header("Session") session: String): Call<ArrayList<TeacherNote>>

    @POST("notes/teacher/")
    fun createTeacherNote(@Header("Session") session: String, @Body data: TeacherNoteCreate): Call<TeacherNote>

    @DELETE("notes/teacher/id/{note_id}")
    fun deleteTeacherNote(@Header("Session") session: String, @Path("note_id") noteId: Int): Call<Response<Void>>

    @PATCH("notes/teacher/id/{note_id}")
    fun patchTeacherNote(@Header("Session") session: String, @Path("note_id") noteId: Int, @Body data: NotePatch): Call<TeacherNote>

    @GET("notes/teacher/public")
    fun getPublicNotes(@Header("Session") session: String): Call<ArrayList<TeacherNote>>
}