package at.appfractory.medibot.medibot.repository.data

import at.appfractory.medibot.medibot.model.Chat
import at.appfractory.medibot.medibot.repository.ChatRepository
import org.springframework.data.repository.CrudRepository

/**
 * Created by Kai Takac on 2019-06-25.
 */
interface JPAChatRepository: CrudRepository<Chat, String>, ChatRepository {
}