package com.example.backgroundremover_changebg.presentation.ui.navigation

import android.annotation.SuppressLint
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DashboardCustomize
import androidx.compose.material.icons.filled.Details
import androidx.compose.material.icons.filled.Layers
import androidx.compose.material.icons.filled.Login
import androidx.compose.material.icons.filled.SupervisorAccount
import androidx.compose.material.icons.outlined.DashboardCustomize
import androidx.compose.material.icons.outlined.Details
import androidx.compose.material.icons.outlined.Layers
import androidx.compose.material.icons.outlined.Login
import androidx.compose.material.icons.outlined.SupervisorAccount
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.backgroundremover_changebg.presentation.ui.screens.BatchScreen
import com.example.backgroundremover_changebg.presentation.ui.screens.CreateScreen
import com.example.backgroundremover_changebg.presentation.ui.screens.TeamsScreen
import com.example.backgroundremover_changebg.presentation.ui.screens.Your_Content_Screen
import com.example.backgroundremover_changebg.presentation.ui.screens.bgdetail.BgDetailScreen
import com.example.backgroundremover_changebg.presentation.ui.screens.bgdetail.BlurScreen
import com.example.backgroundremover_changebg.presentation.ui.screens.bgdetail.ColorsBgDetail
import com.example.backgroundremover_changebg.presentation.ui.screens.bgdetail.CropScreen
import com.example.backgroundremover_changebg.presentation.ui.screens.mixcolors.Pic1Screen
import com.example.backgroundremover_changebg.presentation.ui.screens.mixcolors.Pic2Screen
import com.example.backgroundremover_changebg.presentation.ui.screens.mixcolors.Pic3Screen
import com.example.backgroundremover_changebg.presentation.ui.screens.mixcolors.Pic4Screen
import com.example.backgroundremover_changebg.presentation.ui.screens.mixcolors.Pic5Screen
import com.example.backgroundremover_changebg.presentation.ui.screens.mixcolors.Pic6Screen

@Composable
fun Navigation(navController: NavHostController) {
    NavHost(navController = navController, startDestination = Screens.Create.route) {
        composable(Screens.Create.route) {
            CreateScreen(navController = navController)
        }
        composable(Screens.Batch.route) {
            BatchScreen(navController = navController)
        }
        composable(Screens.Teams.route) {
            TeamsScreen(navController = navController)
        }
        composable(Screens.Your_Content.route) {
            Your_Content_Screen(navController = navController)
        }
        composable(Screens.BgDetail.route) {
            BgDetailScreen(navController = navController)
        }
        composable(Screens.ColorsBgDetail.route) {
            ColorsBgDetail(navController = navController)
        }
        composable(Screens.CropScreen.route) {
            CropScreen(navController = navController)
        }
        composable(Screens.BlursScreen.route) {
            BlurScreen(navController = navController)
        }
        composable(Screens.Pic1Screen.route) {
            Pic1Screen(navController = navController)
        }

        composable(Screens.Pic2Screen.route) {
            Pic2Screen(navController = navController)
        }

        composable(Screens.Pic3Screen.route) {
            Pic3Screen(navController = navController)
        }

        composable(Screens.Pic4Screen.route) {
            Pic4Screen(navController = navController)
        }

        composable(Screens.Pic5Screen.route) {
            Pic5Screen(navController = navController)
        }

        composable(Screens.Pic6Screen.route) {
            Pic6Screen(navController = navController)
        }

    }
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun NavEntry() {
    val navController = rememberNavController()
    var showBottomNav by remember { mutableStateOf(true) }
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    showBottomNav = when {
        currentRoute == null -> true
        currentRoute?.startsWith(Screens.BgDetail.route) == true -> false
        currentRoute?.startsWith(Screens.ColorsBgDetail.route) == true -> false
        currentRoute?.startsWith(Screens.CropScreen.route) == true -> false
        currentRoute?.startsWith(Screens.BlursScreen.route) == true -> false
        currentRoute?.startsWith(Screens.Pic1Screen.route) == true -> false
        currentRoute?.startsWith(Screens.Pic2Screen.route) == true -> false
        currentRoute?.startsWith(Screens.Pic3Screen.route) == true -> false
        currentRoute?.startsWith(Screens.Pic4Screen.route) == true -> false
        currentRoute?.startsWith(Screens.Pic5Screen.route) == true -> false
        currentRoute?.startsWith(Screens.Pic6Screen.route) == true -> false
        else -> true
    }

    Scaffold(
        bottomBar = {
            if (showBottomNav) {
                BottomNavigation(navController = navController)
            }
        }
    ) { innerPadding ->
        Navigation(navController = navController)
    }
}

sealed class Screens(
    val title: String,
    val route: String,
    val selectedIcon: ImageVector,
    val unSelectedIcon: ImageVector
) {
    object Create : Screens(
        "Create",
        "Create",
        selectedIcon = Icons.Filled.DashboardCustomize,
        unSelectedIcon = Icons.Outlined.DashboardCustomize
    )

    object Batch : Screens(
        "Batch",
        "Batch",
        selectedIcon = Icons.Filled.Layers,
        unSelectedIcon = Icons.Outlined.Layers
    )

    object Teams : Screens(
        "Teams",
        "Teams",
        selectedIcon = Icons.Filled.SupervisorAccount,
        unSelectedIcon = Icons.Outlined.SupervisorAccount
    )

    object Your_Content : Screens(
        "Your Content",
        "Your Content",
        selectedIcon = Icons.Filled.Login,
        unSelectedIcon = Icons.Outlined.Login
    )

    object BgDetail : Screens(
        "BgDetail",
        "BgDetail",
        selectedIcon = Icons.Filled.Details,
        unSelectedIcon = Icons.Outlined.Details
    )

    object ColorsBgDetail : Screens(
        "ColorsBgDetail",
        "ColorsBgDetail",
        selectedIcon = Icons.Filled.Details,
        unSelectedIcon = Icons.Outlined.Details
    )

    object CropScreen : Screens(
        "CropScreen",
        "CropScreen",
        selectedIcon = Icons.Filled.Details,
        unSelectedIcon = Icons.Outlined.Details
    )

    object BlursScreen : Screens(
        "BlursScreen",
        "BlursScreen",
        selectedIcon = Icons.Filled.Details,
        unSelectedIcon = Icons.Outlined.Details
    )


    object Pic1Screen : Screens(
        "Pic1Screen",
        "Pic1Screen",
        selectedIcon = Icons.Filled.Details,
        unSelectedIcon = Icons.Outlined.Details
    )

    object Pic2Screen : Screens(
        "Pic2Screen",
        "Pic2Screen",
        selectedIcon = Icons.Filled.Details,
        unSelectedIcon = Icons.Outlined.Details
    )

    object Pic3Screen : Screens(
        "Pic3Screen",
        "Pic3Screen",
        selectedIcon = Icons.Filled.Details,
        unSelectedIcon = Icons.Outlined.Details
    )

    object Pic4Screen : Screens(
        "Pic4Screen",
        "Pic4Screen",
        selectedIcon = Icons.Filled.Details,
        unSelectedIcon = Icons.Outlined.Details
    )

    object Pic5Screen : Screens(
        "Pic5Screen",
        "Pic5Screen",
        selectedIcon = Icons.Filled.Details,
        unSelectedIcon = Icons.Outlined.Details
    )

    object Pic6Screen : Screens(
        "Pic6Screen",
        "Pic6Screen",
        selectedIcon = Icons.Filled.Details,
        unSelectedIcon = Icons.Outlined.Details
    )
}

@Composable
fun BottomNavigation(
    navController: NavHostController
) {
    val item = listOf(
        Screens.Create,
        Screens.Batch,
        Screens.Teams,
        Screens.Your_Content
    )

    NavigationBar(containerColor = Color.White) {
        val navStack by navController.currentBackStackEntryAsState()
        val current = navStack?.destination?.route

        item.forEach {
            NavigationBarItem(selected = current == it.route, onClick = {
                navController.navigate(it.route) {
                    navController.graph?.let {
                        it.route?.let { it1 -> popUpTo(it1) }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            }, icon = {
                if (current == it.route) {
                    Icon(imageVector = it.selectedIcon, contentDescription = "", tint = Color.Blue)
                } else {
                    Icon(
                        imageVector = it.unSelectedIcon,
                        contentDescription = "",
                    )
                }
            }, label = {
                if (current == it.route) {
                    Text(text = it.title, color = Color.Blue, fontWeight = FontWeight.Medium)
                } else {
                    Text(text = it.title, fontWeight = FontWeight.Medium)
                }
            }, colors = NavigationBarItemDefaults.colors(indicatorColor = Color.Transparent))
        }
    }

}