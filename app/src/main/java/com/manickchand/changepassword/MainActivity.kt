package com.manickchand.changepassword

import android.app.Activity
import android.app.admin.DevicePolicyManager
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.manickchand.changepassword.ui.theme.ChangePasswordTheme

class MainActivity : ComponentActivity() {

    companion object {
        const val REQUEST_CODE_ENABLE_ADMIN = 1
    }

    private lateinit var adminComponent: ComponentName

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            ChangePasswordTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp),
                    ) {
                        startText(modifier = Modifier)
                        buttonChangePassword(Modifier) {
                            adminComponent =
                                ComponentName(this@MainActivity, MyDeviceAdminReceiver::class.java)
                            // Request device admin privileges
                            val intent = Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN).apply {
                                putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, adminComponent)
                            }
                            startActivityForResult(intent, REQUEST_CODE_ENABLE_ADMIN)
                        }
                    }

                }
            }
        }
    }

    private fun byteArrayOfInts(vararg ints: Int) =
        ByteArray(ints.size) { pos -> ints[pos].toByte() }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == REQUEST_CODE_ENABLE_ADMIN) {
            if (resultCode == Activity.RESULT_OK) {

                val devicePolicyManager =
                    getSystemService(Context.DEVICE_POLICY_SERVICE) as DevicePolicyManager
                if (devicePolicyManager.isAdminActive(adminComponent)) {

                    val token = byteArrayOfInts(
                        0xA1,
                        0x2E,
                        0x38,
                        0xD4,
                        0x89,
                        0xC3,
                        0xA1,
                        0x2E,
                        0x38,
                        0xD4,
                        0x89,
                        0xC3,
                        0xA1,
                        0x2E,
                        0x38,
                        0xD4,
                        0x89,
                        0xC3,
                        0xA1,
                        0x2E,
                        0x38,
                        0xD4,
                        0x89,
                        0xC3,
                        0xA1,
                        0x2E,
                        0x38,
                        0xD4,
                        0x89,
                        0xC3,
                        0xA1,
                        0x2E,
                        0x38,
                        0xD4,
                        0x89,
                        0xC3,
                        0xA1,
                        0x2E,
                        0x38,
                        0xD4,
                        0x89,
                        0xC3
                    )

                    devicePolicyManager.setResetPasswordToken(adminComponent, token);
                    devicePolicyManager.resetPasswordWithToken(adminComponent, "1234", token, 0)
                    //devicePolicyManager.lockNow()
                }
            } else {
                Toast.makeText(this, "Admin not enabled", Toast.LENGTH_SHORT).show()
            }
        }
    }
}

@Composable
fun startText(modifier: Modifier = Modifier) {
    Text(
        text = "Clique para alterar a senha",
        modifier = modifier
    )
}

@Composable
fun buttonChangePassword(modifier: Modifier = Modifier, onClick: () -> Unit) {
    Button(onClick = {
        onClick.invoke()
    }) {
        Text("Setar senha")
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    startText()
    buttonChangePassword() {

    }

}