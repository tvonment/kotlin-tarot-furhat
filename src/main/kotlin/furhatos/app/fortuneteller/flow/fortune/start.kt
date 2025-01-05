package furhatos.app.fortuneteller.flow.fortune

import furhatos.app.fortuneteller.flow.Parent
import furhatos.app.fortuneteller.flow.main.Idle
import furhatos.flow.kotlin.State
import furhatos.flow.kotlin.onResponse
import furhatos.flow.kotlin.state
import furhatos.nlu.common.No
import furhatos.nlu.common.Yes
import furhatos.flow.kotlin.*


val Start: State = state(Parent) {
    onEntry {
        furhat.say("Hello, I am the fortune teller. I can read your fortune. Would you like me to read your fortune?")
        furhat.listen(timeout = 15000)
    }

    onResponse<Yes> {
        goto(SetTopic)
    }

    onResponse<No> {
        furhat.say("Okay, have a nice day!")
        goto(Idle)
    }

    onResponse {
        furhat.say("I'm sorry, I didn't understand that. Would you like me to read your fortune?")
        furhat.listen(timeout = 15000)
    }
}