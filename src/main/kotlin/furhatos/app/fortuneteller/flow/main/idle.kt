package furhatos.app.fortuneteller.flow.main

import furhat.libraries.standard.BehaviorLib.AutomaticMovements.randomHeadMovements
import furhatos.app.fortuneteller.flow.Global
import furhatos.app.fortuneteller.flow.fortune.Start
import furhatos.app.fortuneteller.flow.log
import furhatos.app.fortuneteller.setting.*
import furhatos.flow.kotlin.*

/**
 * Tip!
 *
 * Use simple Idle state where the interaction will start as soon as a user enters for situations where you know
 * people want to be interacting with the robot. Or define a more complex idle state like "WaitingForEngagedUser"
 * to avoid starting the interaction without the user actually being interested to engage with the user.
 */

/** State where Furhat is inactive with no users in front of it */
val Idle: State = state(parent = Global) {
    include(randomHeadMovements())
    include(napWhenTired) // Sends the robot to "sleep" after a period of inactivity

    init { // init is only performed once
        if (furhat.isVirtual()) furhat.say("Add a virtual user to start the interaction")
    }

    onEntry { // on entry performed every time entering the state
        furhat.beIdle() // custom function to set the robot in a specific 'mode'
        log.info("idling")
    }

    onUserEnter {
        furhat.attend(it) // It's good practice to make attention shifts on the trigger, instead of shifting the attention onEntry in the state.
        goto(Start)
    }
}
