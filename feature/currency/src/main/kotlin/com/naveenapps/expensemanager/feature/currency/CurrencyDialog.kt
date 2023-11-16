package com.naveenapps.expensemanager.feature.currency

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.hilt.navigation.compose.hiltViewModel
import com.naveenapps.expensemanager.core.designsystem.ui.theme.ExpenseManagerTheme
import com.naveenapps.expensemanager.core.designsystem.ui.utils.getSelectedBGColor
import com.naveenapps.expensemanager.core.model.Currency
import com.naveenapps.expensemanager.core.model.CurrencySymbolPosition


@Composable
fun CurrencyDialogView(
    complete: () -> Unit
) {

    val viewModel: CurrencyViewModel = hiltViewModel()

    val selectedCurrency by viewModel.currentCurrency.collectAsState()
    val currencies by viewModel.currencies.collectAsState()

    CurrencyDialogViewContent(
        currencies = currencies,
        selectedCurrency = selectedCurrency,
        onCurrencySelection = viewModel::selectThisCurrency,
        onCurrencyPositionTypeChange = viewModel::setCurrencyPositionType,
        onDismiss = complete,
        onSave = {
            viewModel.saveSelectedCurrency()
            complete.invoke()
        },
    )
}

@Composable
fun CurrencyDialogViewContent(
    currencies: List<Currency>,
    selectedCurrency: Currency,
    onCurrencySelection: (Currency?) -> Unit,
    onCurrencyPositionTypeChange: ((CurrencySymbolPosition) -> Unit),
    onDismiss: () -> Unit,
    onSave: () -> Unit,
) {
    Dialog(
        onDismissRequest = {
            onDismiss.invoke()
        },
        properties = DialogProperties(
            usePlatformDefaultWidth = false
        ),
    ) {
        Surface(
            modifier = Modifier.padding(16.dp),
            shape = RoundedCornerShape(8.dp)
        ) {
            LazyColumn(
                modifier = Modifier
                    .wrapContentSize()
            ) {
                item {
                    Text(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        text = stringResource(id = R.string.choose_currency),
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
                item {
                    CurrencyPositionTypeSelectionView(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 14.dp, end = 14.dp),
                        selectedCurrencyPositionType = selectedCurrency.position,
                        onCurrencyPositionTypeChange = onCurrencyPositionTypeChange
                    )
                }
                items(currencies) { currency ->
                    val isSelectedCurrency = selectedCurrency.type == currency.type
                    Row(
                        modifier = Modifier
                            .clickable {
                                onCurrencySelection.invoke(currency)
                            }
                            .fillMaxWidth()
                            .then(
                                if (isSelectedCurrency) {
                                    Modifier
                                        .padding(4.dp)
                                        .background(
                                            color = getSelectedBGColor(),
                                            shape = RoundedCornerShape(size = 12.dp)
                                        )
                                } else {
                                    Modifier
                                        .padding(4.dp)
                                }
                            )
                            .padding(12.dp),
                    ) {
                        Text(
                            modifier = Modifier.weight(1f),
                            text = "${stringResource(id = currency.name)} (${stringResource(id = currency.type)})"
                        )
                        if (isSelectedCurrency) {
                            Icon(
                                modifier = Modifier.align(Alignment.CenterVertically),
                                imageVector = Icons.Default.Done,
                                contentDescription = null
                            )
                        }
                    }
                }
                item {
                    Column(
                        modifier = Modifier
                            .padding(top = 8.dp, start = 16.dp, bottom = 8.dp)
                            .fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier
                                .wrapContentSize()
                                .align(Alignment.End)
                        ) {
                            TextButton(onClick = onDismiss) {
                                Text(text = stringResource(id = R.string.cancel).uppercase())
                            }
                            TextButton(onClick = onSave) {
                                Text(text = stringResource(id = R.string.ok).uppercase())
                            }
                        }
                    }
                }
            }
        }
    }
}

@com.naveenapps.expensemanager.core.designsystem.AppPreviewsLightAndDarkMode
@Composable
fun CurrencyDialogViewPreview() {
    ExpenseManagerTheme {
        CurrencyDialogViewContent(
            onCurrencySelection = {},
            selectedCurrency = Currency(
                com.naveenapps.expensemanager.core.common.R.string.dollar_type,
                com.naveenapps.expensemanager.core.common.R.string.dollar_name,
                com.naveenapps.expensemanager.core.common.R.drawable.currency_dollar
            ),
            currencies = emptyList(),
            onCurrencyPositionTypeChange = {

            },
            onDismiss = {},
            onSave = {},
        )
    }
}