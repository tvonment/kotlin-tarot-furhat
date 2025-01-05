package furhatos.app.fortuneteller.model

import furhatos.gestures.Gesture
import furhatos.gestures.Gestures

data class Fortune (
    val content: String,
    val card: String,
    val gesture: String,
) {
    fun getGesture(): Gesture {
        return when (gesture) {
            "Gestures.Smile" -> Gestures.Smile
            "Gestures.BigSmile" -> Gestures.BigSmile
            "Gestures.ExpressFear" -> Gestures.ExpressFear
            "Gestures.Wink" -> Gestures.Wink
            "Gestures.Nod" -> Gestures.Nod
            "Gestures.Shake" -> Gestures.Shake
            "Gestures.Surprise" -> Gestures.Surprise
            "Gestures.BrowRaise" -> Gestures.BrowRaise
            "Gestures.BrowFrown" -> Gestures.BrowFrown
            "Gestures.Thoughtful" -> Gestures.Thoughtful
            "Gestures.CloseEyes" -> Gestures.CloseEyes
            "Gestures.OpenEyes" -> Gestures.OpenEyes
            "Gestures.Blink" -> Gestures.Blink
            else -> Gestures.Smile
        }
    }
}