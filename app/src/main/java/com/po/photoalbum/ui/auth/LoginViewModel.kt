package com.po.photoalbum.ui.auth

import android.content.Context
import android.util.Patterns
import android.widget.Toast
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class LoginViewModel(
    private val repository: AuthRepository = AuthRepository()
) : ViewModel() {
    val currentUser = repository.currentUser
    val hasUser: Boolean
        get() = repository.hasUser()

    var loginUiState by mutableStateOf(LoginUiState())
        private set

    fun onUserNameChange(userName: String) {
        loginUiState = loginUiState.copy(
            userName = userName
        )
    }

    fun onUserNameClear() {
        loginUiState = loginUiState.copy(
            userName = ""
        )
    }

    fun onPasswordNameChange(password: String) {
        loginUiState = loginUiState.copy(
            password = password
        )
    }

    fun onUserNameSignUpChange(userName: String) {
        loginUiState = loginUiState.copy(
            userNameSignUp = userName
        )
    }

    fun onPasswordSignUpChange(password: String) {
        loginUiState = loginUiState.copy(
            passwordSignUp = password
        )
    }

    fun onConfirmPasswordChange(password: String) {
        loginUiState = loginUiState.copy(
            confirmPasswordSignUp = password
        )
    }

    private fun validateLoginForm() =
        loginUiState.userName.isNotBlank() && loginUiState.password.isNotBlank()

    fun validateSignUpForm() =
        loginUiState.userNameSignUp.isNotBlank() &&
                loginUiState.passwordSignUp.isNotBlank() &&
                loginUiState.confirmPasswordSignUp.isNotBlank()

    private fun validatePwNotMatch(): Boolean {
        return loginUiState.passwordSignUp != loginUiState.confirmPasswordSignUp
    }

   private fun isEmailValid(): Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(loginUiState.userNameSignUp).matches()
    }

    fun createUser(context: Context) = viewModelScope.launch {
        if (isEmailValid()) {
            if (!validatePwNotMatch()) {
                loginUiState = loginUiState.copy(
                    isLoading = true
                )
                delay(2000L)
                repository.createUser(
                    loginUiState.userNameSignUp,
                    loginUiState.passwordSignUp
                ) { isSuccessful ->
                    loginUiState = if (isSuccessful) {
                        Toast.makeText(context, "success Login", Toast.LENGTH_SHORT).show()
                        loginUiState.copy(
                            isSuccessLogin = true,
                            isLoading = false,
                            signUpError = false,

                            )
                    } else {
                        Toast.makeText(context, "fail Login", Toast.LENGTH_SHORT).show()
                        loginUiState.copy(
                            isSuccessLogin = false,
                            isLoading = false,
                            signUpError = false,
                        )
                    }
                }
            } else {
                loginUiState = loginUiState.copy(
                    signUpError = true
                )
            }
        } else {
            loginUiState = loginUiState.copy(
                errorSignUpInvalidEmail = true
            )
        }
    }

    fun loginUser(context: Context) = viewModelScope.launch {
        /* try {*/
        /*      if (!validateLoginForm()) {
                  throw IllegalArgumentException("email and password cannot empty")
              }
              loginUiState = loginUiState.copy(
                  isLoading = true
              )
              loginUiState = loginUiState.copy(
                  loginError = true
              )*/
        if (validateLoginForm()) {
            repository.login(
                loginUiState.userName,
                loginUiState.password
            ) { isSuccessful ->
                loginUiState = if (isSuccessful) {
                    Toast.makeText(context, "success Login", Toast.LENGTH_SHORT).show()
                    loginUiState.copy(
                        isSuccessLogin = true,
                        loginError = false,
                    )
                } else {
                    Toast.makeText(context, "fail Login", Toast.LENGTH_SHORT).show()
                    loginUiState.copy(
                        isSuccessLogin = false,
                        loginError = false,
                    )
                }
            }
        } else {
            loginUiState = loginUiState.copy(
                loginError = true
            )
        }
        /*    } catch (e: Exception) {
                loginUiState = loginUiState.copy(
                    loginError = true
                )
                e.printStackTrace()
            } finally {
                loginUiState = loginUiState.copy(
                    isLoading = false
                )
            }*/
    }
}

data class LoginUiState(
    val userName: String = "",
    val password: String = "",
    val userNameSignUp: String = "",
    val passwordSignUp: String = "",
    val confirmPasswordSignUp: String = "",
    val isLoading: Boolean = false,
    val isSuccessLogin: Boolean = false,
    val signUpError: Boolean? = false,
    val errorSignUpInvalidEmail: Boolean? = false,
    val loginError: Boolean? = false,
)