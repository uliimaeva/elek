package pp.dair.models

data class Staff(
    val id: Int,
    val is_works: Int,
    val is_teacher: Int,
    val family: String,
    val name: String,
    val father: String,
)

fun getStaffName(staff: Staff): String {
    return "${staff.family} ${staff.name} ${staff.father}"
}