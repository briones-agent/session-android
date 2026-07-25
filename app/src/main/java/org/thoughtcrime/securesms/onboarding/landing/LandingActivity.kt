package org.thoughtcrime.securesms.onboarding.landing

import android.os.Bundle
import dagger.hilt.android.AndroidEntryPoint
import org.session.libsession.utilities.TextSecurePreferences
import org.thoughtcrime.securesms.BaseActionBarActivity
import org.thoughtcrime.securesms.crypto.IdentityKeyUtil
import org.thoughtcrime.securesms.onboarding.loadaccount.LoadAccountActivity
import org.thoughtcrime.securesms.onboarding.pickname.startPickDisplayNameActivity
import org.thoughtcrime.securesms.service.KeyCachingService
import org.thoughtcrime.securesms.ui.setComposeContent
import org.thoughtcrime.securesms.util.setUpActionBarSessionLogo
import org.thoughtcrime.securesms.util.start
import javax.inject.Inject

@AndroidEntryPoint
class LandingActivity: BaseActionBarActivity() {

    @Inject
    internal lateinit var prefs: TextSecurePreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // We always hit this LandingActivity on launch - but if there is a previous instance of
        // Session then close this activity to resume the last activity from the previous instance.
        if (!isTaskRoot) { finish(); return }

        setUpActionBarSessionLogo(true)

        setComposeContent {
            LandingScreen(
                createAccount = { startPickDisplayNameActivity() },
                loadAccount = { start<LoadAccountActivity>() }
            )
        }

        addExpoLaunchButton()

        IdentityKeyUtil.generateIdentityKeyPair(this)
        TextSecurePreferences.setPasswordDisabled(this, true)
        // AC: This is a temporary workaround to trick the old code that the screen is unlocked.
        KeyCachingService.setMasterSecret(applicationContext, Object())
    }

    /**
     * Adds a floating "Expo" button that launches [org.thoughtcrime.securesms.ExpoActivity], which
     * renders the React Native screen from the Expo brownfield AAR. Added over the Compose content
     * via addContentView on this landing screen (the first screen users see before an account).
     */
    private fun addExpoLaunchButton() {
        val button = android.widget.Button(this).apply {
            text = "Expo"
            isAllCaps = false
            setOnClickListener {
                startActivity(android.content.Intent(this@LandingActivity, org.thoughtcrime.securesms.ExpoActivity::class.java))
            }
        }
        val margin = (16 * resources.displayMetrics.density).toInt()
        val params = android.widget.FrameLayout.LayoutParams(
            android.widget.FrameLayout.LayoutParams.WRAP_CONTENT,
            android.widget.FrameLayout.LayoutParams.WRAP_CONTENT,
            android.view.Gravity.BOTTOM or android.view.Gravity.END
        ).apply { setMargins(margin, margin, margin, margin) }
        addContentView(button, params)
    }
}
