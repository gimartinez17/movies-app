package com.gmart.gmovies.ui.composable

import androidx.compose.animation.core.CubicBezierEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.SearchBarDefaults.TonalElevation
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.structuralEqualityPolicy
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.semantics.onClick
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.gmart.gmovies.utils.PreviewLayout
import com.gmart.gmovies.utils.ThemePreviews

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppSearchBar(
    modifier: Modifier = Modifier,
    query: String,
    active: Boolean = false,
    enabled: Boolean = true,
    onQueryChange: (String) -> Unit = {},
    onSearch: (String) -> Unit = {},
    onActiveChange: (Boolean) -> Unit = {},
    placeholder: @Composable (() -> Unit)? = null,
    leadingIcon: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
    content: @Composable (ColumnScope.() -> Unit) = {},
) {
    val density = LocalDensity.current
    val focusRequester = remember { FocusRequester() }
    val interactionSource: MutableInteractionSource = remember { MutableInteractionSource() }
    val animationProgress: State<Float> = animateFloatAsState(
        targetValue = if (active) 1f else 0f,
        animationSpec = if (active) tween(
            durationMillis = 600,
            delayMillis = 100,
            easing = CubicBezierEasing(0.05f, 0.7f, 0.1f, 1.0f),
        ) else tween(
            durationMillis = 350,
            delayMillis = 100,
            easing = CubicBezierEasing(0.0f, 1.0f, 0.0f, 1.0f),
        ), label = ""
    )
    val unconsumedInsets = remember { MutableWindowInsets() }
    val topPadding = remember(density) {
        derivedStateOf {
            8.dp + unconsumedInsets.asPaddingValues(density).calculateTopPadding()
        }
    }
    Column(modifier = modifier) {
        Surface(
            modifier = Modifier.padding(bottom = topPadding.value),
            shape = SearchBarDefaults.inputFieldShape,
            color = SearchBarDefaults.colors().containerColor,
            tonalElevation = TonalElevation
        ) {
            SearchBarInputField(
                query = query,
                onQueryChange = onQueryChange,
                focusRequester = focusRequester,
                onActiveChange = onActiveChange,
                enabled = enabled,
                interactionSource = interactionSource,
                placeholder = placeholder,
                leadingIcon = leadingIcon,
                trailingIcon = trailingIcon,
                onSearch = onSearch
            )
        }
        val showResults by remember {
            derivedStateOf(structuralEqualityPolicy()) { animationProgress.value > 0 }
        }
        if (showResults) {
            Column(modifier = Modifier.padding(horizontal = 4.dp)) {
                content()
            }
        }

    }
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
private fun SearchBarInputField(
    query: String,
    onQueryChange: (String) -> Unit,
    focusRequester: FocusRequester,
    onActiveChange: (Boolean) -> Unit,
    enabled: Boolean,
    interactionSource: MutableInteractionSource,
    placeholder: @Composable (() -> Unit)?,
    leadingIcon: @Composable (() -> Unit)?,
    trailingIcon: @Composable (() -> Unit)?,
    onSearch: (String) -> Unit
) {
    BasicTextField(
        value = query,
        onValueChange = onQueryChange,
        modifier = Modifier
            .zIndex(1f)
            .height(SearchBarDefaults.InputFieldHeight)
            .fillMaxWidth()
            .focusRequester(focusRequester)
            .onFocusChanged { if (it.isFocused) onActiveChange(true) }
            .semantics {
                onClick {
                    focusRequester.requestFocus()
                    true
                }
            },
        enabled = enabled,
        singleLine = true,
        textStyle = LocalTextStyle.current.merge(TextStyle(color = MaterialTheme.colorScheme.onSurface)),
        cursorBrush = SolidColor(MaterialTheme.colorScheme.primary),
        decorationBox = @Composable { innerTextField ->
            TextFieldDefaults.DecorationBox(
                value = query,
                innerTextField = innerTextField,
                enabled = enabled,
                singleLine = true,
                visualTransformation = VisualTransformation.None,
                interactionSource = interactionSource,
                placeholder = placeholder,
                leadingIcon = leadingIcon?.let {
                    { Box(Modifier.offset(x = 4.dp)) { leadingIcon() } }
                },
                trailingIcon = trailingIcon?.let {
                    { Box(Modifier.offset(x = (-4).dp)) { trailingIcon() } }
                },
                shape = SearchBarDefaults.inputFieldShape,
                colors = SearchBarDefaults.inputFieldColors(),
                //contentPadding = TextFieldDefaults.contentPaddingWithoutLabel(),
                container = {},
            )
        },
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
        keyboardActions = KeyboardActions(onSearch = { onSearch(query) }),
    )
}

private class MutableWindowInsets(
    initialInsets: WindowInsets = WindowInsets(0, 0, 0, 0)
) : WindowInsets {
    var insets by mutableStateOf(initialInsets)

    override fun getLeft(density: Density, layoutDirection: LayoutDirection): Int =
        insets.getLeft(density, layoutDirection)

    override fun getTop(density: Density): Int = insets.getTop(density)
    override fun getRight(density: Density, layoutDirection: LayoutDirection): Int =
        insets.getRight(density, layoutDirection)

    override fun getBottom(density: Density): Int = insets.getBottom(density)
}

@ThemePreviews
@Composable
private fun AppSearchBarPreview() {
    PreviewLayout {
        Column {
            AppSearchBar(
                query = "",
                enabled = true,
                active = true,
                onQueryChange = {},
                onSearch = {},
                onActiveChange = { },
                placeholder = { Text("Search movies, tv shows..") },
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.CenterHorizontally),
                content = {
                    Text("Search results")
                }
            )
        }
    }
}