package com.choegozip.domain.model

data class ComponentInfo(
    val packageName: String,
    val className: String,
) {

    fun isEmpty(): Boolean {
        return packageName == "" || className == ""
    }

    companion object {
        fun empty() = ComponentInfo("", "")
    }
}