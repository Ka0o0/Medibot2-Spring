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
class CreateNewMediNameHandler(val alarmRepository: AlarmRepository) : ChatStateTransitionHandler {

    override fun processTransition(chat: Chat, transitionPayload: Any?): Triple<ChatState, String?, StateMachineResponse> {
        val name = transitionPayload as? String
                ?: return Triple(ChatState.CreateNewMediStep1, null, StateMachineResponse.INVALID_ALARM_NAME)

        if (!isValidName(name)) {
            return Triple(ChatState.CreateNewMediStep1, null, StateMachineResponse.INVALID_ALARM_NAME)
        }

        val alarms = alarmRepository.findByChatIdAndName(chat.chatId, name)
        if (alarms.count() > 0) {
            return Triple(ChatState.CreateNewMediStep1, null, StateMachineResponse.DUPLICATED_ALARM_NAME)
        }

        return Triple(ChatState.CreateNewMediStep2, name, StateMachineResponse.AWAITING_INTERVAL)
    }

    private fun isValidName(name: String): Boolean {
        return name.trim().isNotEmpty()
    }
}