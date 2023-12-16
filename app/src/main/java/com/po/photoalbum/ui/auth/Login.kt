package com.po.photoalbum.ui.auth

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.po.photoalbum.R
import com.po.photoalbum.ui.common.CommonTextField
import com.po.photoalbum.ui.common.CustomTopBar
import com.po.photoalbum.ui.common.CustomVerticalSpacer
import com.po.photoalbum.ui.common.PasswordTextField
import com.po.photoalbum.ui.theme.resources.dimen

@Composable
fun LoginScreen(
    loginViewModel: LoginViewModel? = null,
    onNavToHomePage: () -> Unit,
    onNavToSignUpPage: () -> Unit
) {
    val loginUiState = loginViewModel?.loginUiState
    val isError = loginUiState?.loginError
    val context = LocalContext.current
    val keyboardController = LocalSoftwareKeyboardController.current
    val snackState = remember { SnackbarHostState() }
    Scaffold(
        contentWindowInsets = WindowInsets(
            bottom = WindowInsets.navigationBars.asPaddingValues().calculateBottomPadding()
        ),
        topBar = {
            CustomTopBar(
                onNavigation = {},
                onActions = {
                    TextButton(
                        modifier = Modifier.padding(end = MaterialTheme.dimen.small),
                        onClick = {
                            onNavToSignUpPage.invoke()
                        }
                    ) {
                        Text(
                            text = "Sign Up",
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                },
                background = Color.Transparent,
                title = "Login",
            )
        },
        snackbarHost = {
            SnackbarHost(hostState = snackState)
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
                .fillMaxWidth()
                .imePadding()
                .padding(MaterialTheme.dimen.base_2x),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                CommonTextField(
                    modifier = Modifier
                        .fillMaxWidth(),
                    placeholder = "Email",
                    value = loginUiState?.userName ?: "",
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Next,
                    onValueChanged = {
                        loginViewModel?.onUserNameChange(it)
                    },
                    onValueCleared = {
                        loginViewModel?.onUserNameClear()
                    },
                    isError = isError ?: false,
                    errorMessage = "Please type correct Email name"
                )
                CustomVerticalSpacer(size = MaterialTheme.dimen.base_2x)
                PasswordTextField(
                    onValueChanged = {
                        loginViewModel?.onPasswordNameChange(it)
                    },
                    keyboardAction = { keyboardController!!.hide() },
                    isError = isError ?: false,
                    errorMessage = isError.toString(),
                    password = loginUiState?.password ?: "",
                    imeAction = ImeAction.Done,
                    keyboardType = KeyboardType.Password,
                )

                Spacer(modifier = Modifier.size(16.dp))
                    Button(
                    onClick = {
                        loginViewModel?.loginUser(context)
                    },
                    shape = CircleShape
                ) {
                    Text(text = "Login")
                    CustomVerticalSpacer(size = MaterialTheme.dimen.base)
                    Icon(
                        painter = painterResource(id = R.drawable.ic_small_chevron_right),
                        contentDescription = "chevron right"
                    )
                }

                if (loginUiState?.isLoading == true) {
                    CircularProgressIndicator()
                }
                LaunchedEffect(key1 = loginViewModel?.hasUser) {
                    if (loginViewModel?.hasUser == true) {
                        onNavToHomePage.invoke()
                    }
                }
            }
        }
    }
}

@Preview
@Composable
fun loginPreview() {
    Surface {
        LoginScreen(onNavToHomePage = { /*TODO*/ }) {}
    }
}
