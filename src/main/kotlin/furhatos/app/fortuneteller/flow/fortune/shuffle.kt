package furhatos.app.fortuneteller.flow.fortune


import furhatos.flow.kotlin.State
import furhatos.flow.kotlin.onResponse
import furhatos.flow.kotlin.state
import furhatos.app.fortuneteller.flow.Parent
import furhatos.app.fortuneteller.model.PlayMode
import furhatos.app.fortuneteller.nlu.DoneIntent
import furhatos.app.fortuneteller.model.SessionState
import furhatos.flow.kotlin.*


val Shuffle: State = state(Parent) {
    onEntry {
        furhat.say("Please shuffle the cards in front of you and think of ${SessionState.session!!.topic} while doing that. Tell me when you are ready.")
        furhat.listen()
    }
    onResponse<DoneIntent> {
        furhat.say("Great! Now select ten cards from the staple and place them in front of you. The cards you choose will be the ones I read. Be careful, the order is important.")
        if (SessionState.playMode == PlayMode.Voice) {
            goto(ReadCardsByVoice)
        } else {
            goto(ReadCardsByCamera)
        }
    }

    onNoResponse {
        furhat.say("I'm sorry, I didn't catch that. Please shuffle the cards.")
        furhat.listen()
    }
}
