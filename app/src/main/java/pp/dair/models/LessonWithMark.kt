package pp.dair.models

data class LessonWithMark(
    val subgroup: Int,
    val number: Int,
    val time: String,
    val subject: String,
    val teacher: String,
    val audience: String,
    val mark: JournalMark? = null
)
