package net.monofraps.profiler.agent.model

class FrameEncodings{
    val reverseEncodings:MutableList<FrameEncoding> = ArrayList()
    val encodings: MutableMap<String, MutableMap<String, Int>> = HashMap()
    private var nextId = 1

    fun encode(className: String, methodName: String): Int {
        val classEncodings = encodings.computeIfAbsent(className) { HashMap() }
        return classEncodings.computeIfAbsent(methodName) {
            val encoding = nextId++
            reverseEncodings.add(FrameEncoding(className, methodName))
            encoding
        }
    }
}