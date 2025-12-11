@file:JvmName("MainKt")
import kotlinx.coroutines.*
import java.io.File
import java.time.Duration

fun main(args: Array<String>) {
    val tray = TrayManager()
    val pomodoro = PomodoroTimer(
        secondAction = { minuteStr, seconStr ->
            tray.minute = minuteStr
            tray.second = seconStr
        },
        startBreak = { SoundPlayer().beginBreak(); Duration.ofMinutes(5) },
        startWork = { SoundPlayer().beginWork(); Duration.ofMinutes(30) }
    )
    val job = GlobalScope.launch {
        pomodoro.run()
    }
    tray.toggleItem.addActionListener {
        if (pomodoro.pauseRequest) {
            tray.toggleText = "Pause"
            pomodoro.pauseRequest = false
        }
        else {
            tray.toggleText = "Resume"
            pomodoro.pauseRequest = true
        }
    }

}

