package com.naveenapps.expensemanager.feature.currency

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.naveenapps.expensemanager.core.designsystem.ui.components.AppFilterChip
import com.naveenapps.expensemanager.core.designsystem.ui.theme.ExpenseManagerTheme
import com.naveenapps.expensemanager.core.model.CurrencySymbolPosition


@Composable
fun CurrencyPositionTypeSelectionView(
    selectedCurrencyPositionType: CurrencySymbolPosition,
    onCurrencyPositionTypeChange: ((CurrencySymbolPosition) -> Unit),
    modifier: Modifier = Modifier
) {

    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        AppFilterChip(
            modifier = Modifier
                .align(Alignment.CenterVertically)
                .weight(1f),
            filterName = stringResource(id = R.string.prefix_amount),
            isSelected = selectedCurrencyPositionType == CurrencySymbolPosition.PREFIX,
            onClick = {
                onCurrencyPositionTypeChange.invoke(CurrencySymbolPosition.PREFIX)
            }
        )
        AppFilterChip(
            modifier = Modifier
                .align(Alignment.CenterVertically)
                .weight(1f),
            filterName = stringResource(id = R.string.suffix_amount),
            isSelected = selectedCurrencyPositionType == CurrencySymbolPosition.SUFFIX,
            onClick = {
                onCurrencyPositionTypeChange.invoke(CurrencySymbolPosition.SUFFIX)
            }
        )
    }
}

@Preview
@Composable
private fun CurrencyPositionTypeSelectionViewPreview() {
    ExpenseManagerTheme {
        Column {
            CurrencyPositionTypeSelectionView(
                modifier = Modifier
                    .padding(top = 16.dp, start = 16.dp, end = 16.dp)
                    .fillMaxWidth(),
                selectedCurrencyPositionType = CurrencySymbolPosition.PREFIX,
                onCurrencyPositionTypeChange = {}
            )
            CurrencyPositionTypeSelectionView(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth(),
                selectedCurrencyPositionType = CurrencySymbolPosition.SUFFIX,
                onCurrencyPositionTypeChange = {}
            )
        }
    }
}