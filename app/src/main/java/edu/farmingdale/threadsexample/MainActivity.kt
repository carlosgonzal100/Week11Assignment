package edu.farmingdale.threadsexample

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import edu.farmingdale.threadsexample.countdowntimer.CountDownActivity
import edu.farmingdale.threadsexample.ui.theme.ThreadsExampleTheme
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.NumberFormat
import java.util.Locale

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        //used an explicit intent to start the CountDownActivity
        val intent = Intent(this, CountDownActivity::class.java)
        startActivity(intent)

        /**
        setContent {
            ThreadsExampleTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->

                    Box(modifier = Modifier.padding(innerPadding)) {
                        //FibonacciDemoNoBgThrd()
                        //FibonacciDemoWithCoroutine()
                    }
                }
            }
        }
        */
    }
}

@Composable
fun FibonacciDemoWithCoroutine() {
    var answer by remember { mutableStateOf("") }
    var textInput by remember { mutableStateOf("40") }
    var isBusy by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()

    Column {
        Row {
            TextField(
                value = textInput,
                onValueChange = { textInput = it },
                label = { Text("Number?") },
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )
            Button(
                onClick = {
                    val n = textInput.toLongOrNull() ?: 0L
                    isBusy = true
                    scope.launch(Dispatchers.Default) {
                        val fib = fibonacciIterative(n)
                        val formatted = NumberFormat.getNumberInstance(Locale.US).format(fib)
                        withContext(Dispatchers.Main) {
                            answer = formatted
                            isBusy = false
                        }
                    }
                }
            ) {
                Text(if (isBusy) "Working..." else "Fibonacci (async)")
            }
        }

        Text("Result: $answer")
    }
}

/** Fast, non-blocking iterative Fibonacci on a background thread. */
private fun fibonacciIterative(n: Long): Long {
    if (n <= 1) return n
    var a = 0L
    var b = 1L
    var i = 2L
    while (i <= n) {
        val c = a + b
        a = b
        b = c
        i++
    }
    return b
}

// ToDo 1: Call `FibonacciDemoNoBgThrd` that calculates the Fibonacci number of a given number.
/** called FibonacciDemoNoBgThrd(), it wouldnt let me use it without padding
 * so i just wrapped the function in a box
 */

// ToDo 2: Create a composable function called `FibonacciDemoWithCoroutine` that calculates the
//  Fibonacci number of a given number using a coroutine.
/** asked chat gpt to create the function used for this toDO
 * i put it in the setContent to see it, but the assignment
 * dousnt say to use it yet so i will leave FibonacciDemoNoBgThrd for now*/

// ToDo 3: Start the application using the CountDownActivity
/**the application now from this point, always starts using the
 * countdown activity. i declared the activity in the app manifest
 * so it knows its an activity and then used an explicit intent to
 * launch the activity. i put the other things for the other todos
 * asside, commenting it out
 */

// ToDo 4: Make the Text of the timer larger
// ToDo 5: Show a visual indicator of the timer going down to 0
// ToDo 6: Add a button to rest the timer
// ToDo 7: Play a sound when the timer reaches 0
// ToDo 8: During the last 10 seconds, make the text red and bold
