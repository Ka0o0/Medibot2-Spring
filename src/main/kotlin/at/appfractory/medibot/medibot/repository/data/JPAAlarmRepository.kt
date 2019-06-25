package at.appfractory.medibot.medibot.repository.data

import at.appfractory.medibot.medibot.model.Alarm
import at.appfractory.medibot.medibot.repository.AlarmRepository
import org.springframework.data.repository.CrudRepository

/**
 * Created by Kai Takac on 2019-06-25.
 */
interface JPAAlarmRepository: CrudRepository<Alarm, Long>, AlarmRepository {
}