package pp.dair.models

data class LessonWithGroup(
    val subgroup: Int,
    val number: Int,
    val time: String,
    val subject: String,
    val teacher: String,
    val audience: String,
    val group: String
)
