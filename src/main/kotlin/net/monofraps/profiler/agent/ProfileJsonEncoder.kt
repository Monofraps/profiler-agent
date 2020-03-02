package net.monofraps.profiler.agent

import com.fasterxml.jackson.core.JsonGenerator
import net.monofraps.profiler.agent.model.CallGraph
import net.monofraps.profiler.agent.model.Profile
import java.util.*

fun Profile.writeJson(generator: JsonGenerator) {
    generator.writeStartObject()

    generator.writeFieldName("callGraph")
    root.writeJson(generator)

    generator.writeFieldName("metadata")
    generator.writeStartObject()

    generator.writeStringField("applicationId", application.toString())
    generator.writeNumberField("startTimeUtc", startTime.toEpochSecond())
    generator.writeNumberField("endTimeUtc", endTime.toEpochSecond())
    generator.writeNumberField("sampleCount", sampleCount)

    generator.writeEndObject()

    generator.writeFieldName("frameEncodings")
    generator.writeStartArray()
    frameEncodings.reverseEncodings.forEach { entry ->
        generator.writeStartObject()

        generator.writeStringField("class", entry.className)
        generator.writeStringField("method", entry.methodName)

        generator.writeEndObject()
    }
    generator.writeEndArray()

    generator.writeEndObject()
}

fun CallGraph.writeJson(generator: JsonGenerator) {
    generator.writeStartObject()

    generator.writeNumberField("frame", frame)

    if (hasCounts) {
        generator.writeFieldName("counts")
        generator.writeStartObject()

        generator.writeNumberField("runnable", counts[Thread.State.RUNNABLE.ordinal])
        generator.writeNumberField("blocked", counts[Thread.State.BLOCKED.ordinal])
        generator.writeNumberField("waiting", counts[Thread.State.WAITING.ordinal] + counts[Thread.State.TIMED_WAITING.ordinal])

        generator.writeEndObject()
    }

    if (children.isNotEmpty()) {
        generator.writeFieldName("children")
        generator.writeStartArray()
        children.forEach { child -> child.writeJson(generator) }
        generator.writeEndArray()
    }

    generator.writeEndObject()
}
