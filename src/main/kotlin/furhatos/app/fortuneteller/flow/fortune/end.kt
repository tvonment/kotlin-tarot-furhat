package furhatos.app.fortuneteller.flow.fortune

import furhatos.app.fortuneteller.flow.Parent
import furhatos.app.fortuneteller.flow.main.Idle
import furhatos.flow.kotlin.State
import furhatos.flow.kotlin.furhat
import furhatos.flow.kotlin.onResponse
import furhatos.flow.kotlin.state
import furhatos.nlu.common.No
import furhatos.nlu.common.Yes

val End: State = state(Parent) {
    onEntry {
        furhat.say("Thank you for visiting the fortune teller. Have a nice day!")
        goto(Idle)
    }
}