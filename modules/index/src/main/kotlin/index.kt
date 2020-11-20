import kotlinx.browser.window
import kotlin.js.Promise


@JsExport
fun main() {
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
