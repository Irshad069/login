package com.login.ui.screen.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.login.R

@Composable
fun CustomOutlinedTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    modifier: Modifier = Modifier,
    isError: Boolean = false,
    errorText: String? = null,
    visualTransformation: VisualTransformation = PasswordVisualTransformation(),
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    isReadOnly: Boolean = false,
    trailingIcon: @Composable (() -> Unit)? = null
) {
    Column(modifier = modifier) {
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            label = { Text(label) },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(8.dp),
            keyboardOptions = keyboardOptions,
            visualTransformation = visualTransformation,
            readOnly = isReadOnly,
            trailingIcon = trailingIcon,
            colors = OutlinedTextFieldDefaults.colors(
                unfocusedTextColor = colorResource(id = R.color.text_color),
                unfocusedBorderColor = Color.White,
                unfocusedLabelColor = colorResource(id = R.color.text_color),
                focusedTextColor = colorResource(id = R.color.text_color),
                focusedBorderColor = colorResource(id = R.color.primary_color),
                focusedLabelColor = colorResource(id = R.color.primary_color),
                cursorColor = colorResource(id = R.color.primary_color),
                focusedContainerColor = colorResource(id = R.color.container_color),
                unfocusedContainerColor = colorResource(id = R.color.container_color)
            )
        )
        if (isError && errorText != null) {
            Text(
                text = errorText,
                color = Color.Red,
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewCustomOutlinedTextField() {
    CustomOutlinedTextField(
        value = "",
        onValueChange = {},
        label = "Email",
        isError = true,
        errorText = "Invalid email"
    )
}
