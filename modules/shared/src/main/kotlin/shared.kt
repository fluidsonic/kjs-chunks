import kotlinx.browser.document


fun printToDocument(string: String) {
    document.body!!.append(document.createElement("p").also { it.textContent = string })
}
