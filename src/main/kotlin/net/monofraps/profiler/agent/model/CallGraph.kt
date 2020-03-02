package net.monofraps.profiler.agent.model

import java.lang.RuntimeException
import java.util.*

class CallGraph(val frame: Int) {
    companion object {
        private val NO_CHILDREN: Array<CallGraph> = arrayOf()
        private val DUMMY: CallGraph = CallGraph(-1)
    }

    val counts: LongArray = Thread.State.values().map { state -> state.ordinal.toLong() }.toLongArray()
    var children: Array<CallGraph> = NO_CHILDREN
        private set
    var hasCounts = false
        private set

    fun addChild(encodedFrame: Int): CallGraph {
        val position = Arrays.binarySearch(children, DUMMY, { a, _ -> a.frame.compareTo(encodedFrame) })
        if (position >= 0) {
            return children[position]
        }

        val insertPosition = -(position + 1)
        val child = CallGraph(encodedFrame)
        insertChild(insertPosition, child)

        return child;
    }

    private fun insertChild(position: Int, node: CallGraph) {
        val newChildren = Array(children.size + 1) { index ->
            when {
                index < position -> children[index]
                index == position -> node
                index > position -> children[index - 1]
                else -> throw RuntimeException("Unreachable")
            }
        }
        children = newChildren
    }

    fun addCounts(stateEncoding: Int, count: Long) {
        counts[stateEncoding] += count
        hasCounts = true
    }

}