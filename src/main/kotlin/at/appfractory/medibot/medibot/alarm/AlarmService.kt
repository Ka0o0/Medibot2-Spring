package at.appfractory.medibot.medibot.alarm

import at.appfractory.medibot.medibot.model.Alarm
import at.appfractory.medibot.medibot.model.ChatState
import at.appfractory.medibot.medibot.repository.AlarmRepository
import at.appfractory.medibot.medibot.repository.ChatRepository
import org.springframework.stereotype.Service

val THREAD_SLEEP_DURATION = 10000

/**
 * Created by Kai Takac on 2019-06-22.
 */
@Service
class AlarmService(val alarmRepository: AlarmRepository, val chatRepository: ChatRepository) : Runnable {

    var observers: MutableList<AlarmServiceObserver> = mutableListOf()

    private var thread: Thread? = null
    private var isRunnning: Boolean = false

    interface AlarmServiceObserver {
        fun alarmDidFire(alarm: Alarm)
    }

    fun start() {
        if (isRunnning) return
        isRunnning = true
        thread = Thread(this)
        thread?.start()
    }

    fun stop() {
        isRunnning = false
    }

    override fun run() {
        val allAlarms = alarmRepository.findAll()
        allAlarms.forEach {
            it.stop()
            alarmRepository.save(it)
        }

        while (isRunnning) {
            val allAlarms = alarmRepository.findAll()
            allAlarms.forEach {
                if (it.shouldRing()) {
                    val chat = chatRepository.findByChatId(it.chatId) ?: return
                    chat.state = ChatState.Ringing
                    chatRepository.save(chat)

                    it.snooze()
                    alarmRepository.save(it)

                    notifyObserversAboutAlarm(it)
                }
            }
            Thread.sleep(THREAD_SLEEP_DURATION.toLong())
        }
    }

    fun notifyObserversAboutAlarm(alarm: Alarm) {
        observers.forEach { it.alarmDidFire(alarm) }
    }
}