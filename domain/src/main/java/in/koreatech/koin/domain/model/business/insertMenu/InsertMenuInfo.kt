package `in`.koreatech.koin.domain.model.business.insertMenu

data class InsertMenuInfo(
    var menuName: String,
    var menuInfo: List<MenuInfo>,
    var menuComposition: String,
    var isEventMenu: Boolean,
    var isRepresentativeMenu: Boolean,
    var isSideMenu: Boolean,
    var isSetMenu: Boolean
    //var menuImage: List<Uri>
)