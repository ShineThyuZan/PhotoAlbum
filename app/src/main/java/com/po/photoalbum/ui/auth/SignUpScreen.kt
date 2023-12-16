package com.po.photoalbum.ui.auth

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
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
import com.po.photoalbum.R
import com.po.photoalbum.ui.common.CommonTextField
import com.po.photoalbum.ui.common.CustomTopBar
import com.po.photoalbum.ui.common.CustomVerticalSpacer
import com.po.photoalbum.ui.common.PasswordTextField
import com.po.photoalbum.ui.theme.resources.PhotoAlbumTheme
import com.po.photoalbum.ui.theme.resources.dimen

@Composable
fun SignUpScreen(
    loginViewModel: LoginViewModel? = null,
    onNavToHomePage: () -> Unit,
    onNavToLoginPage: () -> Unit
) {
    val loginUiState = loginViewModel?.loginUiState
    val isErrorPwdNotMatch = loginUiState?.signUpError
    val isErrorEmailInvalidFormat = loginUiState?.errorSignUpInvalidEmail
    val context = LocalContext.current
    val snackState = remember { SnackbarHostState() }
    val keyboardController = LocalSoftwareKeyboardController.current
    val signUpBtnState = remember {
        mutableStateOf(false)
    }

    if (loginUiState!!.userNameSignUp.isNotBlank() &&
        loginUiState.passwordSignUp.isNotBlank() &&
        loginUiState.confirmPasswordSignUp.isNotBlank() &&
        (loginUiState.passwordSignUp.length >= 9) &&
        (loginUiState.confirmPasswordSignUp.length >= 9)
    ) {
        signUpBtnState.value = true
    }
    LaunchedEffect(key1 = loginViewModel.hasUser) {
        if (loginViewModel.hasUser) {
            onNavToHomePage.invoke()
        }
    }

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
                            onNavToLoginPage.invoke()
                        }
                    ) {
                        Text(
                            text = "Login",
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                },
                background = Color.Transparent,
                title = "Sign Up",
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
                CustomVerticalSpacer(size = MaterialTheme.dimen.base_2x)

                CommonTextField(
                    modifier = Modifier
                        .fillMaxWidth(),
                    placeholder = "Email",
                    value = loginUiState.userNameSignUp,
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Next,
                    onValueChanged = {
                        loginViewModel.onUserNameSignUpChange(it)
                    },
                    onValueCleared = {},
                    isError = isErrorEmailInvalidFormat ?: false,
                    errorMessage = "Sample name is abc@gmail.com"
                )

                CustomVerticalSpacer(size = MaterialTheme.dimen.base_2x)

                PasswordTextField(
                    onValueChanged = {
                        loginViewModel.onPasswordSignUpChange(it)
                    },
                    keyboardAction = { keyboardController!!.hide() },
                    isError = isErrorPwdNotMatch ?: false,
                    errorMessage = "Password and Confirm Password does not match.",
                    password = loginUiState.passwordSignUp,
                    imeAction = ImeAction.Done,
                    keyboardType = KeyboardType.Password,
                    placeholder = "Type Password"
                )

                CustomVerticalSpacer(size = MaterialTheme.dimen.base_2x)

                PasswordTextField(
                    onValueChanged = {
                        loginViewModel.onConfirmPasswordChange(it)
                    },
                    keyboardAction = { keyboardController!!.hide() },
                    isError = isErrorPwdNotMatch ?: false,
                    errorMessage = "Password and Confirm Password does not match.",
                    password = loginUiState.confirmPasswordSignUp,
                    imeAction = ImeAction.Done,
                    keyboardType = KeyboardType.Password,
                    placeholder = "Confirm Password"
                )

                CustomVerticalSpacer(size = MaterialTheme.dimen.base_2x)

                Button(
                    onClick = {
                        loginViewModel.createUser(context)
                    },
                    enabled = signUpBtnState.value
                ) {
                    Text(text = "Sign Up")
                    CustomVerticalSpacer(size = MaterialTheme.dimen.base)
                    Icon(
                        painter = painterResource(id = R.drawable.ic_small_chevron_right),
                        contentDescription = "chevron right"
                    )
                }

                if (loginUiState.isLoading) {
                    CircularProgressIndicator()
                }
            }
        }
    }
}

@Preview(showSystemUi = true)
@Composable
fun SignUpScreen() {
    PhotoAlbumTheme {
        SignUpScreen()
    }
}
