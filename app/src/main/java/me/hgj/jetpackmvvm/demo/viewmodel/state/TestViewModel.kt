package me.hgj.jetpackmvvm.demo.viewmodel.state

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.*
import me.hgj.jetpackmvvm.base.viewmodel.BaseViewModel
import me.hgj.jetpackmvvm.demo.app.util.either.*

/**
 * Created by WangMaoBo.
 * Date: 2021/12/2
 */
class TestViewModel : BaseViewModel() {
    private val _loadingLiveData = MutableLiveData<Boolean>()
    val loadingLiveData: LiveData<Boolean> get() = _loadingLiveData
    private val _tokenLiveData = MutableLiveData<String>()
    val tokenLiveData: LiveData<String> get() = _tokenLiveData

    private object ERROR : CommonError()

    fun getLoginResult(userName: String) {
        viewModelScope.launch {
            BEGIN.run(::startLoading)
                .map {
                    userName
                }.flatMapConcat {
                    loginRequest(it)
                }.map {
                    Pair(it, it)
                }.flatMapConcat {
                    getUserTokenRequest(it)
                }.onSuccess {
                    showToken(it)
                    dismissLoading()
                }.onFailure {
                    showError()
                    dismissLoading()
                }
        }

    }

    private fun showToken(token: String) {
        _tokenLiveData.value = token

    }

    private fun showError() {
        _tokenLiveData.value = "error"
    }

    private suspend fun getUserTokenRequest(userInfo: Pair<String, String>): Expect<CommonError, String> {
        if (null == userInfo) {
            return ERROR.failure()
        }
        val result = sendRequest()
        if (result == 200) {
            return "12356".success()
        } else {
            return ERROR.failure()
        }
    }

    private fun startLoading() {
        _loadingLiveData.value = true
    }

    private fun dismissLoading() {
        _loadingLiveData.value = false
    }

    // 协程异步
    private suspend fun loginRequest(userName: String): Expect<CommonError, String> {
        if (null == userName) {
            return ERROR.failure()
        }
        val result = sendRequest()
        if (result == 200) {
            return "123".success()
        } else {
            return ERROR.failure()
        }

    }
    //  Repository
    private suspend fun sendRequest(): Int {
        // todo 是否需要io？
        withContext(Dispatchers.IO) {
            delay(1000)
        }
        return 1
    }

    open class CommonError
}