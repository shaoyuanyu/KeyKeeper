package com.yusy.keykeeper.ui.pages.mine

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.yusy.keykeeper.ui.pages.empty.EmptyScreen
import com.yusy.keykeeper.ui.theme.KeyKeeperTheme

@Composable
fun MineScreen(
    modifier: Modifier = Modifier
) {
//    MineBody(
//        modifier = modifier
//    )
    EmptyScreen()
}

@Composable
fun MineBody(
    modifier: Modifier = Modifier
) {
    ;
}

@Preview
@Composable
fun MineScreenPreview() {
    KeyKeeperTheme {
        MineBody()
    }
}