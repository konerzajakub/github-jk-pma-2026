package com.example.myapp003compose

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

// Unicode symboly pro hodnoty kostky 1-6
private val diceSymbols = listOf("⚀", "⚁", "⚂", "⚃", "⚄", "⚅")

// Počet náhodných změn během animace a prodleva mezi nimi
private const val ANIMATION_STEPS = 10
private const val STEP_DELAY_MS = 250L

// Kolik posledních hodů se ukazuje
private const val HISTORY_SIZE = 5

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // Rozhraní je definované v Kotlinu, žádný XML layout
        setContent {
            MaterialTheme {
                // Scaffold dodá odsazení od systémových lišt (innerPadding)
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    DiceScreen(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}

@Composable
fun DiceScreen(modifier: Modifier = Modifier) {
    // Stav obrazovky: zobrazený symbol a zda právě běží animace.
    // Při změně stavu Compose obrazovku sám překreslí.
    var dice by remember { mutableStateOf(diceSymbols.first()) }
    var isRolling by remember { mutableStateOf(false) }

    // Počet hodů a posledních pár výsledků (nejnovější první)
    var rollCount by remember { mutableIntStateOf(0) }
    var history by remember { mutableStateOf(listOf<String>()) }
    val scope = rememberCoroutineScope()

    // Prvky pod sebou, vystředěné na obrazovce
    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Nadpis aplikace
        Text(
            text = stringResource(R.string.title),
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold
        )

        // Velký symbol kostky
        Text(
            text = dice,
            fontSize = 160.sp,
            modifier = Modifier.padding(vertical = 24.dp)
        )

        // Tlačítko pro hod, během animace zakázané
        Button(
            enabled = !isRolling,
            onClick = {
                // Animace hodu: několik náhodných změn, pak výsledný hod
                scope.launch {
                    isRolling = true
                    repeat(ANIMATION_STEPS) {
                        dice = diceSymbols.random()
                        delay(STEP_DELAY_MS)
                    }
                    dice = diceSymbols.random()

                    // Výsledek se přičte a přidá na začátek historie
                    rollCount++
                    history = (listOf(dice) + history).take(HISTORY_SIZE)

                    isRolling = false
                }
            }
        ) {
            Text(text = stringResource(R.string.roll), fontSize = 20.sp)
        }

        // Počet hodů
        Text(
            text = stringResource(R.string.roll_count, rollCount),
            fontSize = 20.sp,
            modifier = Modifier.padding(top = 32.dp)
        )

        // Historie posledních hodů
        Text(
            text = stringResource(R.string.history),
            fontSize = 20.sp,
            modifier = Modifier.padding(top = 8.dp)
        )
        Text(
            text = history.joinToString(" "),
            fontSize = 36.sp
        )
    }
}

@Preview(showBackground = true)
@Composable
fun DiceScreenPreview() {
    MaterialTheme {
        DiceScreen()
    }
}
