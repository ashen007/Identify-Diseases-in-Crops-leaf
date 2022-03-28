package com.example.planter

data class Recognition(
    var id: String = "",
    var title: String = "",
    var confidence: Float = 0f
) {
    override fun toString(): String {
        return "Title = $title, Confidence = $confidence"
    }
}