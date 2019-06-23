package at.appfractory.medibot.medibot.model

import java.time.DayOfWeek
import java.time.LocalDateTime
import java.time.LocalTime

/**
 * Created by Kai Takac on 2019-06-22.
 */
data class Alarm(val chatId: String, val name: String, var interval: Map<DayOfWeek, LocalTime>, var nextFiring: LocalDateTime?, var paused: Boolean) {

    fun shouldRing(): Boolean {
        val nextFiring = nextFiring ?: return false
        return !paused && nextFiring < LocalDateTime.now()
    }

    fun snooze() {
        nextFiring = LocalDateTime.now().plusMinutes(10)
    }

    fun stop() {
        recalculateNextFiring()
    }

    private fun recalculateNextFiring() {
        nextFiring = null
        val now = LocalDateTime.now()
        for (i in 0..6) {
            val targetDate = now.plusDays(i.toLong())
            val time = interval[targetDate.dayOfWeek] ?: continue
            val newFiring = LocalDateTime.of(targetDate.toLocalDate(), time)

            if (newFiring <= now) continue

            if (nextFiring == null || (nextFiring!! > newFiring)) {
                nextFiring = newFiring
                break
            }
        }
    }

    fun continueAlarm() {
        recalculateNextFiring()
        paused = false
    }

    fun pauseAlarm() {
        paused = true;
    }
}