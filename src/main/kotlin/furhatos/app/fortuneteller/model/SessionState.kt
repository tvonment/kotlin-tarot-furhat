package furhatos.app.fortuneteller.model

object SessionState {
    var session: Session? = null
    var rawTopic: String = ""
    var playMode: PlayMode = PlayMode.Voice
}