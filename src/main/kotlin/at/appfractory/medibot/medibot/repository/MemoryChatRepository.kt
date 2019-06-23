package at.appfractory.medibot.medibot.repository

import at.appfractory.medibot.medibot.model.Chat
import at.appfractory.medibot.medibot.model.ChatState
import org.springframework.stereotype.Service

/**
 * Created by Kai Takac on 2019-06-22.
 */
@Service
class MemoryChatRepository: ChatRepository {

    private val store: MutableMap<String, Chat> = mutableMapOf()

    override fun getChatByIdOrCreate(chatId: String): Chat {
        return store[chatId] ?: return Chat(chatId, ChatState.Normal, null)
    }

    override fun saveChat(chat: Chat) {
        store[chat.chatId] = chat
    }

}