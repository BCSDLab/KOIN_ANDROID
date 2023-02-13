package `in`.koreatech.koin.domain.util.ext

import `in`.koreatech.koin.domain.model.land.Land

fun List<Land>.nameSearchFilter(searching: String) = this.filter {
    it.name.contains(searching)
}