package pp.dair.utils

import pp.dair.models.TeacherNote
import java.util.TreeMap

fun sortNotesByTeacherAndSubject(notes: ArrayList<TeacherNote>): TreeMap<String, List<TeacherNote>> {
    val noteByTeacher = notes.groupBy { it.owner.name }
    val totalMap: MutableMap<String, List<TeacherNote>> = mutableMapOf()
    for (pair in noteByTeacher) {
        val noteBySubjects = pair.value.groupBy { it.subject }
        for (innerPair in noteBySubjects) {
            totalMap[pair.key + " - " + innerPair.key] = innerPair.value
        }
    }
    return TreeMap(totalMap)
}

fun sortNotesByGroupAndSubject(notes: ArrayList<TeacherNote>): TreeMap<String, List<TeacherNote>> {
    val noteBySubjects = notes.groupBy { it.subject }
    val totalMap: MutableMap<String, List<TeacherNote>> = mutableMapOf()
    for (pair in noteBySubjects) {
        val noteByGroups = pair.value.groupBy { it.group }
        for (innerPair in noteByGroups) {
            totalMap[innerPair.key + " - " + pair.key] = innerPair.value
        }
    }
    return TreeMap(totalMap)
}