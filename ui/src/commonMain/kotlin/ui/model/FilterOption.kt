package ui.model

enum class FilterOption(val text: String) {
    Source("Source"),
    Sink("Sink"),
    SPLITTER("Splitter"),
    MERGER("Merger"),

    INPUT_MATERIAL("Input Material"),
    RECIPE("Recipe"),
    OUTPUT_MATERIAL("Output Material");
}