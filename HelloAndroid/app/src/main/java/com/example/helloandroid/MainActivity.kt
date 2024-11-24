package com.example.helloandroid

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.helloandroid.audio.SoundPoolManager
import com.example.helloandroid.audio.TTSManager

class MainActivity : ComponentActivity() {
    private lateinit var soundPoolManager: SoundPoolManager
    private lateinit var ttsManager: TTSManager
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        soundPoolManager = SoundPoolManager(this)
        ttsManager = TTSManager(this)
        enableEdgeToEdge()
        setContent {
            MyApp{
                ScaffoldExample(
                    onButtonClick = {
                            index ->
                        soundPoolManager.playSound(index.toString())
                    },
                    onSpeak = {
                        text -> ttsManager.speak(text)
                    }
                )
            }
        }
    }
    override fun onDestroy() {
        soundPoolManager.release()
        ttsManager.release()
        super.onDestroy()
    }
}


@Composable
fun MyApp(content: @Composable () -> Unit) {
    MaterialTheme {
        content()
    }
}


@Composable
fun ScaffoldExample(onButtonClick: (Int) -> Unit, onSpeak: (String) -> Unit) {
    Scaffold(
        topBar = {
            MyTopAppBar(
                title = "Sonifikacja ale ze Scaffoldem",
                onSpeak = onSpeak
            )
        },
        content = { innerPadding ->
            MainContent(
                modifier = Modifier.padding(innerPadding),
                onButtonClick = onButtonClick,
                onSpeak = onSpeak
            )
        },
        bottomBar = {
            MyBottomAppBar()
        },
    )
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyTopAppBar(onSpeak : (String) -> Unit, title: String){
    TopAppBar(
        colors = topAppBarColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer,
            titleContentColor = MaterialTheme.colorScheme.primary
        ),
        title = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    text = title
                )
            }
        },
        actions = {
            IconButton(onClick = {
                onSpeak(title)
            }) {
                Icon(Icons.Default.Warning, contentDescription = "----")
            }
        },
    )
}

@Composable
fun MainContent(
    modifier : Modifier = Modifier,
    onButtonClick : (Int) -> Unit,
    onSpeak : (String) -> Unit
){
    var text by remember { mutableStateOf("Cześć, Mam na imie Michał") }
    Column(
        modifier = modifier.fillMaxSize()
    ) {


        Box(
            modifier = Modifier.weight(1f).fillMaxWidth(),
            contentAlignment = Alignment.Center
        ){
            ButtonExample(onClick = onButtonClick)
        }
        Box(
            modifier = Modifier.weight(2f).fillMaxWidth(),
            contentAlignment = Alignment.Center
        ){
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                OutlinedTextFieldCom(
                    text = text,
                    onTextChange = { text = it}
                )
                ReadButton(onClick = { onSpeak(text)})
            }
        }

    }
}

@Composable
fun ReadButton(onClick : () -> Unit) {
    Box(
        modifier = Modifier.padding(8.dp)
    ){
        Button(onClick = onClick) {
            Text("Przeczytaj")
        }
    }
}


@Composable
fun OutlinedTextFieldCom(
    text: String,
    onTextChange: (String) -> Unit
) {
    OutlinedTextField(
        value = text,
        onValueChange = onTextChange,
        label = { Text("Label") },
        modifier = Modifier.fillMaxWidth().padding(22.dp, 4.dp)
    )
}

@Composable
fun ButtonExample(onClick: (Int) -> Unit) {
    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ){
        items(4) { index ->
            FilledTonalButton(onClick = {
                onClick(index)
            }){
                Text(text = "$index")
            }
        }
    }
}

@Composable
fun MyBottomAppBar() {
    BottomAppBar(
        containerColor = MaterialTheme.colorScheme.primaryContainer,
        contentColor = MaterialTheme.colorScheme.primary,
    ){
        Text(
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center,
            text = "Nawigacja in progress"
        )
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    MyApp {
        ScaffoldExample(
            onButtonClick = { index ->
                println("Button clicked: $index")
            },
            onSpeak = { text ->
                println("Speak text: $text")
            }
        )
    }
}