package net.monofraps.profiler.agent.model

import java.time.OffsetDateTime
import java.util.*

class Profile(val application: UUID, val startTime: OffsetDateTime = OffsetDateTime.now()) {
    var endTime = startTime
    val root: CallGraph = CallGraph(0)
    val frameEncodings: FrameEncodings = FrameEncodings()
    var sampleCount = 0

    fun recordSample() {
        sampleCount++
    }

    fun capture(state: Thread.State, isNative: Boolean, stacktrace: Array<StackTraceElement>) {
        var node = root
        stacktrace.reversedArray().forEach { stackTraceElement ->
            val frame = frameEncodings.encode(stackTraceElement.className, stackTraceElement.methodName)
            node = node.addChild(frame)
        }
        node.addCounts(encodeState(state), 1)
    }

    fun finish() {
        endTime = OffsetDateTime.now()
    }

    private fun encodeState(state: Thread.State): Int {
        return state.ordinal
    }
}