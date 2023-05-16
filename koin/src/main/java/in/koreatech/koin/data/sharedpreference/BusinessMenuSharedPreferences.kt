package `in`.koreatech.koin.data.sharedpreference

import android.content.Context
import android.content.SharedPreferences
import com.google.common.reflect.TypeToken
import com.google.gson.Gson

class BusinessMenuSharedPreferences(private val context: Context) {
    private val sharedPreferences: SharedPreferences
        get() = context.getSharedPreferences("SelectedItemsPrefs", Context.MODE_PRIVATE)

    fun saveSelectedItems(selectedItems: List<Int>) {
        val editor = sharedPreferences.edit()
        val json = Gson().toJson(selectedItems)
        editor.putString("selectedItems", json)
        editor.apply()
    }

    fun loadSelectedItems(): List<Int> {
        val json = sharedPreferences.getString("selectedItems", null)
        return if (json != null) {
            Gson().fromJson(json, object : TypeToken<List<Int>>() {}.type)
        } else {
            emptyList()
        }
    }
}