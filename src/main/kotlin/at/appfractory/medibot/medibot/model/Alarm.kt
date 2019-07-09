package at.appfractory.medibot.medibot.model

import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneId
import java.time.ZonedDateTime
import javax.persistence.*

/**
 * Created by Kai Takac on 2019-06-22.
 */
@Entity
data class Alarm(
        @Id
        @GeneratedValue(strategy = GenerationType.AUTO)
        var id: Long?,
        var chatId: String,
        var name: String,
        @ElementCollection(fetch=FetchType.EAGER)
        var interval: Map<Int, String>,
        var nextFiring: ZonedDateTime?,
        var paused: Boolean
) {

    constructor() : this(null, "", "", emptyMap(), null, false)

    fun shouldRing(): Boolean {
        val nextFiring = nextFiring ?: return false
        return !paused && nextFiring <= ZonedDateTime.now()
    }

    fun snooze() {
        nextFiring = ZonedDateTime.now().plusMinutes(10)
    }

    fun stop() {
        recalculateNextFiring()
    }

    private fun recalculateNextFiring() {
        nextFiring = null
        val now = ZonedDateTime.now()
        for (i in 0..6) {
            val targetDate = now.plusDays(i.toLong())
            val time = interval[targetDate.dayOfWeek.value] ?: continue
            val timep = LocalTime.parse(time);

            val newFiring = ZonedDateTime.of(targetDate.year, targetDate.monthValue, targetDate.dayOfMonth, timep.hour, timep.minute, 0, 0, ZoneId.of("Europe/Vienna"))

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