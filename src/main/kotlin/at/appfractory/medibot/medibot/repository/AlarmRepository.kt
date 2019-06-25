package at.appfractory.medibot.medibot.repository

import at.appfractory.medibot.medibot.model.Alarm

/**
 * Created by Kai Takac on 2019-06-22.
 */
interface AlarmRepository {

    fun findAll(): Iterable<Alarm>
    fun findByChatId(chatId: String): Iterable<Alarm>
    fun findByChatIdAndName(chatId: String, name: String): Iterable<Alarm>
    fun delete(alarm: Alarm)
    fun save(alarm: Alarm)
}