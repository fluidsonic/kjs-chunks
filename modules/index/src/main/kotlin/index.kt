import kotlinx.browser.window
import kotlin.js.Promise


// @JsExport is needed or else dead code elimination will remove this.
@JsExport
@OptIn(ExperimentalJsExport::class)
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
