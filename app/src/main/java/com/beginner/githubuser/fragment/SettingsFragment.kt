package com.beginner.githubuser.fragment

import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.BuildCompat
import androidx.preference.ListPreference
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.SwitchPreference
import com.beginner.githubuser.R
import com.beginner.githubuser.receiver.AlarmReceiver
import com.shashank.sony.fancytoastlib.FancyToast

class SettingsFragment : PreferenceFragmentCompat() {
    private lateinit var languange: Preference
    private lateinit var reminder: SwitchPreference
    private lateinit var darkTheme: ListPreference

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        addPreferencesFromResource(R.xml.settings_preference)

        val alarmReceiver = AlarmReceiver()

        languange = findPreference(resources.getString(R.string.key_language))!!
        reminder = findPreference(resources.getString(R.string.key_notification))!!
        darkTheme = findPreference(resources.getString(R.string.key_theme))!!

        darkTheme.onPreferenceChangeListener = modeChangeListener

        languange.onPreferenceClickListener = Preference.OnPreferenceClickListener {
            startActivity(Intent(Settings.ACTION_LOCALE_SETTINGS))
            true
        }

        reminder.onPreferenceChangeListener = Preference.OnPreferenceChangeListener { _, _ ->
            if (reminder.isChecked) {
                activity?.let { alarmReceiver.cancelAlarm(it) }
                FancyToast.makeText(
                    activity,
                    getString(R.string.reminder_off),
                    Toast.LENGTH_SHORT,
                    FancyToast.ERROR,
                    false
                ).show()
                reminder.isChecked = false
            } else {
                activity?.let { alarmReceiver.setRepeatingAlarm(it) }
                FancyToast.makeText(
                    activity,
                    getString(R.string.reminder_on),
                    Toast.LENGTH_SHORT,
                    FancyToast.SUCCESS,
                    false
                ).show()
                reminder.isChecked = true
            }
            false
        }
    }

    private val modeChangeListener = object : Preference.OnPreferenceChangeListener {
        override fun onPreferenceChange(preference: Preference?, newValue: Any?): Boolean {
            newValue as? String
            when (newValue) {
                getString(R.string.night_on) -> {
                    updateTheme(AppCompatDelegate.MODE_NIGHT_YES)
                }
                getString(R.string.night_off) -> {
                    updateTheme(AppCompatDelegate.MODE_NIGHT_NO)
                }
                else -> {
                    if (BuildCompat.isAtLeastR()) {
                        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
                    } else {
                        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_AUTO_BATTERY)
                    }
                }
            }
            return true
        }

        private fun updateTheme(darkTheme: Int): Boolean {
            AppCompatDelegate.setDefaultNightMode(darkTheme)
            requireActivity().recreate()
            return true
        }
    }
}