@file:OptIn(ExperimentalMaterial3Api::class)

package io.portalhq.portalhackathon.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector

data class FloatingActionButtonParams(
    val icon: ImageVector,
    val onClick: () -> Unit
)

@Composable
fun AppScaffold(
    title: String,
    navigationUp: (() -> Unit)? = null,
    snackbarHostState: SnackbarHostState? = null,
    floatingActionButtonParams: FloatingActionButtonParams? = null,
    actions: @Composable RowScope.() -> Unit = {},
    content: @Composable BoxScope.() -> Unit
) {
    val navigationIcon: @Composable () -> Unit = navigationIcon(navigationUp)

    val snackbarHost: @Composable () -> Unit = snackbarHost(snackbarHostState)

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = title,
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.primary),
                navigationIcon = navigationIcon,
                actions = actions
            )
        },
        snackbarHost = snackbarHost,
        floatingActionButton = appFloatingActionButton(params = floatingActionButtonParams)
    ) { contentPadding ->
        Box(modifier = Modifier.fillMaxSize().padding(contentPadding)) {
            content()
        }
    }
}

@Composable
private fun appFloatingActionButton(params: FloatingActionButtonParams?): @Composable () -> Unit {
    val fab: @Composable () -> Unit = params?.let {
        {
            FloatingActionButton(onClick = params.onClick) {
                Icon(imageVector = params.icon, contentDescription = "Fab icon")
            }
        }
    } ?: {}

    return fab
}

@Composable
private fun snackbarHost(snackbarHostState: SnackbarHostState?): @Composable () -> Unit {
    val snackbarHost: @Composable () -> Unit = if (snackbarHostState != null) {
        { androidx.compose.material3.SnackbarHost(hostState = snackbarHostState) }
    } else {
        {}
    }
    return snackbarHost
}

@Composable
private fun navigationIcon(navigationUp: (() -> Unit)?): @Composable () -> Unit {
    val navigationIcon: @Composable () -> Unit = if (navigationUp != null) {
        {
            IconButton(onClick = { navigationUp() }) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Up Button",
                    tint = MaterialTheme.colorScheme.onPrimary
                )
            }
        }
    } else {
        {}
    }
    return navigationIcon
}
