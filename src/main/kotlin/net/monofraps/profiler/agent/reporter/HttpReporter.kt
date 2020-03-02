package net.monofraps.profiler.agent.reporter

import com.fasterxml.jackson.core.JsonFactory
import com.fasterxml.jackson.core.JsonGenerator
import net.monofraps.profiler.agent.model.CallGraph
import net.monofraps.profiler.agent.model.Profile
import net.monofraps.profiler.agent.writeJson
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import okio.BufferedSink
import java.net.HttpURLConnection
import java.net.URL
import java.util.logging.Level
import java.util.logging.LogManager

class HttpReporter(private val address: String) : ReporterPlugin {
    private val logger = LogManager.getLogManager().getLogger(HttpReporter::class.java.name)

    override fun report(profile: Profile) {
        println("xxx")

        val connection = URL("$address/profile").openConnection() as HttpURLConnection
        connection.requestMethod = "PUT"
        connection.addRequestProperty("Content-Type", "application/json")
        connection.doOutput = true

        val jsonFactory = JsonFactory()
        jsonFactory.createGenerator(connection.outputStream).use { generator ->
            profile.writeJson(generator)
        }

        val responseCode = connection.responseCode
        println(responseCode)
        if (!response.isSuccessful) {
            logger.log(Level.WARNING, "Failed to post performance profile: ${response.code} - ${response.body?.string()}")
        }
    }
}