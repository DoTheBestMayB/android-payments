package nextstep.payments.ui.new_card

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import nextstep.payments.ui.theme.PaymentsTheme

class NewCardActivity: ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PaymentsTheme {
                NewCardScreenRoot(
                    navigateToCardList = {
                        setResult(RESULT_OK)
                        finish()
                    }
                )
            }
        }
    }
}