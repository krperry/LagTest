package com.orbitresearch.lagtest

//import android.content.res.AssetManager



import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.orbitresearch.lagtest.ui.theme.LagTestTheme
import android.speech.tts.TextToSpeech
import android.util.Log
import android.view.KeyEvent
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStream
import java.io.InputStreamReader
import java.util.*



class MainActivity : ComponentActivity(),TextToSpeech.OnInitListener  {
    private var tts :TextToSpeech? = null
    private var index : Int =0
    private var charIndex: Int  =0
    private var lines = mutableListOf<String>()
private var loaded: Boolean = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            LagTestTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Greeting("Android")
                }
            }
        }
        // TextToSpeech(Context: this, OnInitListener: this)
        tts = TextToSpeech(this, this)



        }

    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) {
            val result = tts!!.setLanguage(Locale.US)
            if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                Log.e("TTS","The Language not supported!")
            }
            else {
                // Choose a directory using the system's file picker.

tts!!.speak("Started",TextToSpeech.QUEUE_FLUSH,null,"")
            }
        }
    }



public         override fun onDestroy() {
            // Shutdown TTS when
            // activity is destroyed
            if (tts != null) {
                tts!!.stop()
                tts!!.shutdown()
            }
            super.onDestroy()
        }

    private  fun charForTts(s:String):String{
        return when (s) {
            " " ->{
                "space"
        }
            "\n" -> {
                "new line"
        }
            else -> {
                s
            }
        }

    }


    override fun onKeyUp(keyCode: Int, event: KeyEvent): Boolean {
        return when (keyCode) {
            KeyEvent.KEYCODE_A  -> {
                if(loaded and lines.isNotEmpty()){
                    charIndex-=1
                    if ((charIndex < 0) and (index > 0)){
                            index -= 1
                            charIndex = (lines[index]).length - 1
                        }else if (charIndex < 0){
                            charIndex=0
                        }
                            tts!!.speak(charForTts((lines[index]).substring(charIndex,charIndex+1)) ,TextToSpeech.QUEUE_FLUSH,null,"")
            }else{
                tts!!.speak("Left", TextToSpeech.QUEUE_FLUSH, null, "")
            }
                true
            }

            KeyEvent.KEYCODE_D-> {
                if(loaded and lines.isNotEmpty()){
                    charIndex+=1
                    if((charIndex >= (lines[index]).length-1) and (index < lines.size-1)){
                            index += 1
                            charIndex = 0
                        }else if (charIndex<0){
                        charIndex= (lines[index]).length-1
                    }
                    tts!!.speak(charForTts((lines[index]).substring(charIndex,charIndex+1)) ,TextToSpeech.QUEUE_FLUSH,null,"")
                }else {
                    tts!!.speak("Right", TextToSpeech.QUEUE_FLUSH, null, "")
                }
                true
                }

            KeyEvent.KEYCODE_W -> {
                if (loaded and lines.isNotEmpty()){
                index -= 1
                    if (index < 0) {
                        index = 0
                    }
                    if (charIndex > (lines[index]).length-1){
                        charIndex = (lines[index]).length-1
                    }
                    if (charIndex <0){
                        charIndex=0
                    }

                tts!!.speak(lines[index], TextToSpeech.QUEUE_FLUSH, null, "")
            }else {
                tts!!.speak("Up",TextToSpeech.QUEUE_FLUSH,null,"")
            }

            true
            }

            KeyEvent.KEYCODE_L -> {
//                Thread {
                    val myInputStream: InputStream

                    // Try to open the text file, reads
                    // the data and stores it in the string
                    try {
                        myInputStream = assets.open("bible.txt")

                        BufferedReader(InputStreamReader(myInputStream)).use { reader ->
                            var line: String? = reader.readLine()
                            while (line != null) {
                                lines.add(line)
                                line = reader.readLine()
                            }
                        }

                    } catch (e: IOException) {
                        // Exception
                        e.printStackTrace()
                    }
                    loaded=true
//                    runOnUiThread {
                    if (loaded and lines.isNotEmpty()) {
                        tts!!.speak(lines[index], TextToSpeech.QUEUE_FLUSH, null, "")
                        } else {
                            tts!!.speak("File empty", TextToSpeech.QUEUE_FLUSH, null, "")
                        }

//                    }
//                }.start()
                true
            }


            KeyEvent.KEYCODE_S -> {
            if(loaded and lines.isNotEmpty()) {
                    index += 1
                    if (index >= lines.size - 1) {
                        index = lines.size - 1
                    }
                if (charIndex > (lines[index]).length-1){
                    charIndex = (lines[index]).length-1
                }
                if (charIndex <0){
                    charIndex=0
                }

                    tts!!.speak(lines[index], TextToSpeech.QUEUE_FLUSH, null, "")
                }else {
                    tts!!.speak("down",TextToSpeech.QUEUE_FLUSH,null,"")
            }
                true
            }

            else -> super.onKeyUp(keyCode, event)
        }
    }



}//end class

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    LagTestTheme {
        Greeting("Android")
    }
}



