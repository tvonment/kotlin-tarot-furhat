package furhatos.app.fortuneteller.model

object CardEvaluationState {
    var card: Card? = null
    var position: Int = 0
    var openQuestions: MutableList<String> = mutableListOf()
    var conversation: MutableList<ConversationItem> = mutableListOf()

    fun clear(initialQuestion: String, cardPosition: Int) {
        card = null
        position = cardPosition
        openQuestions.clear()
        openQuestions.add(initialQuestion)
        conversation.clear()
    }
}