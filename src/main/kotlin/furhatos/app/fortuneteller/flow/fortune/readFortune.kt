package furhatos.app.fortuneteller.flow.fortune

import furhatos.app.fortuneteller.flow.Parent
import furhatos.app.fortuneteller.flow.log
import furhatos.app.fortuneteller.model.SessionState
import furhatos.app.fortuneteller.services.BackendServiceImpl
import furhatos.event.Event
import furhatos.flow.kotlin.State
import furhatos.flow.kotlin.state
import furhatos.flow.kotlin.*

val ReadFortune: State = state(Parent) {

    val backendService = BackendServiceImpl()

    onEntry {
        furhat.say("Alright let's see what the cards have to say.")

        backendService.getFortune { response: Boolean ->
            if (response) {
                furhat.run {
                    raise(FortuneReceived(true))
                }
            } else {
                furhat.say("Sorry something went wrong.")
            }
        }
    }

    onEvent<FortuneReceived> {
        log.debug("ID Session: {}", SessionState.session!!.id)
        log.debug("Topic Session: {}", SessionState.session!!.topic)

        val fortune = SessionState.session!!.fortune
        for (f in fortune) {
            log.debug("Fortune: {}, {}", f.content, f.gesture)
            furhat.gesture(f.getGesture())
            furhat.say(f.content)
        }
        goto(OpenQuestions)
    }
}

class FortuneReceived(val res: Boolean) : Event()