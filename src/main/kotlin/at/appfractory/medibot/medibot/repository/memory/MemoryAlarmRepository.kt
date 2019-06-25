package at.appfractory.medibot.medibot.repository.memory

import at.appfractory.medibot.medibot.model.Alarm
import at.appfractory.medibot.medibot.repository.AlarmRepository
import org.springframework.stereotype.Service

/**
 * Created by Kai Takac on 2019-06-22.
 */
class MemoryAlarmRepository : AlarmRepository {

    private var store: MutableMap<String, MutableMap<String, Alarm>> = mutableMapOf()

    override fun findAll(): Iterable<Alarm> {
        return store.flatMap { it.value.values }
    }

    override fun findByChatIdAndName(chatId: String, name: String): Iterable<Alarm> {
        val temp = store[chatId]?.get(name) ?: return emptyList()
        return listOf(temp)
    }

    override fun save(alarm: Alarm) {
        if (!store.containsKey(alarm.chatId)) {
            store.put(alarm.chatId, mutableMapOf())
        }

        store[alarm.chatId]?.put(alarm.name, alarm)
    }

    override fun delete(alarm: Alarm) {
        store[alarm.chatId]?.remove(alarm.name)
    }

    override fun findByChatId(chatId: String): Iterable<Alarm> {
        val values = store[chatId]?.values ?: return emptyList()
        return values
    }
}