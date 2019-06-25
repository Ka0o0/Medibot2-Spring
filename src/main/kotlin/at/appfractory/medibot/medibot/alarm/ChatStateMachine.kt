package at.appfractory.medibot.medibot.alarm

import at.appfractory.medibot.medibot.alarm.transitionhandler.*
import at.appfractory.medibot.medibot.model.Chat
import at.appfractory.medibot.medibot.repository.ChatRepository
import at.appfractory.medibot.medibot.model.ChatState
import org.springframework.stereotype.Service

/**
 * Created by Kai Takac on 2019-06-22.
 */
@Service
class ChatStateMachine(
        val chatRepository: ChatRepository,
        val continueAlarmCommandHandler: ContinueAlarmCommandHandler,
        val createAlarmCommandHandler: CreateAlarmCommandHandler,
        val deleteAlarmCommandHandler: DeleteAlarmCommandHandler,
        val pauseAlarmCommandHandler: PauseAlarmCommandHandler,
        val processAlarmCommandHandler: ProcessAlarmCommandHandler,
        val stopAlarmCommandHandler: StopAlarmCommandHandler,
        val createNewMediNameHandler: CreateNewMediNameHandler,
        val createNewMediIntervalHandler: CreateNewMediIntervalHandler,
        val listAlarmsCommandHandler: ListAlarmsCommandHandler,
        val helpCommandHandler: HelpCommandHandler
) {


    private val stateMachine: Map<ChatState, Map<ChatTransition, ChatStateTransitionHandler>>
        get() =
            mapOf(
                    Pair(
                            ChatState.Normal,
                            mapOf(
                                    Pair(ChatTransition.CONTINUE_ALARM_COMMAND, continueAlarmCommandHandler),
                                    Pair(ChatTransition.DELETE_ALARM_COMMAND, deleteAlarmCommandHandler),
                                    Pair(ChatTransition.CREATE_ALARM_COMMAND, createAlarmCommandHandler),
                                    Pair(ChatTransition.PAUSE_ALARM_COMMAND, pauseAlarmCommandHandler),
                                    Pair(ChatTransition.LIST_ALARMS_COMMAND, listAlarmsCommandHandler),
                                    Pair(ChatTransition.HELP_COMMAND, helpCommandHandler)
                            )
                    ),
                    Pair(
                            ChatState.AwaitingNameForOperation,
                            mapOf(
                                    Pair(ChatTransition.TEXT_ENTERED, processAlarmCommandHandler)
                            )
                    ),
                    Pair(
                            ChatState.Ringing,
                            mapOf(
                                    Pair(ChatTransition.STOP_ALARM_COMMAND, stopAlarmCommandHandler)
                            )
                    ),
                    Pair(
                            ChatState.CreateNewMediStep1,
                            mapOf(
                                    Pair(ChatTransition.TEXT_ENTERED, createNewMediNameHandler)
                            )
                    ),
                    Pair(
                            ChatState.CreateNewMediStep2,
                            mapOf(
                                    Pair(ChatTransition.TEXT_ENTERED, createNewMediIntervalHandler)
                            )
                    )
            )


    fun processTransition(chatId: String, transition: ChatTransition, transitionPayload: Any?): StateMachineResponse {
        val chat: Chat
        val databaseChat = chatRepository.findByChatId(chatId)
        if (databaseChat != null) {
            chat = databaseChat
        } else {
            chat = Chat(chatId, ChatState.Normal, null)
        }
        val possibleTransitions = stateMachine[chat.state] ?: return StateMachineResponse.INVALID_COMMAND
        val chatStateTransitionHandler = possibleTransitions[transition] ?: return StateMachineResponse.INVALID_COMMAND

        val (newState, newPayload, response) = chatStateTransitionHandler.processTransition(chat, transitionPayload)
        chat.state = newState
        chat.statePayload = newPayload
        chatRepository.save(chat)
        return response
    }
}