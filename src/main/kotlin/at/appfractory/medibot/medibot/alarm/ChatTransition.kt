package at.appfractory.medibot.medibot.alarm

enum class ChatTransition {
    CREATE_ALARM_COMMAND, CONTINUE_ALARM_COMMAND, PAUSE_ALARM_COMMAND, DELETE_ALARM_COMMAND, TEXT_ENTERED, STOP_ALARM_COMMAND,

    LIST_ALARMS_COMMAND,

    HELP_COMMAND
}
