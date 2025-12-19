package com.mamaruo.tally.ui.util

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material.icons.filled.AttachMoney
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.BusinessCenter
import androidx.compose.material.icons.filled.CardGiftcard
import androidx.compose.material.icons.filled.Celebration
import androidx.compose.material.icons.filled.Commute
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material.icons.filled.Flight
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Hotel
import androidx.compose.material.icons.filled.LaptopMac
import androidx.compose.material.icons.filled.LocalCafe
import androidx.compose.material.icons.filled.LocalGasStation
import androidx.compose.material.icons.filled.LocalGroceryStore
import androidx.compose.material.icons.filled.LocalLaundryService
import androidx.compose.material.icons.filled.MedicalServices
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.Movie
import androidx.compose.material.icons.filled.Payments
import androidx.compose.material.icons.filled.Pets
import androidx.compose.material.icons.filled.PhoneAndroid
import androidx.compose.material.icons.filled.ReceiptLong
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.material.icons.filled.Savings
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.SportsEsports
import androidx.compose.material.icons.filled.TrendingDown
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material.icons.filled.Work
import androidx.compose.ui.graphics.vector.ImageVector

/**
 * 图标注册表，维护 iconKey 到 ImageVector 的映射
 */
object IconRegistry {

    private val iconMap: Map<String, ImageVector> = mapOf(
        // 生活消费 (12)
        "Restaurant" to Icons.Filled.Restaurant,        // 餐饮
        "LocalCafe" to Icons.Filled.LocalCafe,          // 咖啡/饮品
        "ShoppingCart" to Icons.Filled.ShoppingCart,    // 购物
        "LocalGroceryStore" to Icons.Filled.LocalGroceryStore, // 超市/杂货
        "Commute" to Icons.Filled.Commute,              // 交通
        "LocalGasStation" to Icons.Filled.LocalGasStation, // 加油
        "Home" to Icons.Filled.Home,                    // 住房/家居
        "Hotel" to Icons.Filled.Hotel,                  // 住宿/酒店
        "LocalLaundryService" to Icons.Filled.LocalLaundryService, // 洗衣/清洁
        "MedicalServices" to Icons.Filled.MedicalServices, // 医疗/药品
        "SportsEsports" to Icons.Filled.SportsEsports,  // 娱乐/游戏
        "Movie" to Icons.Filled.Movie,                  // 电影/影音

        // 财务收支 (8)
        "AttachMoney" to Icons.Filled.AttachMoney,      // 现金/通用收入
        "AccountBalance" to Icons.Filled.AccountBalance, // 银行/账户
        "CreditCard" to Icons.Filled.CreditCard,        // 信用卡/支付
        "Savings" to Icons.Filled.Savings,              // 存款/理财
        "TrendingUp" to Icons.Filled.TrendingUp,        // 投资/增长
        "TrendingDown" to Icons.Filled.TrendingDown,    // 亏损/减少
        "Payments" to Icons.Filled.Payments,            // 账单/付款
        "ReceiptLong" to Icons.Filled.ReceiptLong,      // 报销/单据

        // 工作学习 (6)
        "Work" to Icons.Filled.Work,                    // 工资/工作
        "BusinessCenter" to Icons.Filled.BusinessCenter, // 商务/办公
        "School" to Icons.Filled.School,                // 教育/学习
        "MenuBook" to Icons.Filled.MenuBook,            // 书籍/资料
        "LaptopMac" to Icons.Filled.LaptopMac,          // 设备/数码
        "Build" to Icons.Filled.Build,                  // 维修/工具

        // 其他 (4)
        "Flight" to Icons.Filled.Flight,                // 旅行/机票
        "Pets" to Icons.Filled.Pets,                    // 宠物
        "CardGiftcard" to Icons.Filled.CardGiftcard,    // 礼品/红包
        "Celebration" to Icons.Filled.Celebration,      // 节日/庆典

        // 额外（用于默认分类）
        "PhoneAndroid" to Icons.Filled.PhoneAndroid     // 通讯
    )

    /**
     * 根据 iconKey 获取对应的 ImageVector
     * 如果找不到则返回默认图标
     */
    fun getIcon(iconKey: String): ImageVector {
        return iconMap[iconKey] ?: Icons.Filled.AttachMoney
    }

    /**
     * 获取所有可用的图标键
     */
    fun getAllIconKeys(): List<String> {
        return iconMap.keys.toList()
    }

    /**
     * 获取所有图标（键值对）
     */
    fun getAllIcons(): Map<String, ImageVector> {
        return iconMap
    }
}
