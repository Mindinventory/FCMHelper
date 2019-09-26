package model

enum class FileType(
    val displayName: String,
    val extension: String
) {
    KOTLIN("Kotlin", "kt"),
    TEXT("Text", "txt");
    override fun toString() = displayName
}