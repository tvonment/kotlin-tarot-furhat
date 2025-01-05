package furhatos.app.fortuneteller.flow

import furhat.libraries.standard.BehaviorLib.AutomaticMovements.randomHeadMovements
import furhatos.app.fortuneteller.flow.fortune.Start
import furhatos.app.fortuneteller.flow.main.Idle
import furhatos.app.fortuneteller.nlu.UniversalResponses
import furhatos.app.fortuneteller.setting.UniversalFallbackBehaviour
import furhatos.app.fortuneteller.setting.nextMostEngagedUser
import furhatos.app.fortuneteller.setting.wakeUp
import furhatos.flow.kotlin.*
import java.util.concurrent.TimeUnit

/**
 * Parent state that house global definitions that should always be active in any state in the main flow
 */
val Global: State = state {
    include(UniversalWizardButtons) // Wizard buttons that should be available in all states
    include(UniversalResponses) // Commands that uses should always be able to say.
    include(UniversalFallbackBehaviour) //Set default fallback behaviour
    if (testMode) include(TestButtons)// Wizard buttons for easy testing
}

/**
 * Additional more specific parent state.
 * Use for all states where the robot is being actively engaged in the interaction.
 */
val Parent: State = state(parent = Global) {
    // Random head movements can affect how gestures that include neck movements are carried out.
    // Either choose to not include random head movements at all
    // Or you can turn them on/off using enableVariableHeadMovements = false before performing the gesture
    include(randomHeadMovements())

    // Global users enter/leave behavior - override in the child state to set specific behaviour for the specific part of the interaction
    onUserEnter(instant = true) {
        when { // "it" is the user that entered
            furhat.isAttendingUser -> furhat.glance(it) // Glance at new users entering
            !furhat.isAttendingUser -> furhat.attend(it) // Attend user if not attending anyone
        }
        reentry()
    }

    onUserLeave(instant = true) {
        when { // "it" is the user that left
            !users.hasAny() -> goto(Idle) // no more users
            furhat.isAttending(it) -> furhat.attend(users.nextMostEngagedUser()) // current user left
            else -> furhat.glance(it.head.location) // other user left, just glance
        }
        reentry()
    }

    onExit(inherit = true) {
        // Reset local counters
        noMatch = 0
        noInput = 0
    }
}

/** Sleeping - use for states where the robot is in power save mode and can only be woken up by wizard buttons */
val PowerSaving: State = state(parent = Global) {
    onEntry(inherit = true) {
        delay(5, TimeUnit.SECONDS) //stay asleep for at least 5 seconds before being able to wake up again
    }
    onButton("Wake Up") {
        furhat.wakeUp()
        if (users.hasAny()) goto(Start) else goto(Idle)
    }
}