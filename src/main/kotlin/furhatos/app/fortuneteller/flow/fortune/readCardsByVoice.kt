package furhatos.app.fortuneteller.flow.fortune

import furhatos.app.fortuneteller.flow.Parent
import furhatos.app.fortuneteller.flow.log
import furhatos.app.fortuneteller.model.CardEvaluationState
import furhatos.app.fortuneteller.model.ConversationItem
import furhatos.app.fortuneteller.model.SessionState
import furhatos.app.fortuneteller.services.BackendServiceImpl
import furhatos.event.Event
import furhatos.flow.kotlin.State
import furhatos.flow.kotlin.onResponse
import furhatos.flow.kotlin.state
import furhatos.flow.kotlin.*

val ReadCardsByVoice: State = state(Parent) {
    onEntry {
        val count = SessionState.session!!.cards.size
        if (count == 10) {
            furhat.say("We have now 10 cards. Let's start reading them.")
            goto(ReadFortune)
        } else {
            val index = count + 1
            log.debug("Reinitialize Index: $index")
            var cardIndex = ""
            if (index == 1) {
                cardIndex = "first"
            } else if (index == 2) {
                cardIndex = "second"
            } else if (index == 3) {
                cardIndex = "third"
            } else if (index == 4) {
                cardIndex = "fourth"
            } else if (index == 5) {
                cardIndex = "fifth"
            } else if (index == 6) {
                cardIndex = "sixth"
            } else if (index == 7) {
                cardIndex = "seventh"
            } else if (index == 8) {
                cardIndex = "eighth"
            } else if (index == 9) {
                cardIndex = "ninth"
            } else if (index == 10) {
                cardIndex = "tenth"
            } else {
                cardIndex = "$index"
            }
            CardEvaluationState.clear("Please describe what you can see on the $cardIndex card?", index)
            goto(DescribeCard)
        }
    }
}

val DescribeCard: State = state(Parent) {
    val backendService = BackendServiceImpl()

    onEntry {
        val question = CardEvaluationState.openQuestions.first()
        furhat.ask(question)
    }

    onResponse {
        val description = it.text
        CardEvaluationState.conversation.add(ConversationItem(CardEvaluationState.openQuestions.first(), description))
        CardEvaluationState.openQuestions.removeFirst()
        if (CardEvaluationState.openQuestions.isEmpty()) {
            backendService.analyzeCardDescription { response: Boolean ->
                if (response) {
                    furhat.run {
                        raise(CardAnalyzed(true))
                    }
                } else {
                    furhat.say("Sorry something went wrong.")
                }
            }
        } else {
            reentry()
        }
    }

    onEvent<CardAnalyzed> {
        val card = CardEvaluationState.card
        if (card?.position == 0) {
            val newQuestions = card.description.split("?").filter { it.isNotBlank() }
            CardEvaluationState.openQuestions.addAll(newQuestions)
            reentry()
        } else {
            furhat.say("The card is ${card?.name}.")
            SessionState.session!!.cards.add(card!!)
            goto(ReadCardsByVoice)
        }
    }
}

class CardAnalyzed(val res: Boolean) : Event()
