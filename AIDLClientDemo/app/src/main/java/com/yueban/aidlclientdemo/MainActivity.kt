package com.yueban.aidlclientdemo

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.codezjx.andlinker.AndLinker

class MainActivity : AppCompatActivity(), AndLinker.BindCallback {
    private val TAG = MainActivity::class.java.simpleName
    private val REMOTE_SERVICE_PKG = "com.yueban.aidlserverdemo"
    private val REMOTE_SERVICE_ACTION = "com.yueban.REMOTE_SERVICE_ACTION"

    private lateinit var mLinker: AndLinker
    private var mRemoteService: IRemoteService? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mLinker = AndLinker.Builder(this).packageName(REMOTE_SERVICE_PKG).action(REMOTE_SERVICE_ACTION)
            // Specify the callback executor by yourself
            //.addCallAdapterFactory(OriginalCallAdapterFactory.create(callbackExecutor))
//            .addCallAdapterFactory(OriginalCallAdapterFactory.create()) // Basic
//            .addCallAdapterFactory(RxJava2CallAdapterFactory.create()) // RxJava2
            .build()
        mLinker.setBindCallback(this)
//        mLinker.registerObject(mRemoteCallback)
        mLinker.bind()
    }

    override fun onBind() {
        Log.d(TAG, "AndLinker onBind()")
        mRemoteService = mLinker.create(IRemoteService::class.java)
    }

    override fun onUnBind() {
        Log.d(TAG, "AndLinker onUnBind()")
    }

    fun getPid(view: View) {
        mRemoteService?.let {
            Toast.makeText(this, it.pid.toString(), Toast.LENGTH_SHORT).show()
        }
    }

    fun basicTypes(view: View) {
        mRemoteService?.basicTypes(1, 2L, true, 3.0f, 4.0, "str")
    }

    override fun onDestroy() {
        super.onDestroy()
        mLinker.setBindCallback(null)
//        mLinker.unRegisterObject(mRemoteCallback)
        mLinker.unbind()
    }
}
