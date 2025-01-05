package furhatos.app.fortuneteller.flow.fortune

import furhatos.app.fortuneteller.flow.Parent
import furhatos.app.fortuneteller.flow.log
import furhatos.app.fortuneteller.model.CardEvaluationState
import furhatos.app.fortuneteller.model.SessionState
import furhatos.app.fortuneteller.nlu.DoneIntent
import furhatos.app.fortuneteller.services.BackendServiceImpl
import furhatos.event.Event
import furhatos.flow.kotlin.State
import furhatos.flow.kotlin.furhat
import furhatos.flow.kotlin.onResponse
import furhatos.flow.kotlin.state
import java.io.File
import java.io.IOException

val ReadCardsByCamera: State = state(Parent) {

    val projectFolder = System.getenv("SAS_TOKEN") ?: "default"
    val shellScript = "${projectFolder}/src/main/kotlin/furhatos/app/fortuneteller/camera/capture_image.sh"
    val storageAccountName = System.getenv("STORAGE_ACCOUNT") ?: "default"
    val storageContainerName = "card-images"
    val storageSasToken = System.getenv("SAS_TOKEN") ?: "default"

    val backendService = BackendServiceImpl()

    onEntry {
        furhat.say("Please lay out the cards in the celtic cross spread in front of you. When you are ready, tell me.")
        furhat.listen()
    }

    onResponse<DoneIntent> {
        furhat.say("Great! I will now read the cards.")
        val id = SessionState.session!!.id
        val exitCode = executeShellScript(shellScript, id, storageAccountName, storageContainerName, storageSasToken)
        log.debug("Script Exit code: $exitCode")
        furhat.say("I have scanned the cards with exit code $exitCode")

        if (exitCode == 0) {
            backendService.analyzeCardsByCamera { response: Boolean ->
                if (response) {
                    furhat.run {
                        raise(CardsAnalyzed(true))
                    }
                } else {
                    furhat.say("Sorry, I couldn't read the cards.")
                }
            }
        } else {
            furhat.say("Sorry, I couldn't read the cards.")
        }
    }

    onEvent<CardsAnalyzed> {
        if (it.res) {
            goto(ReadFortune)
        } else {
            furhat.say("Sorry, something went wrong.")
        }
    }
}

private fun executeShellScript(scriptPath: String, id: String, storageAccountName: String, storageContainerName: String, storageSasToken: String): Int {
    try {
        val command = arrayOf(scriptPath, id, storageAccountName, storageContainerName, storageSasToken)
        val processBuilder = ProcessBuilder(*command)
        processBuilder.directory(File(scriptPath).parentFile) // Set the working directory
        processBuilder.redirectOutput(ProcessBuilder.Redirect.INHERIT)
        processBuilder.redirectError(ProcessBuilder.Redirect.INHERIT)

        val process = processBuilder.start() // Start the process
        return process.waitFor() // Wait for the process to complete
    } catch (e: IOException) {
        e.printStackTrace()
        return -1
    } catch (e: InterruptedException) {
        e.printStackTrace()
        return -1
    }
}

class CardsAnalyzed(val res: Boolean) : Event()
