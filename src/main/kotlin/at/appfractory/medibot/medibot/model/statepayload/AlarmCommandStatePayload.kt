package at.appfractory.medibot.medibot.model.statepayload

import at.appfractory.medibot.medibot.model.ChatStatePayload

/**
 * Created by Kai Takac on 2019-06-22.
 */
class AlarmCommandStatePayload(val command: AlarmCommand) : ChatStatePayload {
    enum class AlarmCommand {
        Continue, Pause, Delete
    }
}