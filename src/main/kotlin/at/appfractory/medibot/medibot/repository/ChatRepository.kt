package at.appfractory.medibot.medibot.repository

import at.appfractory.medibot.medibot.model.Chat

/**
 * Created by Kai Takac on 2019-06-22.
 */
interface ChatRepository {

    fun findByChatId(chatId: String): Chat?
    fun save(chat: Chat)
}