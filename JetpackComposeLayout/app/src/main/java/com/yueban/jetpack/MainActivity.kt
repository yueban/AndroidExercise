package com.yueban.jetpack

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Button
import androidx.compose.material.Card
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.AlignmentLine
import androidx.compose.ui.layout.FirstBaseline
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.layout.layout
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import coil.annotation.ExperimentalCoilApi
import coil.compose.rememberImagePainter
import com.yueban.jetpack.ui.theme.JetpackComposeLayoutTheme
import kotlinx.coroutines.launch
import kotlin.math.max

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
fun CustomColumn(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit) {
    Layout(modifier = modifier, content = content) { measurables, constraints ->
        val placeables = measurables.map { measurable -> measurable.measure(constraints) }

        var yPos = 0
        layout(constraints.maxWidth, constraints.maxHeight) {
            placeables.forEach { placeable ->
                placeable.placeRelative(0, yPos)
                yPos += placeable.height
            }
        }
    }
}

@Composable
fun TextWithPaddingToBaseline() {
    Text("Just do it", Modifier.firstBaselineToTop(32.dp))
}

@Composable
fun TextWithNormalPadding() {
    Text("Just do it", Modifier.padding(top = 32.dp))
}

@Composable
fun StaggeredGrid(
    modifier: Modifier = Modifier,
    rows: Int = 2,
    content: @Composable () -> Unit
) {
    Layout(
        modifier = modifier,
        content = content,
    ) { measurables, constraints ->
        val rowWidths = IntArray(rows) { 0 }
        val rowHeights = IntArray(rows) { 0 }
        val placeables = measurables.mapIndexed { index, measurable ->
            val placeable = measurable.measure(constraints)
            val row = index % rows
            rowWidths[row] += placeable.width
            rowHeights[row] = max(rowHeights[row], placeable.height)
            placeable
        }

        val width = rowWidths.maxOrNull()?.coerceIn(constraints.minWidth.rangeTo(constraints.maxWidth)) ?: constraints.minWidth
        val height = rowHeights.sum().coerceIn(constraints.minHeight.rangeTo(constraints.maxHeight))

        val rowY = rowHeights.runningReduce { acc, i -> acc + i }.toMutableList().apply {
            add(0, 0)
            removeLast()
        }
        layout(width, height) {
            val rowX = IntArray(rows) { 0 }
            placeables.mapIndexed { index, placeable ->
                val row = index % rows
                placeable.placeRelative(x = rowX[row], y = rowY[row])
                rowX[row] += placeable.width
            }
        }
    }
}

@Composable
fun Chip(modifier: Modifier = Modifier, text: String) {
    Card(modifier = modifier,
         border = BorderStroke(width = Dp.Hairline, color = Color.Black),
         shape = RoundedCornerShape(8.dp)) {
        Row(modifier = Modifier.padding(start = 8.dp, top = 4.dp, end = 8.dp, bottom = 4.dp),
            verticalAlignment = Alignment.CenterVertically) {
            Box(modifier = Modifier
                .size(16.dp, 16.dp)
                .background(color = MaterialTheme.colors.secondary))
            Spacer(modifier = Modifier.width(4.dp))
            Text(text)
        }
    }
}

val topics = listOf(
    "Arts & Crafts", "Beauty", "Books", "Business", "Comics", "Culinary",
    "Design", "Fashion", "Film", "History", "Maths", "Music", "People", "Philosophy",
    "Religion", "Social sciences", "Technology", "TV", "Writing"
)

@Composable
fun StaggeredGridDemo(modifier: Modifier = Modifier) {
    Row(modifier = modifier.horizontalScroll(rememberScrollState())) {
        StaggeredGrid(modifier, rows = 5) {
            topics.map { topic ->
                Chip(modifier = Modifier.padding(8.dp), text = topic)
            }
        }
    }
}

@Composable
fun ConstraintLayoutContent() {
    ConstraintLayout {
        val (button, text) = createRefs()
        Button(
            onClick = {},
            modifier = Modifier.constrainAs(button) {
                top.linkTo(parent.top, margin = 16.dp)
            }
        ) { Text("Button") }

        Text("Text",
             Modifier.constrainAs(text) {
                 top.linkTo(button.bottom, margin = 16.dp)
                 centerHorizontallyTo(parent)
             })
    }
}

@Composable
fun ConstraintLayoutContent2() {
    ConstraintLayout {
        val (button1, button2, text) = createRefs()


        Button(
            onClick = {},
            modifier = Modifier.constrainAs(button1) {
                top.linkTo(parent.top, margin = 16.dp)
            }
        ) { Text("Button 1") }


        Text("Text",
             Modifier.constrainAs(text) {
                 top.linkTo(button1.bottom, margin = 16.dp)
                 centerAround(button1.end)
             })

        val barrier = createEndBarrier(button1, text)
        Button(
            onClick = {},
            modifier = Modifier.constrainAs(button2) {
                top.linkTo(parent.top, margin = 16.dp)
                start.linkTo(barrier)
            }
        ) {
            Text("Button 2")
        }
    }
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

//    TextWithPaddingToBaseline()
//    TextWithNormalPadding()

//    CustomColumn {
//        repeat(10) {
//            Text("text $it")
//        }
//    }

//    StaggeredGridDemo()

    ConstraintLayoutContent2()
}
