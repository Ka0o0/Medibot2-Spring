package at.appfractory.medibot.medibot.repository.memory

import at.appfractory.medibot.medibot.model.Chat
import at.appfractory.medibot.medibot.model.ChatState
import at.appfractory.medibot.medibot.repository.ChatRepository
import org.springframework.stereotype.Service

/**
 * Created by Kai Takac on 2019-06-22.
 */
class MemoryChatRepository: ChatRepository {

    private val store: MutableMap<String, Chat> = mutableMapOf()

    override fun findByChatId(chatId: String): Chat {
        return store[chatId] ?: return Chat(chatId, ChatState.Normal, null)
    }

    override fun save(chat: Chat) {
        store[chat.chatId] = chat
    }

}