package at.appfractory.medibot.medibot.alarm

import at.appfractory.medibot.medibot.model.Chat
import at.appfractory.medibot.medibot.model.ChatState
import at.appfractory.medibot.medibot.model.ChatStatePayload

/**
 * Created by Kai Takac on 2019-06-22.
 */
interface ChatStateTransitionHandler {

    fun processTransition(chat: Chat, transitionPayload: Any?): Triple<ChatState, ChatStatePayload?, StateMachineResponse>
}