import java.time.Duration
import java.util.concurrent.TimeUnit

/**
 * Managed logic
 */
class PomodoroTimer constructor(
    private var timeLeft: Duration = Duration.ofMinutes(30),
    private var isBreak: Boolean = false,
    var secondAction: (String, String) -> Unit = {ok, dude -> println(ok + dude)},
    var startBreak: () -> Duration = {Duration.ZERO},
    var startWork: () -> Duration = {Duration.ZERO}
) {
    var pauseRequest: Boolean = false
    set(value) {
        if(value) {
            pauseRecover = value
        }
        field = value
    }
    private var pauseRecover: Boolean = false
    private val minuteStr: String
        get() {
        return timeLeft.toMinutes().toString() + if(isBreak) "b" else "w"
    }
    private val secondStr: String
        get() {
            return timeLeft.toSecondsPart().toString() + if(isBreak) "b" else "w"
        }
    fun run() {
        while (timeLeft != Duration.ZERO && !pauseRequest) {
            secondAction(minuteStr, secondStr)
            TimeUnit.SECONDS.sleep(1)
            timeLeft = timeLeft.minus(Duration.ofSeconds(1))
            println(timeLeft.toSeconds().toString())
        }
        secondAction(minuteStr, secondStr)
        TimeUnit.SECONDS.sleep(1)

        if(!pauseRecover) {
            isBreak = !isBreak
        }
        if(!pauseRequest) {
            if (isBreak && !pauseRecover) {
                timeLeft = startBreak()
//                isBreak = !isBreak
            }
            else if(!isBreak && !pauseRecover) {
                timeLeft = startWork()
//                isBreak = !isBreak
            }
            pauseRecover = false
        }

        if(timeLeft != Duration.ZERO) {
            run()
        }
        else {
            System.exit(1)
        }

    }

}