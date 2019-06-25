package at.appfractory.medibot.medibot.model

import javax.persistence.Entity
import javax.persistence.Id

/**
 * Created by Kai Takac on 2019-06-22.
 */
@Entity
data class Chat(@Id var chatId: String, var state: ChatState, var statePayload: String?) {

    constructor() : this("", ChatState.Normal, null) {
    }
}