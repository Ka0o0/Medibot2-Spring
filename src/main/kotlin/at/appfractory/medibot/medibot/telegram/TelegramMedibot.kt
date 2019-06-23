package at.appfractory.medibot.medibot.telegram

import at.appfractory.medibot.medibot.alarm.AlarmService
import at.appfractory.medibot.medibot.bot.BotResponse
import at.appfractory.medibot.medibot.bot.BotResponsePayload
import at.appfractory.medibot.medibot.bot.MedibotMessageHandler
import at.appfractory.medibot.medibot.bot.response.ChoiceResponse
import at.appfractory.medibot.medibot.bot.response.ChoiceResponsePayload
import at.appfractory.medibot.medibot.bot.response.TextResponse
import at.appfractory.medibot.medibot.bot.response.TextResponsePayload
import at.appfractory.medibot.medibot.model.Alarm
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.telegram.telegrambots.bots.TelegramLongPollingBot
import org.telegram.telegrambots.meta.api.methods.send.SendMessage
import org.telegram.telegrambots.meta.api.objects.Message
import org.telegram.telegrambots.meta.api.objects.Update

@Service
class TelegramMedibot(val messageHandler: MedibotMessageHandler) : TelegramLongPollingBot(), AlarmService.AlarmServiceObserver {

    override fun alarmDidFire(alarm: Alarm) {
        val message = SendMessage(alarm.chatId, "Take your " + alarm.name)
        execute(message)
    }

    override fun getBotUsername(): String {
        return "medibot2_bot"
    }

    override fun getBotToken(): String {
        return System.getenv("API_KEY")
    }

    override fun onUpdateReceived(update: Update?) {
        if (update == null) return
        if (!update.hasMessage()) return

        val chatId = update.message.chatId
        val text = update.message.text

        val response = messageHandler.handleMessageFromChat(text, chatId.toString())
        processResponseForMessage(response, update.message)
    }

    private fun processResponseForMessage(response: BotResponse, message: Message) {
        when (response::class) {
            ChoiceResponse::class -> sendChoiceResponse(response.payload as ChoiceResponsePayload, message)
            TextResponse::class -> sendTextResponse(response.payload as TextResponsePayload, message)
        }
    }

    private fun sendChoiceResponse(payload: ChoiceResponsePayload, message: Message) {
        execute(SendMessage(message.chatId, payload.text))

        var text = "Please type one of the following:\n"
        payload.choices.forEach {
            text += it.key + " for " + it.value + "\n"
        }
        execute(SendMessage(message.chatId, text))

    }

    private fun sendTextResponse(payload: TextResponsePayload, message: Message) {
        val message = SendMessage(message.chatId, payload.text)
        execute(message)
    }
}

