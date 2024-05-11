package pp.dair.retrofit

import pp.dair.models.LessonWithMark
import pp.dair.models.Note
import pp.dair.models.StudentInfo
import pp.dair.models.TeacherNote

object Common {
    private val BASE_URL = "https://apt-api.mrtstg.ru"
    val retrofitService: RetrofitService
        get() = RetrofitClient.getClient(BASE_URL).create(RetrofitService::class.java)

    var sessionId: String? = null;
    var subgroupId: Int = 0;
    var isTeacher: Boolean = false;
    var studentInfo: StudentInfo? = null
    var currentSub: LessonWithMark? = null
    var currentNote: Note? = null
    var currentTeacherNote: TeacherNote? = null
    var selectedTeacher: Int? = null
    var selectedGroup: Int? = null
    var loggedInTeacher: Int? = null
}