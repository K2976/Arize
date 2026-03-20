package com.example.arize

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.arize.ui.theme.ArizeTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE)
        val initialOnboardingDone = prefs.getBoolean(KEY_ONBOARDING_DONE, false)
        val initialProfile = loadProfile(prefs)

        enableEdgeToEdge()
        setContent {
            ArizeTheme {
                ArizeApp(
                    initialOnboardingDone = initialOnboardingDone,
                    initialProfile = initialProfile,
                    onOnboardingFinished = { profile ->
                        saveProfile(prefs, profile)
                        prefs.edit().putBoolean(KEY_ONBOARDING_DONE, true).apply()
                    },
                    onProfileUpdated = { profile ->
                        saveProfile(prefs, profile)
                    }
                )
            }
        }
    }
}
