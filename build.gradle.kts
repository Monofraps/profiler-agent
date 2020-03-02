import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("org.jetbrains.kotlin.jvm") version "1.3.41"
    id("com.github.johnrengelman.shadow") version "5.1.0"

    java
}

repositories {
    jcenter()
}

dependencies {
    implementation(platform("org.jetbrains.kotlin:kotlin-bom"))

    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")

    implementation("com.squareup.okhttp3:okhttp:4.2.2")
    implementation("com.fasterxml.jackson.core:jackson-core:2.10.0")

    testImplementation("org.jetbrains.kotlin:kotlin-test")

    testImplementation("org.jetbrains.kotlin:kotlin-test-junit")

    testImplementation("org.assertj:assertj-core:3.11.1")
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs = listOf("-Xjsr305=strict")
        jvmTarget = "1.8"
    }
}

tasks.withType<Jar> {
    manifest {
        attributes(mapOf(
                "Main-Class" to "net.monofraps.profiler.agent.JavaAgentAttacher",
                "Premain-Class" to "net.monofraps.profiler.agent.JavaAgent",
                "Agent-Class" to "net.monofraps.profiler.agent.JavaAgent"
        ))
    }
}