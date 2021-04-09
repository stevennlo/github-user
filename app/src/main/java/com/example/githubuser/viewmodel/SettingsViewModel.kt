package com.example.githubuser.viewmodel

import android.content.Context
import androidx.core.content.edit
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.githubuser.R
import com.example.githubuser.model.Setting
import com.example.githubuser.service.AlarmReceiver.Companion.cancelAlarm
import com.example.githubuser.service.AlarmReceiver.Companion.isAlarmSet
import com.example.githubuser.service.AlarmReceiver.Companion.setAlarm
import com.example.githubuser.util.getSettingsPref
import java.util.*

class SettingsViewModel : ViewModel() {
    private val _settings = MutableLiveData<Map<String, Setting>>()
    val settings: LiveData<Map<String, Setting>> = _settings

    fun init(context: Context) {
        val isDailyReminderActive = context.getSettingsPref().getBoolean("daily_reminder", false)
        _settings.postValue(
            mutableMapOf(
                "daily_reminder" to Setting(
                    "daily_reminder",
                    context.getString(R.string.daily_reminder_title),
                    context.getString(R.string.daily_reminder_description),
                    isDailyReminderActive
                )
            )
        )
        setAlarm(context, isDailyReminderActive)

        context.getSettingsPref()
            .registerOnSharedPreferenceChangeListener { sharedPreferences, key ->
                settings.value?.get(key)?.let {
                    val isActive = sharedPreferences.getBoolean(key, false)
                    it.isActive = isActive
                    _settings.postValue(_settings.value)
                    when (key) {
                        "daily_reminder" -> setAlarm(context, isActive)
                    }
                }
            }
    }

    fun changeSetting(context: Context, key: String, value: Boolean) {
        context.getSettingsPref().edit {
            putBoolean(key, value)
        }
    }

    private fun setAlarm(context: Context, isActive: Boolean) {
        if (isActive) {
            if (!isAlarmSet(context)) {
                setAlarm(
                    context,
                    context.getString(R.string.app_name),
                    context.getString(R.string.daily_reminder_message),
                    Calendar.getInstance().apply {
                        set(Calendar.HOUR_OF_DAY, 9)
                        set(Calendar.MINUTE, 0)
                        set(Calendar.SECOND, 0)
                    }
                )
            }
        } else {
            if (isAlarmSet(context)) {
                cancelAlarm(context)
            }
        }
    }
}