package pp.dair.models

data class BaseLesson(
    val subgroup: Int,
    val number: Int,
    val time: String,
    val subject: String,
    val teacher: String,
    val audience: String
)
