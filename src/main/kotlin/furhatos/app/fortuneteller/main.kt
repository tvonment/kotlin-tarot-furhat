package furhatos.app.fortuneteller

import furhatos.app.fortuneteller.flow.Init
import furhatos.skills.Skill
import furhatos.flow.kotlin.*

class TarotFortuneSkill : Skill() {
    override fun start() {
        Flow().run(Init)
    }
}

fun main(args: Array<String>) {
    Skill.main(args)
}