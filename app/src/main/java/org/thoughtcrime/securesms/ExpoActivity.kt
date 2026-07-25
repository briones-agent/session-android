package org.thoughtcrime.securesms

import android.os.Bundle
import session.brownfield.brownfield.BrownfieldActivity

/**
 * Hosts the React Native screen shipped by the Expo brownfield AAR
 * (`session.brownfield:brownfield-fused-release`).
 *
 * [BrownfieldActivity] (from the fused AAR) extends AppCompatActivity and forwards configuration
 * changes; `showReactNativeFragment` mounts the RN root fragment (module "main") and wires native
 * back-button handling. The JS bundle is embedded in the release AAR, so no Metro is required.
 */
class ExpoActivity : BrownfieldActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        showReactNativeFragment("main")
    }
}
