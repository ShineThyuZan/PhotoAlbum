package com.po.photoalbum.ui.common

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActionScope
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.po.photoalbum.R
import com.po.photoalbum.ui.theme.resources.dimen

@Composable
fun CommonTextField(
    modifier: Modifier = Modifier,
    textFieldLabel: String = "",
    placeholder: String,
    value: String = "",
    isShowInputCount: Boolean = false,
    inputCount: Int = 0,
    bioCountLimit: Int = 150,
    onValueChanged: (String) -> Unit = {},
    keyboardType: KeyboardType = KeyboardType.Text,
    imeAction: ImeAction = ImeAction.Go,
    onValueCleared: () -> Unit = {},
    isError: Boolean = false,
    errorMessage: String = "",
    keyboardAction: (KeyboardActionScope) -> Unit = {}
) {
    Column(modifier = modifier.fillMaxWidth()) {
        if (textFieldLabel.isNotEmpty()) {
            Text(
                modifier = modifier.padding(
                    vertical = MaterialTheme.dimen.small,
                    horizontal = MaterialTheme.dimen.base
                ),
                text = textFieldLabel,
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.onSurface
            )
        }
        Row(
            modifier = modifier
                .fillMaxWidth()
                .defaultMinSize(minHeight = MaterialTheme.dimen.base_7x)
                .clip(RoundedCornerShape(MaterialTheme.dimen.base_2x))
                .border(
                    width = 1.dp,
                    color = if (isError) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.outline,
                    shape = RoundedCornerShape(MaterialTheme.dimen.base_2x)
                )
                .background(
                    color = MaterialTheme.colorScheme.surface
                ),
            verticalAlignment = Alignment.CenterVertically
        ) {
            CustomHorizontalSpacer(size = MaterialTheme.dimen.base_2x)
            BasicTextField(
                modifier = modifier
                    .fillMaxWidth()
                    .padding(vertical = MaterialTheme.dimen.base)
                    .weight(1f, fill = false),
                value = value,
                onValueChange = {
                    if (isShowInputCount) {
                        if (it.length <= bioCountLimit) {
                            onValueChanged(it)
                        }
                    } else {
                        if (it.length <= 30) {
                            onValueChanged(it)
                        }
                    }
                },
                textStyle = MaterialTheme.typography.bodyLarge.copy(
                    color = MaterialTheme.colorScheme.onSurface
                ),
                decorationBox = { innerTextField ->
                    if (value.isEmpty()) {
                        Text(
                            text = placeholder,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            style = MaterialTheme.typography.bodyLarge
                        )
                    }
                    innerTextField()
                },
                singleLine = !isShowInputCount,
                keyboardOptions = KeyboardOptions(
                    keyboardType = keyboardType,
                    imeAction = imeAction
                ),
                keyboardActions = KeyboardActions(
                    onDone = keyboardAction,
                    onGo = keyboardAction,
                    onNext = keyboardAction,
                    onPrevious = keyboardAction,
                    onSearch = keyboardAction,
                    onSend = keyboardAction
                ),
                cursorBrush = SolidColor(
                    value = MaterialTheme.colorScheme.primary
                )
            )
            if (value.isNotEmpty()) {
                IconButton(onClick = onValueCleared) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_cross),
                        contentDescription = "Trailing Icon",
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
        Row {
            VisibilityAnimator(
                isVisible = isError,
                errorMessage = errorMessage
            )
            if (isShowInputCount) {
                Text(
                    modifier = modifier
                        .fillMaxWidth()
                        .padding(
                            vertical = MaterialTheme.dimen.small,
                            horizontal = MaterialTheme.dimen.base
                        ),
                    text = stringResource(
                        id = R.string.input_limit,
                        inputCount,
                        bioCountLimit
                    ),
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    textAlign = TextAlign.End
                )
            }
        }
    }
}

@Composable
@Preview
private fun ChatTextFieldPreview() {
    Surface {
        CommonTextField(
            placeholder = "Placeholder",
            value = "",
            onValueChanged = {},
            onValueCleared = { },
            isError = false,
            errorMessage = ""
        )
    }
}

@Composable
@Preview
private fun ChatTextFieldErrorPreview() {
    Surface {
        CommonTextField(
            placeholder = "Placeholder",
            value = "Text Data",
            onValueChanged = {},
            onValueCleared = { },
            isError = true,
            errorMessage = "Error message"
        )
    }
}
