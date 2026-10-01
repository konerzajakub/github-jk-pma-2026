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

// Unicode symboly pro hodnoty kostky 1–6
private val diceSymbols = listOf("⚀", "⚁", "⚂", "⚃", "⚄", "⚅")

// Počet náhodných změn během animace a prodleva mezi nimi
private const val ANIMATION_STEPS = 10
private const val STEP_DELAY_MS = 250L

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
                    isRolling = false
                }
            }
        ) {
            Text(text = stringResource(R.string.roll), fontSize = 20.sp)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DiceScreenPreview() {
    MaterialTheme {
        DiceScreen()
    }
}
