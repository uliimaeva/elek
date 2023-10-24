package pp.dair.retrofit

import pp.dair.models.LessonWithMark
import pp.dair.models.StudentInfo

object Common {
    private val BASE_URL = "https://apt-api.mrtstg.ru"
    val retrofitService: RetrofitService
        get() = RetrofitClient.getClient(BASE_URL).create(RetrofitService::class.java)

    var sessionId: String? = null;
    var subgroupId: Int = 0;
    var studentInfo: StudentInfo? = null
    var currentSub: LessonWithMark? = null
}