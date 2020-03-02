package net.monofraps.profiler.agent

import net.monofraps.profiler.agent.model.Profile
import net.monofraps.profiler.agent.reporter.ReporterPlugin
import java.lang.management.ManagementFactory
import java.security.AccessController
import java.security.PrivilegedAction
import java.time.Duration
import java.time.Instant
import java.util.*

class ProfilingCommand(
        private val application: UUID,
        private val reporter: ReporterPlugin,
        private val reportingInterval: Duration
) {
    private var lastReportTime = Instant.now()
    private var profile = Profile(application)

    fun profile() {
        try {
            sample()
            flush()
        } catch (t: Throwable) {
            println(t.message)
        }
    }

    private fun sample() {
        val threadIds = ManagementFactory.getThreadMXBean().allThreadIds
        val threadInfos = ManagementFactory.getThreadMXBean().getThreadInfo(threadIds, Integer.MAX_VALUE)

        profile.recordSample()
        threadInfos.forEach { info -> profile.capture(info.threadState, info.isInNative, info.stackTrace) }
    }

    private fun flush() {
        val now = Instant.now()
        if (isTimeToReport(now)) {
            report(now)
        }
    }

    private fun report(now: Instant) {
        println("Reporting")
        lastReportTime = now

        profile.finish()

        reporter.report(profile)

        profile = Profile(application)
    }

    private fun isTimeToReport(now: Instant): Boolean {
        return Duration.between(lastReportTime, now) > reportingInterval
    }
}