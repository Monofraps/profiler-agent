package net.monofraps.profiler.agent.reporter

import net.monofraps.profiler.agent.model.Profile

interface ReporterPlugin {
    fun report(profile: Profile)
}