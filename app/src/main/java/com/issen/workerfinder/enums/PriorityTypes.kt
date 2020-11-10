package com.issen.workerfinder.enums

enum class PriorityTypes(private val type: String) {
    URGENT("URGENT"),
    HIGH("HIGH"),
    NORMAL("NORMAL"),
    LOW("LOW"),
    OTHER("OTHER");

    override fun toString(): String {
        return type
    }
}

