package at.appfractory.medibot.medibot.repository

import at.appfractory.medibot.medibot.model.Alarm

/**
 * Created by Kai Takac on 2019-06-22.
 */
interface AlarmRepository {

    fun getAllAlarms(): Array<Alarm>
    fun getAlarmsForChatId(chatId: String): Array<Alarm>
    fun getAlarm(chatId: String, name: String): Alarm?
    fun removeAlarmForChatId(alarm: Alarm)
    fun saveAlarm(alarm: Alarm)
}