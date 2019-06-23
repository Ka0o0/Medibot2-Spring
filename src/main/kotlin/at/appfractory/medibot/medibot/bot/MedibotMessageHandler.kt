package at.appfractory.medibot.medibot.bot

import at.appfractory.medibot.medibot.alarm.ChatStateMachine
import at.appfractory.medibot.medibot.alarm.ChatTransition
import at.appfractory.medibot.medibot.alarm.StateMachineResponse
import at.appfractory.medibot.medibot.bot.response.ChoiceResponse
import at.appfractory.medibot.medibot.bot.response.ChoiceResponsePayload
import at.appfractory.medibot.medibot.bot.response.TextResponse
import at.appfractory.medibot.medibot.bot.response.TextResponsePayload
import at.appfractory.medibot.medibot.repository.AlarmRepository
import org.springframework.stereotype.Service

/**
 * Created by Kai Takac on 2019-06-21.
 */

@Service
class MedibotMessageHandler(val stateMachine: ChatStateMachine, val alarmRepository: AlarmRepository) {

    fun handleMessageFromChat(message: String, chatId: String): BotResponse {
        var payload: Any? = null
        val transition: ChatTransition

        when (message.toLowerCase()) {
            "/newmedi" -> transition = ChatTransition.CREATE_ALARM_COMMAND
            "/pausemedi" -> transition = ChatTransition.PAUSE_ALARM_COMMAND
            "/deletemedi" -> transition = ChatTransition.DELETE_ALARM_COMMAND
            "/continuemedi" -> transition = ChatTransition.CONTINUE_ALARM_COMMAND
            "/listmedis" -> transition = ChatTransition.LIST_ALARMS_COMMAND
            "/help" -> transition = ChatTransition.HELP_COMMAND
            "/start" -> transition = ChatTransition.HELP_COMMAND
            "taken" -> transition = ChatTransition.STOP_ALARM_COMMAND
            else -> {
                transition = ChatTransition.TEXT_ENTERED
                payload = message
            }
        }

        val botResponse: BotResponse
        val response = stateMachine.processTransition(chatId, transition, payload)
        when (response) {
            StateMachineResponse.INVALID_COMMAND -> botResponse = TextResponse(TextResponsePayload("Invalid command"))
            StateMachineResponse.AWAITING_INTERVAL -> botResponse = TextResponse(TextResponsePayload("Tell me the time (HH:MM) and interval (default daily, possible: Mon,Tue,Wed,Thu,Fri,Sat,Sun,Daily)"))
            StateMachineResponse.AWAITING_NAME -> botResponse = TextResponse(TextResponsePayload("Which medication would you like me to remind you on?"))
            StateMachineResponse.CONTINUED_SUCCESSFUL -> botResponse = TextResponse(TextResponsePayload("I'll again remind you"))
            StateMachineResponse.CREATION_SUCCESSFUL -> botResponse = TextResponse(TextResponsePayload("Okay, I'll remind you to take your med"))
            StateMachineResponse.PAUSED_SUCCESSFUL -> botResponse = TextResponse(TextResponsePayload("I'll no longer remind you"))
            StateMachineResponse.STOPPED_SUCCESSFUL -> botResponse = TextResponse(TextResponsePayload("Good girl :-)"))
            StateMachineResponse.AWAITING_ALARM_SELECTION -> {
                val options: MutableMap<String, String> = mutableMapOf()
                alarmRepository.getAlarmsForChatId(chatId).forEach { options[it.name] = it.name }
                botResponse = ChoiceResponse(ChoiceResponsePayload("Please select a medication.", options))
            }
            StateMachineResponse.INVALID_ALARM_NAME -> botResponse = TextResponse(TextResponsePayload("No good alarm name"))
            StateMachineResponse.DELETED_SUCCESSFUL -> botResponse = TextResponse(TextResponsePayload("Alarm removed successfully"))
            StateMachineResponse.DUPLICATED_ALARM_NAME -> botResponse = TextResponse(TextResponsePayload("Alarm name already exists"))
            StateMachineResponse.INVALID_INTERVAL -> botResponse = TextResponse(TextResponsePayload("Invalid interval format"))
            StateMachineResponse.LIST_ALARMS -> {
                val alarms = alarmRepository.getAlarmsForChatId(chatId)
                val text: String
                if (alarms.isEmpty()) {
                    text = "You have no alarms set!"
                } else {
                    var tempText = "Your set alarms:\n"
                    alarms.forEach { tempText += it.name +"\n" }
                    text = tempText
                }


                botResponse = TextResponse(TextResponsePayload(text))
            }
            StateMachineResponse.HELP -> botResponse = TextResponse(TextResponsePayload("Welcome to Medibot2. Your friendly drug reminder :-). I'm here to help, just like Clippy. Follow this link to get more information: https://github.com/Ka0o0/Medibot2-Spring"))
        }

        return botResponse
    }
}

