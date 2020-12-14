package com.issen.workerfinder.enums

enum class CompletionTypes(private val type: String) {
    PENDING("PENDING"),
    COMPLETED("COMPLETED"),
    ACTIVE("ACTIVE"),
    ABANDONED("ABANDONED");

    override fun toString(): String {
        return type
    }
}
