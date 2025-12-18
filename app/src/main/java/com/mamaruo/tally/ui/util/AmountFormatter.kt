package com.mamaruo.tally.ui.util

import java.text.DecimalFormat

/**
 * 金额格式化工具
 */
object AmountFormatter {
    
    private val decimalFormat = DecimalFormat("#,##0.00")
    
    /**
     * 将分转换为元并格式化显示
     * @param amountMinor 金额（分）
     * @param showSign 是否显示正负号
     * @return 格式化后的金额字符串，如 "¥123.45"
     */
    fun format(amountMinor: Long, showSign: Boolean = false): String {
        val amount = amountMinor / 100.0
        val formatted = decimalFormat.format(amount)
        return if (showSign && amountMinor > 0) {
            "+¥$formatted"
        } else {
            "¥$formatted"
        }
    }
    
    /**
     * 将用户输入的金额字符串转换为分
     * @param input 用户输入的金额字符串（如 "123.45"）
     * @return 金额（分），如果输入无效则返回 null
     */
    fun parseToMinor(input: String): Long? {
        return try {
            val amount = input.toDoubleOrNull() ?: return null
            if (amount < 0) return null
            (amount * 100).toLong()
        } catch (e: Exception) {
            null
        }
    }
    
    /**
     * 将分转换为元的字符串（用于编辑时显示）
     */
    fun toEditableString(amountMinor: Long): String {
        val amount = amountMinor / 100.0
        return if (amount == amount.toLong().toDouble()) {
            amount.toLong().toString()
        } else {
            String.format("%.2f", amount).trimEnd('0').trimEnd('.')
        }
    }
}
