package furhatos.app.fortuneteller.flow.fortune

import furhatos.app.fortuneteller.flow.Parent
import furhatos.app.fortuneteller.flow.log
import furhatos.app.fortuneteller.services.BackendServiceImpl
import furhatos.app.fortuneteller.model.SessionState
import furhatos.event.Event
import furhatos.flow.kotlin.State
import furhatos.flow.kotlin.onResponse
import furhatos.flow.kotlin.state
import furhatos.flow.kotlin.*

val SetTopic: State = state(Parent) {

    val backendService = BackendServiceImpl()

    onEntry {
        furhat.say("What question would you like to ask the cards?")
        furhat.listen()
    }

    onResponse {
        SessionState.rawTopic = it.text
        log.debug("Topic Session: ${SessionState.rawTopic}")

        backendService.createSession { response: Boolean ->
            if (response) {
                furhat.run {
                    raise(SessionCreated(true))
                }
            } else {
                furhat.say("Sorry something went wrong.")
            }
        }
    }

    onEvent<SessionCreated> {
        if (it.res) {
            val topic: String = SessionState.session!!.topic
            furhat.say("Alright. Let's explore the Topic: $topic")
            goto(Shuffle)
        } else {
            furhat.say("Sorry something went wrong.")
        }
    }
}

class SessionCreated(val res: Boolean) : Event()
