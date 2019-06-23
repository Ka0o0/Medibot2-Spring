package at.appfractory.medibot.medibot

import at.appfractory.medibot.medibot.alarm.AlarmService
import at.appfractory.medibot.medibot.telegram.TelegramMedibot
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.telegram.telegrambots.ApiContextInitializer
import org.telegram.telegrambots.meta.TelegramBotsApi
import org.telegram.telegrambots.meta.exceptions.TelegramApiException


@SpringBootApplication
class MedibotApplication : CommandLineRunner {

    @Autowired
    lateinit var telegramBot: TelegramMedibot
    @Autowired
    lateinit var alarmService: AlarmService

    override fun run(vararg args: String?) {
        val botsApi = TelegramBotsApi()
        try {
            botsApi.registerBot(telegramBot)
            alarmService.observers.add(telegramBot)
            alarmService.start()
        } catch (e: TelegramApiException) {
            e.printStackTrace()
        }
    }
}

fun main(args: Array<String>) {
    ApiContextInitializer.init()

    runApplication<MedibotApplication>(*args)
}
