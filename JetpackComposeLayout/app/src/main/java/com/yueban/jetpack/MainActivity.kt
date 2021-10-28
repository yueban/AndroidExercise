package com.yueban.jetpack

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Button
import androidx.compose.material.ContentAlpha
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.LocalContentAlpha
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.AlignmentLine
import androidx.compose.ui.layout.FirstBaseline
import androidx.compose.ui.layout.layout
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil.annotation.ExperimentalCoilApi
import coil.compose.rememberImagePainter
import com.yueban.jetpack.ui.theme.JetpackComposeLayoutTheme
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            JetpackComposeLayoutTheme {
                Surface(color = MaterialTheme.colors.background) {
                    MainView()
                }
            }
        }
    }
}

@Composable
fun PhotographerCard(modifier: Modifier = Modifier) {
    Row(modifier
            // click area not contains padding
//            .padding(16.dp)
//            .clickable { }

            // click area contains padding
//            .clickable { }
//            .padding(16.dp)

            .padding(8.dp)
            .clip(RoundedCornerShape(4.dp))
            .background(MaterialTheme.colors.surface)
            .clickable { }
            .padding(16.dp)) {
        Surface(modifier = Modifier.size(50.dp), shape = CircleShape, color = MaterialTheme.colors.onSurface.copy(alpha = 0.2f)) {

        }
        Column(
            Modifier
                .padding(start = 8.dp)
                .align(Alignment.CenterVertically)
        ) {
            Text("Alfred Sisley", fontWeight = FontWeight.Bold)
            CompositionLocalProvider(LocalContentAlpha provides ContentAlpha.medium) {
                Text("3 minutes ago", style = MaterialTheme.typography.body2)
            }
        }
    }
}

@Composable
fun ScaffoldLayout() {
    Scaffold(topBar = {
        TopAppBar(title = {
            Text(text = "app bar text")
        }, actions = {
            IconButton(onClick = { /*TODO*/ }) {
                Icon(imageVector = Icons.Filled.Favorite, contentDescription = null)
            }
        })
    }) { innerPadding ->
        print("innerPadding: $innerPadding")
        BodyContent(Modifier.padding(innerPadding))
    }
}

@ExperimentalCoilApi
@Composable
fun ListLayout() {
//    val scrollState = rememberScrollState()

    val listSize = 100
    val scrollState = rememberLazyListState(initialFirstVisibleItemIndex = 30)
    val coroutineScope = rememberCoroutineScope()

    Column {
        Row {
            Button(
                onClick = { coroutineScope.launch { scrollState.animateScrollToItem(0) } },
                modifier = Modifier.weight(1f)
            ) {
                Text(text = "scroll to top")
            }
            Button(
                onClick = { coroutineScope.launch { scrollState.animateScrollToItem(listSize - 1) } },
                modifier = Modifier.weight(1f)
            ) {
                Text(text = "scroll to bottom")
            }
        }
        LazyColumn(state = scrollState, modifier = Modifier.fillMaxWidth()) {
            items(listSize) {
                ImageItem(it)
            }
        }
    }
}

fun Modifier.firstBaselineToTop(firstBaselineToTop: Dp) = this.then(layout { measurable, constraints ->
    val placeable = measurable.measure(constraints)

    check(placeable[FirstBaseline] != AlignmentLine.Unspecified)
    val firstBaseline = placeable[FirstBaseline]

    val placeableY = firstBaselineToTop.roundToPx() - firstBaseline
    val height = placeable.height + placeableY

    layout(placeable.width, height) {
        placeable.placeRelative(0, placeableY)
    }
})

@Composable
fun TextWithPaddingToBaseline() {
    Text("Just do it", Modifier.firstBaselineToTop(32.dp))
}

@Composable
fun TextWithNormalPadding() {
    Text("Just do it", Modifier.padding(top = 32.dp))
}

@ExperimentalCoilApi
@Composable
fun ImageItem(index: Int) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Image(painter = rememberImagePainter(data = "https://developer.android.com/images/brand/Android_Robot.png"),
              contentDescription = "Robot",
              modifier = Modifier.size(50.dp))
        Spacer(modifier = Modifier.size(4.dp))
        Text("Item $index")
    }
}

val tabs = listOf(
    "Phone" to Icons.Filled.Phone,
    "Home" to Icons.Filled.Home,
    "Mine" to Icons.Filled.Person,
)

@Composable
fun BodyContent(modifier: Modifier = Modifier) {
    Column(modifier = modifier) {
        Column(Modifier.weight(1.0f, true)) {
            Text(text = "Hello, Scaffold!")
            Text(text = "There is some text in body content")
        }

        var selectedIndex by remember { mutableStateOf(0) }
        BottomNavigation {
            tabs.forEachIndexed { index, item ->
                BottomNavigationItem(
                    icon = {
                        Icon(imageVector = item.second, contentDescription = null)
                    },
                    label = {
                        Text(text = item.first)
                    },
                    selected = selectedIndex == index,
                    onClick = { selectedIndex = index },
                    alwaysShowLabel = false)
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    JetpackComposeLayoutTheme {
        MainView()
    }
}

@Composable
fun MainView() {
//    PhotographerCard()

//    SlotButtonDemo()

//    ScaffoldLayout()

//    ListLayout()

    TextWithPaddingToBaseline()
    TextWithNormalPadding()
}
