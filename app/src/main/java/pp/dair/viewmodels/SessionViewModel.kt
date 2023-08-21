package pp.dair.viewmodels

import android.util.Log
import okhttp3.ResponseBody
import pp.dair.models.LoginPayload
import pp.dair.models.LoginResponse
import pp.dair.models.SessionDetails
import pp.dair.models.StudentInfo
import pp.dair.retrofit.Common
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SessionViewModel {
    fun getSessionCallback(
        callback: Callback<SessionDetails>
    ) {
        Common.retrofitService.getSession().enqueue(object: Callback<SessionDetails> {
            override fun onResponse(
                call: Call<SessionDetails>,
                response: Response<SessionDetails>
            ) {
                if (response.isSuccessful) {
                    Common.sessionId  = response.body()!!.header
                    callback.onResponse(call, response)
                }
            }

            override fun onFailure(call: Call<SessionDetails>, t: Throwable) {
                Log.d("SESSION-ERROR", t.toString())
                callback.onFailure(call, t)
            }
        })
    }

    fun getCaptchaCallback(callback: Callback<ResponseBody>) {
        Common.retrofitService.getCaptcha(Common.sessionId!!).enqueue(object: Callback<ResponseBody> {
            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                callback.onFailure(call, t)
            }

            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                callback.onResponse(call, response)
            }
        })
    }

    fun loginCallback(callback: Callback<LoginResponse>, payload: LoginPayload) {
        Common.retrofitService.login(Common.sessionId!!, payload).enqueue(callback)
    }

    fun getInfoCallback(callback: Callback<StudentInfo>) {
        Common.retrofitService.getStudentInfo(Common.sessionId!!).enqueue(callback)
    }
}