package pp.dair.activities

import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import okhttp3.ResponseBody
import pp.dair.R
import pp.dair.models.LoginResponse
import pp.dair.models.SessionDetails
import pp.dair.viewmodels.SessionViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import android.content.Intent
import pp.dair.models.LoginPayload
import pp.dair.models.StudentInfo
import pp.dair.retrofit.Common

class AuthActivity : AppCompatActivity() {
    var viewModel : SessionViewModel = SessionViewModel()
    lateinit var captcha_tiet : TextInputEditText
    lateinit var captcha_image : ImageView
    lateinit var btn_register: Button
    lateinit var login_tiet: TextInputEditText
    lateinit var password_tiet : TextInputEditText
    lateinit var login_til: TextInputLayout
    lateinit var password_til: TextInputLayout
    lateinit var captcha_til: TextInputLayout

    fun showToast(text: String) {
        Toast.makeText(this, text, Toast.LENGTH_LONG).show()
    }

    fun goToMain() {
        viewModel.getInfoCallback(object: Callback<StudentInfo> {
            override fun onResponse(call: Call<StudentInfo>, response: Response<StudentInfo>) {
                if (response.isSuccessful) {
                    Common.studentInfo = response.body()
                    Log.d("STUDENT-INFO", response.body().toString())
                }
            }

            override fun onFailure(call: Call<StudentInfo>, t: Throwable) {
                Log.d("STUDENT-INFO", t.toString())
            }
        })
        Thread.sleep(500)
        startActivity(Intent(this, MainActivity::class.java))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_auth)
        captcha_tiet = findViewById(R.id.captcha_TIET)
        captcha_image = findViewById(R.id.captcha_image)
        btn_register = findViewById(R.id.btnRegister)
        login_tiet = findViewById(R.id.login_TIET)
        password_tiet = findViewById(R.id.password_TIET)
        login_til = findViewById(R.id.login_TIL)
        password_til = findViewById(R.id.password_TIL)
        captcha_til = findViewById(R.id.captcha_TIL)

        setFormEnabled(false);

        viewModel.getSessionCallback(object: Callback<SessionDetails> {
            override fun onFailure(call: Call<SessionDetails>, t: Throwable) {
                showToast("Технические шоколадки!")
            }

            override fun onResponse(
                call: Call<SessionDetails>,
                response: Response<SessionDetails>
            ) {
                Log.d("SESSION", "Сессия получена!")
                loadCaptchaImage()
            }
        })

        btn_register.setOnClickListener { checkForm() }

        //cleanFields()
    }

    private fun cleanFields() {
        login_tiet.setText("")
        password_tiet.setText("")
        captcha_tiet.setText("")
    }

    private fun checkForm() {
        for (field in arrayListOf(
            login_til,
            password_til,
            captcha_til
        )) {
            field.error = null
        }

        val login = login_tiet.text.toString()
        val password = password_tiet.text.toString()
        val captcha = captcha_tiet.text.toString()
        if (login.isEmpty()) {
            login_til.error = "Пустой логин!"
        }

        if (password.isEmpty()) {
            password_til.error = "Пустой пароль!"
        }

        if (captcha.isEmpty()) {
            captcha_til.error = "Введите капчу!"
        }

        if (captcha_til.error == null && login_til.error == null && captcha_til.error == null) {
            if (Common.sessionId == null) {
                showToast("Подождите загрузки формы!")
                return;
            }

            viewModel.loginCallback(object : Callback<LoginResponse> {
                override fun onResponse(
                    call: Call<LoginResponse>,
                    response: Response<LoginResponse>
                ) {
                    if (response.isSuccessful) {
                        showToast(response.body()!!.message)
                        if (response.body()!!.success == 1) {
                            if (login == "mastervan" || login == "gxJ92") {
                                Common.subgroupId = 1
                            }
                            goToMain()
                        } else {
                            showToast("Неудачный вход!")
                            loadCaptchaImage()
                        }
                    } else {
                        showToast("Технические шоколадки 3.5!")
                    }
                }

                override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                    showToast("Технические шоколадки 3!")
                }
            }, LoginPayload(login, password, captcha))
        }
    }

    fun setFormEnabled(value: Boolean) {
        captcha_tiet.isEnabled = value
    }

    fun loadCaptchaImage() {
        viewModel.getCaptchaCallback(object: Callback<ResponseBody> {
            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                showToast("Технические шоколадки 2!")
            }

            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                val bytes: ByteArray = response.body()!!.bytes()
                var bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
                captcha_image.setImageBitmap(bitmap)
                setFormEnabled(true)
            }
        })
    }
}