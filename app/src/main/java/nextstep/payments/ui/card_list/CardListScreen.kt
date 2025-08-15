package nextstep.payments.ui.card_list

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun CardListScreenRoot(
    viewModel: CardListViewModel,
    navigateToNewCard: () -> Unit,
    modifier: Modifier = Modifier,
) {
    CardListScreen(
        navigateToNewCard = navigateToNewCard,
        modifier = modifier,
    )
}

@Composable
fun CardListScreen(
    navigateToNewCard: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Scaffold(
        modifier = modifier,
    ) { innerPadding ->
        Column(
            modifier = Modifier.padding(innerPadding),
        ) {
            Button(
                onClick = navigateToNewCard,
            ) {
                Text("카드 추가 화면으로 이동")
            }
        }
    }
}