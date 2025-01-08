package furhatos.app.fortuneteller.services

import com.google.gson.Gson
import com.google.gson.JsonObject
import furhatos.app.fortuneteller.flow.log
import furhatos.app.fortuneteller.model.*
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import java.util.concurrent.TimeUnit

interface BackendService {
    fun createSession(callback: (successful: Boolean) -> Unit)
    fun analyzeCardsByCamera(callback: (successful: Boolean) -> Unit)
    fun analyzeCardDescription(callback: (successful: Boolean) -> Unit)
    fun getFortune(callback: (successful: Boolean) -> Unit)
    fun answerOpenQuestion(question: String, callback: (successful: Boolean) -> Unit)
}

class BackendServiceImpl: BackendService {
    private val backendBaseUrl: String = "http://localhost:3000"

    /**
     * Sends a request to the backend to analyze cards by camera.
     * The session ID and card file name are included in the payload.
     * The response updates the session state with the new session data.
     */
    override fun analyzeCardsByCamera(callback: (successful: Boolean) -> Unit) {
        val payload = mapOf(
            "sessionId" to SessionState.session!!.id,
            "cardFileName" to "test-cards.png",
            //"cardFileName" to "furhat_image_${ SessionState.session!!.id }.jpg",
        )

        val gson = Gson()
        val jsonPayload = gson.toJson(payload)

        val client = OkHttpClient.Builder()
            .connectTimeout(60, TimeUnit.SECONDS)
            .writeTimeout(60, TimeUnit.SECONDS)
            .readTimeout(60, TimeUnit.SECONDS)
            .build()

        val request = Request.Builder()
            .url("$backendBaseUrl/sessions/cardsByFile")
            .header("Content-Type", "application/json")
            .patch(jsonPayload.toRequestBody("application/json".toMediaType()))
            .build()

        client.newCall(request).execute().use { response ->
            if (!response.isSuccessful) {
                log.debug("Response: ${response.body?.string()}")
                callback(false)
                return@use
            }

            val responseString = response.body?.string()
            SessionState.session = gson.fromJson(responseString, Session::class.java)
            callback(true)
        }
    }

    /**
     * Sends a request to the backend to analyze a card description.
     * The session ID, position, and conversation are included in the payload.
     * The response updates the card evaluation state with the new card data.
     */
    override fun analyzeCardDescription(callback: (successful: Boolean) -> Unit) {
        val payload = mapOf(
            "sessionId" to SessionState.session!!.id,
            "position" to CardEvaluationState.position,
            "conversation" to CardEvaluationState.conversation,
        )

        val gson = Gson()
        val jsonPayload = gson.toJson(payload)

        val client = OkHttpClient.Builder()
            .connectTimeout(60, TimeUnit.SECONDS)
            .writeTimeout(60, TimeUnit.SECONDS)
            .readTimeout(60, TimeUnit.SECONDS)
            .build()

        val request = Request.Builder()
            .url("$backendBaseUrl/sessions/cardByDescription")
            .header("Content-Type", "application/json")
            .post(jsonPayload.toRequestBody("application/json".toMediaType()))
            .build()

        client.newCall(request).execute().use { response ->
            if (!response.isSuccessful) {
                log.debug("Response: ${response.body?.string()}")
                callback(false)
                return@use
            }

            val responseString = response.body?.string()
            CardEvaluationState.card = gson.fromJson(responseString, Card::class.java)
            callback(true)
        }
    }

    /**
     * Sends a request to the backend to create a new session.
     * The topic is included in the payload.
     * The response updates the session state with the new session data.
     */
    override fun createSession(callback: (successful: Boolean) -> Unit) {

        val payload = mapOf(
            "topic" to SessionState.rawTopic
        )

        val gson = Gson()
        val jsonPayload = gson.toJson(payload)

        val client = OkHttpClient.Builder()
            .connectTimeout(60, TimeUnit.SECONDS)
            .writeTimeout(60, TimeUnit.SECONDS)
            .readTimeout(60, TimeUnit.SECONDS)
            .build()

        val request = Request.Builder()
            .url("$backendBaseUrl/sessions")
            .header("Content-Type", "application/json")
            .post(jsonPayload.toRequestBody("application/json".toMediaType()))
            .build()

        client.newCall(request).execute().use { response ->
            if (!response.isSuccessful) {
                log.debug("Response: ${response.body?.string()}")
                callback(false)
                return@use
            }

            val responseString = response.body?.string()
            val responseJson = gson.fromJson(responseString, JsonObject::class.java)
            SessionState.session = Session( responseJson.get("id").asString, responseJson.get("topic").asString, mutableListOf(), mutableListOf(), "")
            callback(true)
        }
    }

    /**
     * Sends a request to the backend to get a fortune.
     * The session ID is included in the payload.
     * The response updates the session state with the new session data.
     */
    override fun getFortune(callback: (successful: Boolean) -> Unit) {
        val payload = mapOf(
            "sessionId" to SessionState.session!!.id,
        )

        val gson = Gson()
        val jsonPayload = gson.toJson(payload)

        val client = OkHttpClient.Builder()
            .connectTimeout(60, TimeUnit.SECONDS)
            .writeTimeout(60, TimeUnit.SECONDS)
            .readTimeout(60, TimeUnit.SECONDS)
            .build()

        val request = Request.Builder()
            .url("$backendBaseUrl/sessions/fortune")
            .header("Content-Type", "application/json")
            .patch(jsonPayload.toRequestBody("application/json".toMediaType()))
            .build()

        client.newCall(request).execute().use { response ->
            if (!response.isSuccessful) {
                log.debug("Response: ${response.body?.string()}")
                callback(false)
                return@use
            }

            val responseString = response.body?.string()
            SessionState.session = gson.fromJson(responseString, Session::class.java)
            callback(true)
        }
    }

    /**
     * Sends a request to the backend to answer an open question.
     * The session ID and question are included in the payload.
     * The response updates the open question state with the answer.
     */
    override fun answerOpenQuestion(question: String, callback: (successful: Boolean) -> Unit) {
        val payload = mapOf(
            "sessionId" to SessionState.session!!.id,
            "question" to question,
        )

        val gson = Gson()
        val jsonPayload = gson.toJson(payload)

        val client = OkHttpClient.Builder()
            .connectTimeout(60, TimeUnit.SECONDS)
            .writeTimeout(60, TimeUnit.SECONDS)
            .readTimeout(60, TimeUnit.SECONDS)
            .build()

        val request = Request.Builder()
            .url("$backendBaseUrl/sessions/openQuestion")
            .header("Content-Type", "application/json")
            .patch(jsonPayload.toRequestBody("application/json".toMediaType()))
            .build()

        client.newCall(request).execute().use { response ->
            if (!response.isSuccessful) {
                log.debug("Response: ${response.body?.string()}")
                callback(false)
                return@use
            }

            val responseString = response.body?.string()
            val responseJson = gson.fromJson(responseString, JsonObject::class.java)
            OpenQuestionState.answer = responseJson.get("answer").asString
            callback(true)
        }
    }
}