package at.appfractory.medibot.medibot.bot.response

import at.appfractory.medibot.medibot.bot.BotResponsePayload

/**
 * Created by Kai Takac on 2019-06-21.
 */
class ChoiceResponsePayload(val text: String, val choices: Map<String, String>) : BotResponsePayload