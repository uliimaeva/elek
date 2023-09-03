package pp.dair.viewmodels

import pp.dair.models.JournalMark
import pp.dair.models.LessonWithMark
import pp.dair.retrofit.Common
import retrofit2.Callback

class MarksViewModel {
    fun getDaySchedule(year: Int, month: Int, day: Int, callback: Callback<ArrayList<LessonWithMark>>) {
        Common.retrofitService.getDaySchedule(Common.sessionId!!, year, month, day, Common.subgroupId).enqueue(callback)
    }

    fun getSemesterMarks(year: Int, semester: Int, callback: Callback<ArrayList<JournalMark>>) {
        Common.retrofitService.getSemesterMarks(Common.sessionId!!, year, semester).enqueue(callback)
    }

    fun getSegmentedSemesterMarks(year: Int, semester: Int, callback: Callback<Map<String, ArrayList<JournalMark>>>) {
        Common.retrofitService.getSegmentedSemesterMarks(Common.sessionId!!, year, semester).enqueue(callback)
    }
}