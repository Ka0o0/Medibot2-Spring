package at.appfractory.medibot.medibot.alarm.transitionhandler

import at.appfractory.medibot.medibot.model.ChatState
import at.appfractory.medibot.medibot.model.ChatStatePayload
import at.appfractory.medibot.medibot.alarm.ChatStateTransitionHandler
import at.appfractory.medibot.medibot.alarm.StateMachineResponse
import at.appfractory.medibot.medibot.model.Chat
import at.appfractory.medibot.medibot.model.statepayload.AlarmCommandStatePayload
import org.springframework.stereotype.Service

/**
 * Created by Kai Takac on 2019-06-22.
 */
@Service
class ContinueAlarmCommandHandler : ChatStateTransitionHandler {
    override fun processTransition(chat: Chat, transitionPayload: Any?): Triple<ChatState, ChatStatePayload?, StateMachineResponse> {
        return Triple(ChatState.AwaitingNameForOperation, AlarmCommandStatePayload(AlarmCommandStatePayload.AlarmCommand.Continue), StateMachineResponse.AWAITING_ALARM_SELECTION)
    }
}