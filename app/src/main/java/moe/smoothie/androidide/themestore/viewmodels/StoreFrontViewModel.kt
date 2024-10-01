package moe.smoothie.androidide.themestore.viewmodels

interface StoreFrontViewModel {
    fun loadItems(pageSize: Int)

    fun reload(pageSize: Int)
}
