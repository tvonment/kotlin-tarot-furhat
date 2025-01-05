package furhatos.app.fortuneteller.flow

import furhatos.app.fortuneteller.flow.main.*
import furhatos.app.fortuneteller.setting.*
import furhatos.flow.kotlin.Color
import furhatos.flow.kotlin.Section
import furhatos.flow.kotlin.furhat
import furhatos.flow.kotlin.partialState

/** Universal button ta always be present **/
val UniversalWizardButtons = partialState {
    onButton("restart", color = Color.Red, section = Section.LEFT) {
        goto(Init)
    }
    onButton("Stop speaking", color = Color.Red, section = Section.LEFT) {
        furhat.stopSpeaking()
    }
}

/** Buttons to speed up testing **/
val TestButtons = partialState {
    onButton("Idle", color = Color.Blue, section = Section.RIGHT) {
        goto(Idle)
    }
    onButton("Nap", color = Color.Blue, section = Section.RIGHT) {
        goto(Nap)
    }
    onButton("DeepSleep", color = Color.Blue, section = Section.RIGHT) {
        goto(DeepSleep)
    }

    onButton("WakeUp", color = Color.Yellow, section = Section.RIGHT) {
        furhat.wakeUp()
    }
    onButton("WakeUp", color = Color.Yellow, section = Section.RIGHT) {
        furhat.fallASleep()
    }
    onButton("WakeUp", color = Color.Yellow, section = Section.RIGHT) {
        furhat.beIdle()
    }
    onButton("WakeUp", color = Color.Yellow, section = Section.RIGHT) {
        furhat.beActive()
    }

    onButton("set furhat persona", color = Color.Yellow, section = Section.RIGHT) {
        activate(furhatPersona)
    }
}