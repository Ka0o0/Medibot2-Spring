package at.appfractory.medibot.medibot.alarm.transitionhandler

import at.appfractory.medibot.medibot.model.ChatState
import at.appfractory.medibot.medibot.model.ChatStatePayload
import at.appfractory.medibot.medibot.alarm.ChatStateTransitionHandler
import at.appfractory.medibot.medibot.alarm.StateMachineResponse
import at.appfractory.medibot.medibot.model.Alarm
import at.appfractory.medibot.medibot.model.Chat
import at.appfractory.medibot.medibot.model.statepayload.CreateNewAlarmStatePayload
import at.appfractory.medibot.medibot.repository.AlarmRepository
import org.springframework.stereotype.Service
import java.time.DayOfWeek
import java.time.LocalTime

/**
 * Created by Kai Takac on 2019-06-22.
 */
@Service
class CreateNewMediIntervalHandler(val alarmRepository: AlarmRepository) : ChatStateTransitionHandler {

    private val dayOfWeekMapping: Map<String, DayOfWeek> = mapOf(
            Pair("mon", DayOfWeek.MONDAY),
            Pair("tue", DayOfWeek.TUESDAY),
            Pair("wed", DayOfWeek.WEDNESDAY),
            Pair("thu", DayOfWeek.THURSDAY),
            Pair("fri", DayOfWeek.FRIDAY),
            Pair("sat", DayOfWeek.SATURDAY),
            Pair("sun", DayOfWeek.SUNDAY)
    )

    override fun processTransition(chat: Chat, transitionPayload: Any?): Triple<ChatState, ChatStatePayload?, StateMachineResponse> {
        val alarmName = (chat.statePayload as? CreateNewAlarmStatePayload)?.name
                ?: return Triple(ChatState.Normal, null, StateMachineResponse.INVALID_COMMAND)
        val invalidTimeResponse = Triple(ChatState.Normal, null, StateMachineResponse.INVALID_INTERVAL)
        val intervalString = transitionPayload as? String ?: return invalidTimeResponse

        val timeAndDaySplit = intervalString.split(" ")
        if (timeAndDaySplit.isEmpty()) return invalidTimeResponse
        val localTime = LocalTime.parse(timeAndDaySplit[0]) ?: return invalidTimeResponse
        val interval: MutableMap<DayOfWeek, LocalTime> = mutableMapOf()

        when (timeAndDaySplit.size) {
            1 -> {
                for (i in 1..7) {
                    interval[DayOfWeek.of(i)] = localTime
                }
            }
            2 -> {
                val daysOfWeekSplit = timeAndDaySplit[1].split(",")
                if (daysOfWeekSplit.size == 1 && daysOfWeekSplit[0].toLowerCase() == "daily") {
                    for (i in 1..7) {
                        interval[DayOfWeek.of(i)] = localTime
                    }
                } else {
                    daysOfWeekSplit.forEach {
                        val dayOfWeek = dayOfWeekMapping[it.toLowerCase()] ?: return invalidTimeResponse
                        interval[dayOfWeek] = localTime
                    }
                }
            }
            else -> return invalidTimeResponse
        }

        val alarm = Alarm(chat.chatId, alarmName, interval, null, false)
        alarm.continueAlarm()
        alarmRepository.saveAlarm(alarm)
        return Triple(ChatState.Normal, null, StateMachineResponse.CREATION_SUCCESSFUL)
    }

}