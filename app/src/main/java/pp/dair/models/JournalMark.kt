package pp.dair.models

data class JournalMark(
    val subject: String,
    val date: String,
    val semester: Int,
    val mark: String,
    val missing: Boolean,
    val late: Boolean,
    val closed: Boolean
)
