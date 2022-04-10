import kotlinx.browser.window
import kotlin.js.Promise


// 'fun main()' no longer works since Kotlin 1.6.20.
@EagerInitialization
@OptIn(ExperimentalStdlibApi::class)
@Suppress("unused")
private val init = init()


private fun init() {
    printToDocument("index!")
    printToDocument("Loading dynamic chunk in 5 seconds…")

    window.setTimeout({
        import<DynamicModule>("./kotlin_kjs_chunks_dynamic").then { module ->
            module.printDynamic()
        }
    }, 5000)
}


external interface DynamicModule {

    fun printDynamic()
}


external fun <T> import(path: String): Promise<T>
