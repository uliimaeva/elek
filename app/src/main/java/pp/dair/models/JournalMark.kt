package pp.dair.models

import java.util.Date

data class JournalMark(
    val subject: String,
    val date: Date,
    val semester: Int,
    val mark: String,
    val missing: Boolean,
    val late: Boolean,
    val closed: Boolean
)
