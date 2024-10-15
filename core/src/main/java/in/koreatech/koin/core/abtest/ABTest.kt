package `in`.koreatech.koin.core.abtest

class ABTestData(
    val experimentTitle: String, // 실험 변수
    private vararg val experimentGroups: String, // 실험군
) {
    private val groupMap: Map<String, String> = experimentGroups.associateWith { it }

    fun getGroup(groupName: String): String? {
        return groupMap[groupName]
    }
}