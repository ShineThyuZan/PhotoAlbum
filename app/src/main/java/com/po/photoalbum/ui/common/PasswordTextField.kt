package com.po.photoalbum.ui.common

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActionScope
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.po.photoalbum.R
import com.po.photoalbum.ui.theme.resources.dimen

@Composable
fun PasswordTextField(
    modifier: Modifier = Modifier,
    placeholder: String = "Placeholder",
    password: String = "Value",
    onValueChanged: (String) -> Unit,
    keyboardType: KeyboardType = KeyboardType.Number,
    imeAction: ImeAction = ImeAction.Done,
    keyboardAction: (KeyboardActionScope) -> Unit,
    isError: Boolean,
    errorMessage: String
) {
    var passwordVisibility by remember {
        mutableStateOf(false)
    }
    val icon =
        if (passwordVisibility) {
            painterResource(id = R.drawable.ic_small_eye_off)
        } else {
            painterResource(id = R.drawable.ic_small_eye)
        }

    Column(modifier = modifier.fillMaxWidth()) {
        Row(
            modifier = modifier
                .fillMaxWidth()
                .height(MaterialTheme.dimen.base_7x)
                .clip(
                    RoundedCornerShape(MaterialTheme.dimen.base_2x)
                )
                .border(
                    width = 1.dp,
                    color = if (isError) {
                        MaterialTheme.colorScheme.error
                    } else {
                        MaterialTheme.colorScheme.outline
                    },
                    shape = RoundedCornerShape(MaterialTheme.dimen.base_2x)
                )
                .background(
                    color = MaterialTheme.colorScheme.surface
                ),
            verticalAlignment = Alignment.CenterVertically
        ) {
            BasicTextField(
                modifier = modifier
                    .wrapContentHeight(align = Alignment.CenterVertically)
                    .padding(start = MaterialTheme.dimen.base_2x)
                    .weight(1f),
                value = password,
                onValueChange = {
                    if (it.length <= 10) {
                        onValueChanged(it)
                    }
                },
                textStyle = MaterialTheme.typography.bodyLarge.copy(
                    color = MaterialTheme.colorScheme.onSurface
                ),
                decorationBox = { innerTextField ->
                    if (password.isEmpty()) {
                        Text(
                            text = placeholder,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            style = MaterialTheme.typography.bodyLarge
                        )
                    }
                    innerTextField()
                },
                singleLine = true,
                keyboardOptions = KeyboardOptions(
                    keyboardType = keyboardType,
                    imeAction = imeAction
                ),
                keyboardActions = KeyboardActions(
                    onDone = keyboardAction
                ),
                visualTransformation = if (passwordVisibility) VisualTransformation.None else PasswordVisualTransformation(),
                cursorBrush = SolidColor(
                    value = MaterialTheme.colorScheme.primary
                )
            )

            IconButton(onClick = {
                passwordVisibility = !passwordVisibility
            }) {
                Icon(
                    painter = icon,
                    contentDescription = "Close Text",
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        if (isError) {
            VisibilityAnimator(
                isVisible = isError,
                errorMessage = errorMessage
            )
        }
    }
}