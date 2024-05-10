package pp.dair.models

data class TeacherNoteCreate(
    val title: String,
    val text: String,
    val subject: String,
    val group: String,
    val public: Boolean
)
