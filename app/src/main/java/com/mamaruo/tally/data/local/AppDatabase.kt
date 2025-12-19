package com.mamaruo.tally.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.sqlite.db.SupportSQLiteDatabase
import com.mamaruo.tally.data.model.Category
import com.mamaruo.tally.data.model.Transaction
import com.mamaruo.tally.data.model.TransactionType
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(
    entities = [Transaction::class, Category::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun transactionDao(): TransactionDao
    abstract fun categoryDao(): CategoryDao

    companion object {
        private const val DATABASE_NAME = "tally_database"

        fun create(context: Context): AppDatabase {
            return Room.databaseBuilder(
                context.applicationContext,
                AppDatabase::class.java,
                DATABASE_NAME
            )
                .addCallback(object : Callback() {
                    override fun onCreate(db: SupportSQLiteDatabase) {
                        super.onCreate(db)
                        // 预填充默认分类
                        CoroutineScope(Dispatchers.IO).launch {
                            create(context).categoryDao().insertCategories(getDefaultCategories())
                        }
                    }
                })
                .build()
        }

        /**
         * 获取默认分类列表
         */
        private fun getDefaultCategories(): List<Category> = listOf(
            // 支出分类 (8个)
            Category(
                name = "餐饮",
                type = TransactionType.EXPENSE,
                iconKey = "Restaurant",
                isDefault = true
            ),
            Category(
                name = "购物",
                type = TransactionType.EXPENSE,
                iconKey = "ShoppingCart",
                isDefault = true
            ),
            Category(
                name = "交通",
                type = TransactionType.EXPENSE,
                iconKey = "Commute",
                isDefault = true
            ),
            Category(
                name = "住房",
                type = TransactionType.EXPENSE,
                iconKey = "Home",
                isDefault = true
            ),
            Category(
                name = "娱乐",
                type = TransactionType.EXPENSE,
                iconKey = "SportsEsports",
                isDefault = true
            ),
            Category(
                name = "医疗",
                type = TransactionType.EXPENSE,
                iconKey = "MedicalServices",
                isDefault = true
            ),
            Category(
                name = "通讯",
                type = TransactionType.EXPENSE,
                iconKey = "PhoneAndroid",
                isDefault = true
            ),
            Category(
                name = "人情",
                type = TransactionType.EXPENSE,
                iconKey = "CardGiftcard",
                isDefault = true
            ),
            // 收入分类 (4个)
            Category(
                name = "工资",
                type = TransactionType.INCOME,
                iconKey = "Work",
                isDefault = true
            ),
            Category(
                name = "奖金",
                type = TransactionType.INCOME,
                iconKey = "AttachMoney",
                isDefault = true
            ),
            Category(
                name = "理财",
                type = TransactionType.INCOME,
                iconKey = "TrendingUp",
                isDefault = true
            ),
            Category(
                name = "兼职",
                type = TransactionType.INCOME,
                iconKey = "Payments",
                isDefault = true
            )
        )
    }
}
