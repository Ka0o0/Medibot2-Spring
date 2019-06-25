package at.appfractory.medibot.medibot.alarm.transitionhandler

import at.appfractory.medibot.medibot.alarm.ChatStateTransitionHandler
import at.appfractory.medibot.medibot.alarm.StateMachineResponse
import at.appfractory.medibot.medibot.model.Chat
import at.appfractory.medibot.medibot.model.ChatState
import org.springframework.stereotype.Service

/**
 * Created by Kai Takac on 2019-06-22.
 */
@Service
class ListAlarmsCommandHandler : ChatStateTransitionHandler {
    override fun processTransition(chat: Chat, transitionPayload: Any?): Triple<ChatState, String?, StateMachineResponse> {
        return Triple(ChatState.Normal, null, StateMachineResponse.LIST_ALARMS)
    }
}