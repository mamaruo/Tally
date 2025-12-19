package com.mamaruo.tally.ui.home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LargeFloatingActionButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.mamaruo.tally.data.model.TransactionType
import com.mamaruo.tally.data.model.TransactionWithCategory
import com.mamaruo.tally.ui.util.AmountFormatter
import com.mamaruo.tally.ui.util.IconRegistry
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    onAddTransaction: () -> Unit,
    onEditTransaction: (Long) -> Unit,
    onManageCategories: () -> Unit,
    viewModel: HomeViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Tally 记账") },
                actions = {
                    IconButton(onClick = onManageCategories) {
                        Icon(
                            imageVector = Icons.Default.Settings,
                            contentDescription = "分类管理"
                        )
                    }
                }
            )
        },
        floatingActionButton = {
            LargeFloatingActionButton(
                onClick = onAddTransaction
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "新增交易"
                )
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            // 顶部概览区
            OverviewSection(
                monthlyExpense = uiState.monthlyExpense,
                monthlyBalance = uiState.monthlyBalance,
                modifier = Modifier.padding(16.dp)
            )

            // 交易列表
            if (uiState.transactions.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "暂无交易记录",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            } else {
                TransactionList(
                    transactionsByDate = uiState.transactionsByDate,
                    onTransactionClick = onEditTransaction
                )
            }
        }
    }
}

@Composable
private fun OverviewSection(
    monthlyExpense: Long,
    monthlyBalance: Long,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // 当月花销
        OverviewCard(
            title = "当月花销",
            amount = monthlyExpense,
            modifier = Modifier.weight(1f)
        )

        // 当月结余
        OverviewCard(
            title = "当月结余",
            amount = monthlyBalance,
            showSign = true,
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
private fun OverviewCard(
    title: String,
    amount: Long,
    showSign: Boolean = false,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = if (showSign && amount >= 0) {
                    "+${AmountFormatter.format(amount)}"
                } else if (showSign && amount < 0) {
                    AmountFormatter.format(amount)
                } else {
                    AmountFormatter.format(amount)
                },
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                color = if (showSign) {
                    if (amount >= 0) MaterialTheme.colorScheme.primary
                    else MaterialTheme.colorScheme.error
                } else {
                    MaterialTheme.colorScheme.onSurface
                }
            )
        }
    }
}

@Composable
private fun TransactionList(
    transactionsByDate: Map<LocalDate, List<TransactionWithCategory>>,
    onTransactionClick: (Long) -> Unit
) {
    val dateFormatter = DateTimeFormatter.ofPattern("MM/dd EEEE")
    val sortedDates = transactionsByDate.keys.sortedDescending()

    LazyColumn(
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        sortedDates.forEach { date ->
            val transactions = transactionsByDate[date] ?: emptyList()

            // 日期头部
            item(key = "header_$date") {
                Text(
                    text = date.format(dateFormatter),
                    style = MaterialTheme.typography.titleSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(vertical = 8.dp)
                )
            }

            // 该日期的交易
            items(
                items = transactions,
                key = { it.transaction.id }
            ) { transactionWithCategory ->
                TransactionItem(
                    transactionWithCategory = transactionWithCategory,
                    onClick = { onTransactionClick(transactionWithCategory.transaction.id) }
                )
            }
        }
    }
}

@Composable
private fun TransactionItem(
    transactionWithCategory: TransactionWithCategory,
    onClick: () -> Unit
) {
    val transaction = transactionWithCategory.transaction
    val category = transactionWithCategory.category

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // 分类图标
            Icon(
                imageVector = IconRegistry.getIcon(category.iconKey),
                contentDescription = category.name,
                modifier = Modifier.size(40.dp),
                tint = MaterialTheme.colorScheme.primary
            )

            Spacer(modifier = Modifier.width(12.dp))

            // 分类名称和备注
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = category.name,
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Medium
                )
                if (!transaction.note.isNullOrBlank()) {
                    Text(
                        text = transaction.note,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }

            // 金额
            Text(
                text = if (transaction.type == TransactionType.INCOME) {
                    "+${AmountFormatter.format(transaction.amountMinor)}"
                } else {
                    "-${AmountFormatter.format(transaction.amountMinor)}"
                },
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Medium,
                color = if (transaction.type == TransactionType.INCOME) {
                    MaterialTheme.colorScheme.primary
                } else {
                    MaterialTheme.colorScheme.error
                }
            )
        }
    }
}
