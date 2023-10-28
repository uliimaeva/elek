package pp.dair.models

import java.util.Date

data class Note(
    val id: Int?,
    val title: String,
    val date: Date,
    val subject: String,
    val text: String
)
