package at.appfractory.medibot.medibot.repository

import at.appfractory.medibot.medibot.model.Alarm
import org.springframework.stereotype.Service

/**
 * Created by Kai Takac on 2019-06-22.
 */
@Service
class MemoryAlarmRepository : AlarmRepository {

    private var store: MutableMap<String, MutableMap<String, Alarm>> = mutableMapOf()

    override fun getAllAlarms(): Array<Alarm> {
        val temp = arrayListOf<Alarm>()
        temp.addAll(store.flatMap { it.value.values })
        return temp.toArray(arrayOf<Alarm>())
    }

    override fun getAlarm(chatId: String, name: String): Alarm? {
        return store[chatId]?.get(name)
    }

    override fun saveAlarm(alarm: Alarm) {
        if (!store.containsKey(alarm.chatId)) {
            store.put(alarm.chatId, mutableMapOf())
        }

        store[alarm.chatId]?.put(alarm.name, alarm)
    }

    override fun removeAlarmForChatId(alarm: Alarm) {
        store[alarm.chatId]?.remove(alarm.name)
    }

    override fun getAlarmsForChatId(chatId: String): Array<Alarm> {
        val values = store[chatId]?.values ?: return emptyArray()
        val temp = arrayListOf<Alarm>()
        temp.addAll(values)
        return temp.toArray(arrayOf<Alarm>())
    }
}