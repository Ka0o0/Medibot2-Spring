package at.appfractory.medibot.medibot.repository

import at.appfractory.medibot.medibot.model.Chat

/**
 * Created by Kai Takac on 2019-06-22.
 */
interface ChatRepository {

    fun getChatByIdOrCreate(chatId: String): Chat
    fun saveChat(chat: Chat)
}