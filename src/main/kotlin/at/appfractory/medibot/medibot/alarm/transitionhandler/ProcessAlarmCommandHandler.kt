package at.appfractory.medibot.medibot.alarm.transitionhandler

import at.appfractory.medibot.medibot.alarm.ChatStateTransitionHandler
import at.appfractory.medibot.medibot.alarm.StateMachineResponse
import at.appfractory.medibot.medibot.model.Chat
import at.appfractory.medibot.medibot.model.ChatState
import at.appfractory.medibot.medibot.repository.AlarmRepository
import org.springframework.stereotype.Service

/**
 * Created by Kai Takac on 2019-06-22.
 */
@Service
class ProcessAlarmCommandHandler(val repository: AlarmRepository) : ChatStateTransitionHandler {

    override fun processTransition(chat: Chat, transitionPayload: Any?): Triple<ChatState, String?, StateMachineResponse> {
        val alarmCommandPayload = chat.statePayload
                ?: return Triple(ChatState.Normal, null, StateMachineResponse.INVALID_COMMAND)
        val alarmName = transitionPayload as? String
                ?: return Triple(ChatState.Normal, null, StateMachineResponse.INVALID_COMMAND)
        val alarm = repository.findByChatIdAndName(chat.chatId, alarmName).firstOrNull()
                ?: return Triple(ChatState.Normal, null, StateMachineResponse.INVALID_ALARM_NAME)

        when (alarmCommandPayload.toLowerCase()) {
            "delete" -> {
                repository.delete(alarm)
                return Triple(ChatState.Normal, null, StateMachineResponse.DELETED_SUCCESSFUL)
            }
            "continue" -> {
                alarm.continueAlarm()
                repository.save(alarm)
                return Triple(ChatState.Normal, null, StateMachineResponse.CONTINUED_SUCCESSFUL)
            }
            "pause" -> {
                alarm.pauseAlarm()
                repository.save(alarm)
                return Triple(ChatState.Normal, null, StateMachineResponse.PAUSED_SUCCESSFUL)
            }
            else -> return Triple(ChatState.Normal, null, StateMachineResponse.INVALID_COMMAND)
        }
    }
}