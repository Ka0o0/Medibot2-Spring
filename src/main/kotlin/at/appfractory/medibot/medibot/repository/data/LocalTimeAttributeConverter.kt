package at.appfractory.medibot.medibot.repository.data

import java.time.LocalTime
import java.time.format.DateTimeFormatter
import javax.persistence.AttributeConverter
import javax.persistence.Converter

/**
 * Created by Kai Takac on 2019-06-25.
 */

@Converter(autoApply = true)
class LocalTimeAttributeConverter : AttributeConverter<LocalTime, String> {

    private val formatter = DateTimeFormatter.ISO_LOCAL_TIME

    override fun convertToDatabaseColumn(attribute: LocalTime?): String {
        return attribute?.format(formatter) ?: ""
    }

    override fun convertToEntityAttribute(dbData: String?): LocalTime {
        val timeString = dbData ?: return LocalTime.now()
        return LocalTime.parse(timeString, formatter)
    }
}