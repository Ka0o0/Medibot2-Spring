package at.appfractory.medibot.medibot.model

/**
 * Created by Kai Takac on 2019-06-22.
 */
data class Chat(val chatId: String, var state: ChatState, var statePayload: ChatStatePayload?) {
}