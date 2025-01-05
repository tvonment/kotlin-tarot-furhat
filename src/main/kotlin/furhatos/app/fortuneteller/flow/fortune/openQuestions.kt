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

    onEvent<AnswerReceived> {
        furhat.say(OpenQuestionState.answer)
        reentry()
    }

    onResponse<No> {
        goto(Idle)
    }

    onResponse<DoneIntent> {
        goto(Idle)
    }

    onResponse<Goodbye> {
        goto(Idle)
    }
}

class AnswerReceived(val res: Boolean) : Event()

