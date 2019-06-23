package at.appfractory.medibot.medibot.alarm.transitionhandler

import at.appfractory.medibot.medibot.model.ChatState
import at.appfractory.medibot.medibot.model.ChatStatePayload
import at.appfractory.medibot.medibot.alarm.ChatStateTransitionHandler
import at.appfractory.medibot.medibot.alarm.StateMachineResponse
import at.appfractory.medibot.medibot.model.statepayload.AlarmCommandStatePayload
import at.appfractory.medibot.medibot.model.Alarm
import at.appfractory.medibot.medibot.model.Chat
import at.appfractory.medibot.medibot.model.statepayload.AlarmCommandStatePayload.AlarmCommand.*
import at.appfractory.medibot.medibot.repository.AlarmRepository
import org.springframework.stereotype.Service

/**
 * Created by Kai Takac on 2019-06-22.
 */
@Service
class ProcessAlarmCommandHandler(val repository: AlarmRepository) : ChatStateTransitionHandler {

    override fun processTransition(chat: Chat, transitionPayload: Any?): Triple<ChatState, ChatStatePayload?, StateMachineResponse> {
        val alarmCommandPayload = chat.statePayload as? AlarmCommandStatePayload
                ?: return Triple(ChatState.Normal, null, StateMachineResponse.INVALID_COMMAND)
        val alarmName = transitionPayload as? String
                ?: return Triple(ChatState.Normal, null, StateMachineResponse.INVALID_COMMAND)
        val alarm = repository.getAlarm(chat.chatId, alarmName)
                ?: return Triple(ChatState.Normal, null, StateMachineResponse.INVALID_ALARM_NAME)

        when (alarmCommandPayload.command) {
            Delete -> {
                repository.removeAlarmForChatId(alarm)
                return Triple(ChatState.Normal, null, StateMachineResponse.DELETED_SUCCESSFUL)
            }
            Continue -> {
                alarm.continueAlarm()
                repository.saveAlarm(alarm)
                return Triple(ChatState.Normal, null, StateMachineResponse.CONTINUED_SUCCESSFUL)
            }
            Pause -> {
                alarm.pauseAlarm()
                repository.saveAlarm(alarm)
                return Triple(ChatState.Normal, null, StateMachineResponse.PAUSED_SUCCESSFUL)
            }
        }
    }
}