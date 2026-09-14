package `in`.koreatech.koin.feature.recruitment.model

import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toPersistentList

data class SkillEntry(
    val id: Long,
    val text: String
)

fun ImmutableList<SkillEntry>.withNewSkill(): ImmutableList<SkillEntry> {
    val nextId = (maxOfOrNull { it.id } ?: 0L) + 1L
    return (this + SkillEntry(id = nextId, text = "")).toPersistentList()
}

fun ImmutableList<SkillEntry>.withSkillText(id: Long, text: String): ImmutableList<SkillEntry> =
    map { skill -> if (skill.id == id) skill.copy(text = text) else skill }.toPersistentList()

fun ImmutableList<SkillEntry>.withoutSkill(id: Long): ImmutableList<SkillEntry> =
    filterNot { it.id == id }.toPersistentList()
