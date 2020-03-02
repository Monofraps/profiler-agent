package net.monofraps.profiler.agent

import net.monofraps.profiler.agent.model.CallGraph
import org.assertj.core.api.Assertions
import kotlin.test.Test

class CallGraphTest {
    @Test fun emptyCallGraphHasEmptyChildren() {
        val fixture = CallGraph(0)

        Assertions.assertThat(fixture.children).isEmpty()
    }

    @Test fun canAddInOrderChildren() {
        val fixture = CallGraph(123)
        fixture.addChild(0)
        fixture.addChild(1)
        fixture.addChild(2)

        Assertions.assertThat(fixture.children).hasSize(3)
        Assertions.assertThat(fixture.children[0].frame).isEqualTo(0)
        Assertions.assertThat(fixture.children[1].frame).isEqualTo(1)
        Assertions.assertThat(fixture.children[2].frame).isEqualTo(2)
    }

    @Test fun canAddOutOfOrderChildren() {
        val fixture = CallGraph(123)
        fixture.addChild(5)
        fixture.addChild(3)
        fixture.addChild(120)

        Assertions.assertThat(fixture.children).hasSize(3)
        Assertions.assertThat(fixture.children[0].frame).isEqualTo(3)
        Assertions.assertThat(fixture.children[1].frame).isEqualTo(5)
        Assertions.assertThat(fixture.children[2].frame).isEqualTo(120)
    }
}