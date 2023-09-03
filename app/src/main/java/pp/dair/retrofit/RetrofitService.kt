package pp.dair.retrofit

import okhttp3.ResponseBody
import retrofit2.Call
import pp.dair.models.*;
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
}