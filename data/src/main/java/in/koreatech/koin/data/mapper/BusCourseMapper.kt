package `in`.koreatech.koin.data.mapper

import `in`.koreatech.koin.domain.model.bus.BusDirection
import `in`.koreatech.koin.domain.model.bus.BusType
import `in`.koreatech.koin.domain.model.bus.course.BusCourse

fun BusCourse.toCourseNameString() : String {
    return when(busType) {
        BusType.Shuttle -> {
            "$region ${if(direction == BusDirection.ToKoreatech) "셔틀 등교" else "셔틀 하교"}"
        }
        BusType.Commuting -> {
            "$region ${if(direction == BusDirection.ToKoreatech) "등교" else "하교"}"
        }
        BusType.Express -> {
            if(direction == BusDirection.ToKoreatech) "야우리->한기대" else "한기대->야우리"
        }
        else -> ""
    }
}

/*
[
  {
    "bus_type": "shuttle",
    "direction": "to",
    "region": "청주"
  },
  {
    "bus_type": "shuttle",
    "direction": "from",
    "region": "청주"
  },
  {
    "bus_type": "commuting",
    "direction": "to",
    "region": "청주"
  },
  {
    "bus_type": "commuting",
    "direction": "from",
    "region": "청주"
  },
  {
    "bus_type": "commuting",
    "direction": "to",
    "region": "천안"
  },
  {
    "bus_type": "commuting",
    "direction": "from",
    "region": "천안"
  },
  {
    "bus_type": "shuttle",
    "direction": "to",
    "region": "천안"
  },
  {
    "bus_type": "shuttle",
    "direction": "from",
    "region": "천안"
  },
  {
    "bus_type": "commuting",
    "direction": "to",
    "region": "세종"
  },
  {
    "bus_type": "commuting",
    "direction": "from",
    "region": "세종"
  },
  {
    "bus_type": "commuting",
    "direction": "to",
    "region": "대전"
  },
  {
    "bus_type": "commuting",
    "direction": "from",
    "region": "대전"
  },
  {
    "bus_type": "commuting",
    "direction": "to",
    "region": "서울"
  },
  {
    "bus_type": "commuting",
    "direction": "from",
    "region": "서울"
  }
]
 */