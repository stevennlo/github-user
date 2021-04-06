package com.example.githubuser.viewmodel

import android.app.Application
import androidx.core.content.edit
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.githubuser.R
import com.example.githubuser.model.Setting
import com.example.githubuser.service.AlarmReceiver.Companion.cancelAlarm
import com.example.githubuser.service.AlarmReceiver.Companion.isAlarmSet
import com.example.githubuser.service.AlarmReceiver.Companion.setAlarm
import com.example.githubuser.util.getSettingsPref
import java.util.*

class SettingsViewModel(application: Application) : AndroidViewModel(application) {
    private val _settings = MutableLiveData<Map<String, Setting>>()
    val settings: LiveData<Map<String, Setting>> = _settings
    private val settingsPref = application.getSettingsPref()
    private val mApplication = application

    init {
        val isDailyReminderActive = settingsPref.getBoolean("daily_reminder", false)
        _settings.postValue(
            mutableMapOf(
                "daily_reminder" to Setting(
                    "daily_reminder",
                    "Daily reminder",
                    "Remind daily at 09.00 AM",
                    isDailyReminderActive
                )
            )
        )
        setAlarm(isDailyReminderActive)

        settingsPref
            .registerOnSharedPreferenceChangeListener { sharedPreferences, key ->
                settings.value?.get(key)?.let {
                    val isActive = sharedPreferences.getBoolean(key, false)
                    it.isActive = isActive
                    _settings.postValue(_settings.value)
                    if (key == "daily_reminder") {
                        setAlarm(isActive)
                    }
                }
            }
    }

    fun changeSetting(key: String, value: Boolean) = settingsPref.edit {
        putBoolean(key, value)
    }

    private fun setAlarm(isActive: Boolean) {
        if (isActive) {
            if (!isAlarmSet(mApplication)) {
                setAlarm(
                    mApplication,
                    mApplication.getString(R.string.app_name),
                    mApplication.getString(R.string.daily_reminder_message),
                    Calendar.getInstance().apply {
                        set(Calendar.HOUR_OF_DAY, 9)
                        set(Calendar.MINUTE, 0)
                        set(Calendar.SECOND, 0)
                    }
                )
            }
        } else {
            if (isAlarmSet(mApplication)) {
                cancelAlarm(mApplication)
            }
        }
    }
}