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
import androidx.compose.ui.unit.sp
import com.manickchand.changepassword.ui.theme.ChangePasswordTheme
import java.security.SecureRandom

class MainActivity : ComponentActivity() {

    companion object {
        const val REQUEST_CODE_ENABLE_ADMIN = 1
    }

    private lateinit var adminComponent: ComponentName
    private lateinit var devicePolicyManager: DevicePolicyManager
    private var hasPermission: Boolean = false
    val token = SecureRandom.getInstance("SHA1PRNG").generateSeed(32)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        devicePolicyManager =
            getSystemService(Context.DEVICE_POLICY_SERVICE) as DevicePolicyManager

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
                        textTitle(getString(R.string.app_name), modifier = Modifier)
                        textDescription(getString(R.string.txt_decription), modifier = Modifier)

                        buttonChangePassword(getString(R.string.button_permission), Modifier) {
                            if (hasPermission.not()) {
                                adminComponent =
                                    ComponentName(
                                        this@MainActivity,
                                        MyDeviceAdminReceiver::class.java
                                    )
                                val intent =
                                    Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN).apply {
                                        putExtra(
                                            DevicePolicyManager.EXTRA_DEVICE_ADMIN,
                                            adminComponent
                                        )
                                    }
                                startActivityForResult(intent, REQUEST_CODE_ENABLE_ADMIN)
                            } else {
                                Toast.makeText(
                                    this@MainActivity,
                                    "Permission Done",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }

                        buttonChangePassword(getString(R.string.button_change_text), Modifier) {

                            if (hasPermission) {
                                changePassword("1234")
                            } else {
                                Toast.makeText(
                                    this@MainActivity,
                                    "Check the permission",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }

                        buttonChangePassword(getString(R.string.button_block_screen), Modifier) {
                            if (hasPermission) {
                                devicePolicyManager.lockNow()
                            } else {
                                Toast.makeText(
                                    this@MainActivity,
                                    "Check the permission",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                    }

                }
            }
        }
    }

    private fun changePassword(password: String?){

        try {

            devicePolicyManager.setResetPasswordToken(adminComponent, token);
            devicePolicyManager.resetPasswordWithToken(
                adminComponent,
                password,
                token,
                0
            )
        }catch (e: Exception){
            e.printStackTrace()
            Toast.makeText(this, "Erro ao alterar senha, verifique se a senha ja esta setada.", Toast.LENGTH_SHORT).show()
        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == REQUEST_CODE_ENABLE_ADMIN) {
            if (resultCode == Activity.RESULT_OK) {
                hasPermission = devicePolicyManager.isAdminActive(adminComponent)
            } else {
                hasPermission = false
                Toast.makeText(this, "Admin not enabled", Toast.LENGTH_SHORT).show()
            }
        }
    }
}

@Composable
fun textDescription(text: String, modifier: Modifier = Modifier) {
    Text(
        text = text,
        modifier = modifier
    )
}

@Composable
fun textTitle(text: String, modifier: Modifier = Modifier) {
    Text(
        text = text,
        modifier = modifier,
        fontSize = 20.sp
    )
}

@Composable
fun buttonChangePassword(text: String, modifier: Modifier = Modifier, onClick: () -> Unit) {
    Button(onClick = {
        onClick.invoke()
    }) {
        Text(text = text)
    }
}

@Preview(showBackground = true)
@Composable
fun textDescriptionPreview() {
    textDescription("Texto")
}

@Preview(showBackground = true)
@Composable
fun buttonPreview() {
    buttonChangePassword("Mudar") {}
}