package net.monofraps.profiler.agent.reporter

import com.fasterxml.jackson.core.JsonFactory
import net.monofraps.profiler.agent.model.Profile
import net.monofraps.profiler.agent.writeJson
import java.nio.file.Files
import java.nio.file.Path

class FileReporter(private val reportingDir: Path) : ReporterPlugin {
    init {
        Files.createDirectories(reportingDir)
    }

    override fun report(profile: Profile) {
        val outputPath = reportingDir.resolve("profile-${System.currentTimeMillis()}.json")
        Files.newOutputStream(outputPath).use { outputStream ->
            val jsonFactory = JsonFactory()
            jsonFactory.createGenerator(outputStream).use { generator ->
                profile.writeJson(generator)
            }
        }

        println("reported profile $outputPath")
    }
}