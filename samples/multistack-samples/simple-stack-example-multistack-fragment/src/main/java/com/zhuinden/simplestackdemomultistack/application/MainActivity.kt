package com.zhuinden.simplestackdemomultistack.application

import android.os.Bundle
import android.view.MotionEvent
import androidx.appcompat.app.AppCompatActivity
import com.zhuinden.simplestack.SimpleStateChanger
import com.zhuinden.simplestack.StateChange
import com.zhuinden.simplestackdemomultistack.R
import com.zhuinden.simplestackdemomultistack.core.navigation.Multistack
import com.zhuinden.simplestackdemomultistack.core.navigation.MultistackFragmentStateChanger
import com.zhuinden.simplestackdemomultistack.features.main.chromecast.ChromeCastKey
import com.zhuinden.simplestackdemomultistack.features.main.cloudsync.CloudSyncKey
import com.zhuinden.simplestackdemomultistack.features.main.list.ListKey
import com.zhuinden.simplestackdemomultistack.features.main.mail.MailKey
import com.zhuinden.simplestackdemomultistack.util.onMenuItemSelected
import it.sephiroth.android.library.bottomnavigation.BottomNavigation

class MainActivity : AppCompatActivity(), SimpleStateChanger.NavigationHandler {
    lateinit var multistack: Multistack

    private var isAnimating: Boolean = false

    enum class StackType {
        CLOUDSYNC,
        CHROMECAST,
        MAIL,
        LIST
    }

    private lateinit var multistackFragmentStateChanger: MultistackFragmentStateChanger

    override fun onCreate(savedInstanceState: Bundle?) {
        this.multistack = (lastCustomNonConfigurationInstance as Multistack?)
            ?: Multistack().apply {
                add(CloudSyncKey())
                add(ChromeCastKey())
                add(MailKey())
                add(ListKey())

                if (savedInstanceState != null) {
                    fromBundle(savedInstanceState.getParcelable("multistack"))
                }
            }

        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        multistackFragmentStateChanger = MultistackFragmentStateChanger(R.id.root, supportFragmentManager)

        findViewById<BottomNavigation>(R.id.bottomNavigationView).onMenuItemSelected { menuItemId: Int, itemIndex: Int, b: Boolean ->
            multistack.setSelectedStack(StackType.values()[itemIndex].name)
        }

        multistack.setStateChanger(SimpleStateChanger(this))
    }

    override fun onRetainCustomNonConfigurationInstance(): Any = multistack

    override fun onPostResume() {
        super.onPostResume()
        multistack.unpause()
    }

    override fun onPause() {
        multistack.pause()
        super.onPause()
    }

    override fun onBackPressed() {
        val backstack = multistack.getSelectedStack()
        if (!backstack.goBack()) {
            super.onBackPressed()
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putParcelable("multistack", multistack.toBundle())
    }

    override fun onDestroy() {
        super.onDestroy()
        multistack.executePendingStateChange()

        if (isFinishing) {
            multistack.finalize()
        }
    }

    override fun getSystemService(name: String): Any? {
        if (::multistack.isInitialized) {
            if (multistack.has(name)) {
                return multistack.get(name)
            }
        }
        return super.getSystemService(name)
    }

    override fun dispatchTouchEvent(ev: MotionEvent): Boolean {
        return !isAnimating && super.dispatchTouchEvent(ev)
    }

    override fun onNavigationEvent(stateChange: StateChange) {
        multistackFragmentStateChanger.handleStateChange(stateChange)
    }
}