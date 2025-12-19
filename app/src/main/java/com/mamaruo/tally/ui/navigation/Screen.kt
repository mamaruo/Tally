package com.mamaruo.tally.ui.navigation

/**
 * 导航路由定义
 */
sealed class Screen(val route: String) {
    /** 主页 */
    data object Home : Screen("home")

    /** 新增交易 */
    data object AddTransaction : Screen("transaction/add")

    /** 编辑交易 */
    data object EditTransaction : Screen("transaction/edit/{transactionId}") {
        fun createRoute(transactionId: Long) = "transaction/edit/$transactionId"
    }

    /** 分类管理 */
    data object CategoryManage : Screen("category/manage")

    /** 新增分类 */
    data object AddCategory : Screen("category/add")
}
