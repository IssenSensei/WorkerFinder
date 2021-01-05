package com.issen.workerfinder.enums

enum class CompletionTypes(private val type: String) {
    PENDING("PENDING"),
    COMPLETED("COMPLETED"),
    ACTIVE("ACTIVE"),
    OFFERED("OFFERED"),
    REFUSED("REFUSED"),
    ABANDONED("ABANDONED"),
    BOARD("BOARD");

    override fun toString(): String {
        return type
    }
}
