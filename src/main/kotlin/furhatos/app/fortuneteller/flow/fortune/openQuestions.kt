package furhatos.app.fortuneteller.flow.fortune

import furhatos.app.fortuneteller.flow.Parent
import furhatos.app.fortuneteller.flow.main.Idle
import furhatos.app.fortuneteller.model.OpenQuestionState
import furhatos.app.fortuneteller.model.SessionState
import furhatos.app.fortuneteller.nlu.DoneIntent
import furhatos.app.fortuneteller.services.BackendServiceImpl
import furhatos.event.Event
import furhatos.flow.kotlin.*
import furhatos.nlu.common.Goodbye
import furhatos.nlu.common.No

val OpenQuestions: State = state(Parent) {

    val backendService = BackendServiceImpl()

    onEntry {
        furhat.ask("Do you have any questions?")
    }

    onResponse {
        if (it.intent is DoneIntent || it.intent is Goodbye || it.intent is No) {
            goto(End)
        } else {
            val question = it.text
            backendService.answerOpenQuestion(question) { response: Boolean ->
                if (response) {
                    furhat.run {
                        raise(AnswerReceived(true))
                    }
                } else {
                    furhat.say("Sorry something went wrong.")
                }
            }
        }
    }

    onEvent<AnswerReceived> {
        furhat.say(OpenQuestionState.answer)
        reentry()
    }

    onResponse<No> {
        goto(End)
    }

    onResponse<DoneIntent> {
        goto(End)
    }

    onResponse<Goodbye> {
        goto(End)
    }
}

class AnswerReceived(val res: Boolean) : Event()

