package me.hgj.jetpackmvvm.demo.ui.activity

import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import me.hgj.jetpackmvvm.demo.R
import me.hgj.jetpackmvvm.demo.app.base.BaseActivity
import me.hgj.jetpackmvvm.demo.databinding.ActivityTestBinding
import me.hgj.jetpackmvvm.demo.ui.adapter.TestAdapter
import me.hgj.jetpackmvvm.demo.viewmodel.request.RequestLoginRegisterViewModel
import me.hgj.jetpackmvvm.demo.viewmodel.state.TestViewModel
import me.hgj.jetpackmvvm.ext.util.logd

/**
 * @author : hgj
 * @date   : 2020/8/26
 */

class TestActivity : BaseActivity<TestViewModel, ActivityTestBinding>() {

    val viewModel: RequestLoginRegisterViewModel by viewModels()

    val adapter: TestAdapter by lazy { TestAdapter(arrayListOf()) }

    override fun layoutId() = R.layout.activity_test


    override fun initView(savedInstanceState: Bundle?) {

        //强烈注意：使用addLoadingObserve 将非绑定当前activity的viewmodel绑定loading回调 防止出现请求时不显示 loading 弹窗bug
        addLoadingObserve(viewModel)

        adapter.run {
            clickAction = { position, item, state ->
                "海王收到了点击事件，并准备发一个红包".logd()
            }
        }

        mViewModel.loadingLiveData.observe(this, Observer {
            if (it) {
                println("show loading")
            } else {
                println("hide loading")
            }
        })
        mViewModel.tokenLiveData.observe(this, Observer {
            println("token show $it")
        })

    }

    override fun createObserver() {


    }
}

