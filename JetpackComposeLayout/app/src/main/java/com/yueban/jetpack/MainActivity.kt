package com.yueban.jetpack

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
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
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.yueban.jetpack.ui.theme.JetpackComposeLayoutTheme

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

    ScaffoldLayout()
}
