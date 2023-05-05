package `in`.koreatech.koin.ui.businessmain

import android.os.Parcel
import android.os.Parcelable

data class MenuItem(
    val title: String,
    val imageResource: Int,
    var isSelected: Boolean = false
) : Parcelable {

    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeString(title)
        dest.writeInt(imageResource)
        dest.writeByte(if (isSelected) 1 else 0)
    }

    companion object CREATOR : Parcelable.Creator<MenuItem> {
        override fun createFromParcel(source: Parcel): MenuItem {
            val title = source.readString() ?: ""
            val imageResource = source.readInt()
            val isSelected = source.readByte().toInt() != 0
            return MenuItem(title, imageResource, isSelected)
        }

        override fun newArray(size: Int): Array<MenuItem?> {
            return arrayOfNulls(size)
        }
    }
}
