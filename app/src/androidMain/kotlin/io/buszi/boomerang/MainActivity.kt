package io.buszi.boomerang

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import kotlin.jvm.java

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Column {
                Button({
                    startActivity(
                        Intent(
                            this@MainActivity,
                            FullComposePreviewActivity::class.java
                        )
                    )
                }) {
                    Text("Start full compose test")
                }
                Button({
                    startActivity(
                        Intent(
                            this@MainActivity,
                            FullFragmentPreviewActivity::class.java
                        )
                    )
                }) {
                    Text("Start full fragment test")
                }
            }
        }
    }
}
