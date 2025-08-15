package nextstep.payments

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import nextstep.payments.ui.card_list.CardListScreenRoot
import nextstep.payments.ui.card_list.CardListViewModel
import nextstep.payments.ui.new_card.NewCardActivity
import nextstep.payments.ui.theme.PaymentsTheme

class MainActivity : ComponentActivity() {

    private val viewModel by viewModels<CardListViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            val launcher =
                rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) {
                    if (it.resultCode == RESULT_OK) {
                        viewModel.fetchCards()
                    }
                }

            PaymentsTheme {
                CardListScreenRoot(
                    viewModel = viewModel,
                    navigateToNewCard = {
                        val intent = Intent(this, NewCardActivity::class.java)
                        launcher.launch(intent)
                    }
                )
            }
        }
    }
}
