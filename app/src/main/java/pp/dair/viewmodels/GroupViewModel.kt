package pp.dair.viewmodels

import pp.dair.models.BaseLesson
import pp.dair.models.Group
import pp.dair.retrofit.Common
import retrofit2.Callback

class GroupViewModel {
    fun getGroups(callback: Callback<ArrayList<Group>>) {
        Common.retrofitService.getGroups().enqueue(callback)
    }

    fun getGroupSchedule(groupId: Int, year: Int, month: Int, day: Int, callback: Callback<ArrayList<BaseLesson>>) {
        Common.retrofitService.getGroupSchedule(groupId, year, month, day).enqueue(callback)
    }
}