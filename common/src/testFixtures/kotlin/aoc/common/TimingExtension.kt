package aoc.common

import aoc.common.util.formatted
import org.junit.jupiter.api.extension.AfterAllCallback
import org.junit.jupiter.api.extension.AfterTestExecutionCallback
import org.junit.jupiter.api.extension.BeforeAllCallback
import org.junit.jupiter.api.extension.BeforeTestExecutionCallback
import org.junit.jupiter.api.extension.ExtensionContext
import java.lang.reflect.Method

class TimingExtension :
    BeforeTestExecutionCallback,
    AfterTestExecutionCallback,
    BeforeAllCallback,
    AfterAllCallback {
    @Throws(Exception::class)
    override fun beforeAll(context: ExtensionContext) {
        getStore(context).put(CLASS_START_TIME, System.currentTimeMillis())
    }

    @Throws(Exception::class)
    override fun beforeTestExecution(context: ExtensionContext) {
        getStore(context).put(TEST_START_TIME, System.currentTimeMillis())
    }

    @Throws(Exception::class)
    override fun afterTestExecution(context: ExtensionContext) {
        val testClass = context.requiredTestClass.name
        val testMethod: Method = context.requiredTestMethod
        val startTime: Long = getStore(context).remove(TEST_START_TIME, Long::class.java)!!
        val duration = System.currentTimeMillis() - startTime
        if (duration > 50) {
            println("$testClass -> Method [${testMethod.name}] took ${duration.formatted()} ms.")
        }
    }

    @Throws(Exception::class)
    override fun afterAll(context: ExtensionContext) {
        val testClass = context.requiredTestClass.name
        val startTime: Long = getStore(context).remove(CLASS_START_TIME, Long::class.java)!!
        val duration = System.currentTimeMillis() - startTime
        if (duration > 50) {
            println("$testClass -> Class test took ${duration.formatted()} ms.")
        }
    }

    private fun getStore(context: ExtensionContext): ExtensionContext.Store =
        context.getStore(ExtensionContext.Namespace.create(javaClass, context.requiredTestClass))

    companion object {
        private const val CLASS_START_TIME = "class start time"
        private const val TEST_START_TIME = "test start time"
    }
}
