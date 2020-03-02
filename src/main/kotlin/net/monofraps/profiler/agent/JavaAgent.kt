@file:JvmName("JavaAgent")

package net.monofraps.profiler.agent

import java.io.File
import java.lang.instrument.Instrumentation
import java.net.HttpURLConnection
import java.net.URL
import java.net.URLConnection
import java.security.AccessController
import java.security.PrivilegedAction
import java.util.jar.JarFile

fun premain(args: String?, instrumentation: Instrumentation) {
    println("[Profiler] Hello")
    println(System.getSecurityManager()?.securityContext)
    Profiler().start()
}

fun agentmain(args: String?, instrumentation: Instrumentation) {
    println("[Profiler] Hello 2")
    val connection = URL("http://localhost:8080").openConnection() as HttpURLConnection
    connection.requestMethod = "GET"
    println(connection.responseCode)
    Profiler().start()
}