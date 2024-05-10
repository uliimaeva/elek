package pp.dair.models

import java.util.Date

data class TeacherNote(
    val title: String,
    val text: String,
    val subject: String,
    val group: String,
    val public: Boolean,
    val id: Int,
    val date: Date,
    val owner: TeacherNoteUser
)