package furhatos.app.fortuneteller.nlu

import furhatos.nlu.Intent
import furhatos.util.Language

/**
 * Define intents to match a user utterance and assign meaning to what they said.
 * Note that there are more intents available in the Asset Collection in furhat.libraries.standard.NluLib
 **/

class DoneIntent: Intent () {
    override fun getExamples(lang: Language): List<String> {
        return listOf(
            "Done",
            "I'm done",
            "I'm finished",
            "I'm ready",
            "I'm ready to go",
            "I'm ready to start",
            "I am done shuffling",
            "I am finished shuffling",
            "I am ready to go",
        )
    }
}

class HelpIntent : Intent() {
    override fun getExamples(lang: Language): List<String> {
        return listOf(
            "I need help",
            "help me please",
            "can someone help me",
            "I need assistance"
        )
    }
}

class WhatIsThisIntent : Intent() {
    override fun getExamples(lang: Language): List<String> {
        return listOf(
            "What is this",
            "what am I supposed to say",
            "what should I say",
            "I don't know what to do",
            "what am I supposed to do now",
            "should I say something",
            "what's going on",
            "what is happening here",
            "can someone tell me what is going on"
        )
    }
}