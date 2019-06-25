package at.appfractory.medibot.medibot.alarm.transitionhandler

import at.appfractory.medibot.medibot.alarm.ChatStateTransitionHandler
import at.appfractory.medibot.medibot.alarm.StateMachineResponse
import at.appfractory.medibot.medibot.model.Alarm
import at.appfractory.medibot.medibot.model.Chat
import at.appfractory.medibot.medibot.model.ChatState
import at.appfractory.medibot.medibot.repository.AlarmRepository
import org.springframework.stereotype.Service
import java.time.DayOfWeek
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException

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

    override fun processTransition(chat: Chat, transitionPayload: Any?): Triple<ChatState, String?, StateMachineResponse> {
        val alarmName = chat.statePayload
                ?: return Triple(ChatState.Normal, null, StateMachineResponse.INVALID_COMMAND)
        val invalidTimeResponse = Triple(ChatState.Normal, null, StateMachineResponse.INVALID_INTERVAL)
        val intervalString = transitionPayload as? String ?: return invalidTimeResponse

        val timeAndDaySplit = intervalString.split(" ")
        if (timeAndDaySplit.isEmpty()) return invalidTimeResponse

        val localTime = try {
            LocalTime.parse(timeAndDaySplit[0])
        } catch (e: DateTimeParseException) {
            return invalidTimeResponse
        }
        val interval: MutableMap<Int, String> = mutableMapOf()

        when (timeAndDaySplit.size) {
            1 -> {
                for (i in 1..7) {
                    interval[i] = localTime.format(DateTimeFormatter.ISO_LOCAL_TIME)
                }
            }
            2 -> {
                val daysOfWeekSplit = timeAndDaySplit[1].split(",")
                if (daysOfWeekSplit.size == 1 && daysOfWeekSplit[0].toLowerCase() == "daily") {
                    for (i in 1..7) {
                        interval[i] = localTime.format(DateTimeFormatter.ISO_LOCAL_TIME)
                    }
                } else {
                    daysOfWeekSplit.forEach {
                        val dayOfWeek = dayOfWeekMapping[it.toLowerCase()] ?: return invalidTimeResponse
                        interval[dayOfWeek.value] = localTime.format(DateTimeFormatter.ISO_LOCAL_TIME)
                    }
                }
            }
            else -> return invalidTimeResponse
        }

        val alarm = Alarm(null, chat.chatId, alarmName, interval, null, false)
        alarm.continueAlarm()
        alarmRepository.save(alarm)
        return Triple(ChatState.Normal, null, StateMachineResponse.CREATION_SUCCESSFUL)
    }

}