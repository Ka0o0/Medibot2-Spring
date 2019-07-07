package at.appfractory.medibot.medibot.model

import java.time.LocalDateTime
import java.time.LocalTime
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
        var nextFiring: LocalDateTime?,
        var paused: Boolean
) {

    constructor() : this(null, "", "", emptyMap(), null, false)

    fun shouldRing(): Boolean {
        val nextFiring = nextFiring ?: return false
        return !paused && nextFiring <= LocalDateTime.now()
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
            val time = interval[targetDate.dayOfWeek.value] ?: continue
            val newFiring = LocalDateTime.of(targetDate.toLocalDate(), LocalTime.parse(time))

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