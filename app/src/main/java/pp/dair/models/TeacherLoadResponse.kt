package pp.dair.models

data class TeacherLoadResponse(
    val hours: Map<String, ArrayList<TeacherLoad>>,
    val total: Map<String, TeacherLoad>,
    val left: Map<String, String>
)
