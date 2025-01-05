package furhatos.app.fortuneteller.model

data class Session (
    var id: String,
    var topic: String,
    var cards: MutableList<Card>,
    var fortune: MutableList<Fortune>,
)