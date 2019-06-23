package at.appfractory.medibot.medibot.alarm.transitionhandler

import at.appfractory.medibot.medibot.model.ChatState
import at.appfractory.medibot.medibot.model.ChatStatePayload
import at.appfractory.medibot.medibot.alarm.ChatStateTransitionHandler
import at.appfractory.medibot.medibot.alarm.StateMachineResponse
import at.appfractory.medibot.medibot.model.statepayload.AlarmCommandStatePayload
import at.appfractory.medibot.medibot.model.statepayload.AlarmCommandStatePayload.AlarmCommand.Delete
import at.appfractory.medibot.medibot.model.Alarm
import at.appfractory.medibot.medibot.model.Chat
import at.appfractory.medibot.medibot.repository.AlarmRepository
import org.springframework.stereotype.Service

/**
 * Created by Kai Takac on 2019-06-22.
 */
@Service
class StopAlarmCommandHandler(val repository: AlarmRepository) : ChatStateTransitionHandler {

    override fun processTransition(chat: Chat, transitionPayload: Any?): Triple<ChatState, ChatStatePayload?, StateMachineResponse> {
        val alarms = repository.getAlarmsForChatId(chat.chatId)
        alarms.forEach {
            it.stop()
            repository.saveAlarm(it)
        }

        return Triple(ChatState.Normal, null, StateMachineResponse.STOPPED_SUCCESSFUL)
    }

}