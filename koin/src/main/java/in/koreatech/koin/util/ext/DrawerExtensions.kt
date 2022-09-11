package `in`.koreatech.koin.util.ext

import android.view.View
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout

inline fun DrawerLayout.addDrawerListener(
    crossinline onDrawerOpened: (drawerView: View) -> Unit = {},
    crossinline onDrawerClosed: (drawerView: View) -> Unit = {},
    crossinline onDrawerStateChanged: (newState: Int) -> Unit = {},
    crossinline onDrawerSlide: (drawerView: View, slideOffset: Float) -> Unit
) {
    this.addDrawerListener(object : DrawerLayout.DrawerListener {
        override fun onDrawerSlide(drawerView: View, slideOffset: Float) {
            onDrawerSlide(drawerView, slideOffset)
        }

        override fun onDrawerOpened(drawerView: View) {
            onDrawerOpened(drawerView)
        }

        override fun onDrawerClosed(drawerView: View) {
            onDrawerClosed(drawerView)
        }

        override fun onDrawerStateChanged(newState: Int) {
            onDrawerStateChanged(newState)
        }
    })
}

fun DrawerLayout.isDrawerOpened(gravity: Int = GravityCompat.END) = isDrawerOpen(gravity)

fun DrawerLayout.openDrawer() {
    openDrawer(GravityCompat.END)
}

fun DrawerLayout.closeDrawer() {
    closeDrawer(GravityCompat.END)
}

fun DrawerLayout.toggleDrawer(gravity: Int = GravityCompat.END) {
    if(isDrawerOpened(gravity)) closeDrawer(gravity) else openDrawer(gravity)
}