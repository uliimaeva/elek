package pp.dair.viewmodels

import pp.dair.models.LessonWithGroup
import pp.dair.models.Staff
import pp.dair.retrofit.Common
import retrofit2.Callback

class StaffViewModel {

    fun getTeachers(callback: Callback<ArrayList<Staff>>) {
        Common.retrofitService.getTeachers().enqueue(callback)
    }

    fun getTeacherSchedule(teacherId: Int, year: Int, month: Int, day: Int, callback: Callback<ArrayList<LessonWithGroup>>) {
        Common.retrofitService.getTeacherSchedule(teacherId, year, month, day).enqueue(callback)
    }
}