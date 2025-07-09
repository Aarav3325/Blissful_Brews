package com.brews.cafeorderingapp.model

data class MenuModel(
    val imageUrl: String = "",
    val itemDes: String = "",
    val itemName: String = "",
    val itemPrice: Int = 0,
    val itemId: String = "",
    var count: Int = 0,
//    var showSizeSelection: Boolean = false,
//    var sizes: List<String>? = null,
//    var showCustomization: Boolean = false,
//    var availability: Boolean = true,
//    var customization: List<String>? = null,
//    var category: String = ""
)
