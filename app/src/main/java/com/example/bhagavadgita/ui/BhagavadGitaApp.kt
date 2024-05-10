package com.example.bhagavadgita.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.bhagavadgita.R
import com.example.bhagavadgita.ui.screens.AllChaptersScreen
import com.example.bhagavadgita.ui.screens.AllVersesScreen
import com.example.bhagavadgita.ui.screens.BhagavadGitaViewModel
import com.example.bhagavadgita.ui.screens.HomeScreen
import com.example.bhagavadgita.ui.screens.Screens
import com.example.bhagavadgita.ui.theme.orange

@Composable
fun BhagavadGitaApp()
{
    val bhagavadGitaViewModel: BhagavadGitaViewModel = viewModel(factory = BhagavadGitaViewModel.Factory)
    val bhagavadGitaNavController: NavHostController = rememberNavController()
    val backStackEntry by bhagavadGitaNavController.currentBackStackEntryAsState()
    val currentScreenTitle = Screens.valueOf(backStackEntry?.destination?.route?:Screens.Home.name).title
    Scaffold(
        topBar = {
            BhagavadGitaTopAppBar(
                currentScreenTitle = currentScreenTitle
            )
        }
    ) {innerPadding->
        Box(
            modifier = Modifier
                .padding(innerPadding)
        ) {
            AppBackground()
            NavHost(
                navController = bhagavadGitaNavController,
                startDestination = Screens.Home.name
            ) {
                composable(route = Screens.Home.name){
                    HomeScreen(
                        bhagavadGitaUiState = bhagavadGitaViewModel.bhagavadGitaUiState,
                        onStartButtonClicked = {
                            bhagavadGitaViewModel.getChapters()
                            bhagavadGitaNavController.navigate(Screens.AllChapters.name)
                        },
                        onRetryButtonClicked = bhagavadGitaViewModel::getVerseOfTheDay
                    )
                }
                composable(route = Screens.AllChapters.name){
                    AllChaptersScreen(
                        bhagavadGitaUiState = bhagavadGitaViewModel.bhagavadGitaUiState,
                        onChapterItemClicked = {selectedChapterNumber->
                            bhagavadGitaViewModel.setChapterNumber(selectedChapterNumber)
                            bhagavadGitaNavController.navigate(Screens.AllVerses.name)
                        },
                        onRetryButtonClicked = bhagavadGitaViewModel::getChapters
                    )
                }
                composable(route = Screens.AllVerses.name){
                    AllVersesScreen(
                        bhagavadGitaUiState = bhagavadGitaViewModel.bhagavadGitaUiState,
                        onRetryButtonClicked = bhagavadGitaViewModel::getVerses
                    )
                }
            }
        }
    }
}

@Composable
fun AppBackground(
    modifier: Modifier = Modifier
) {
    Image(
        painter = painterResource(id = R.drawable.app_backround),
        contentDescription = stringResource(R.string.app_background),
        modifier = modifier
            .fillMaxSize()
            .alpha(0.35f),
        contentScale = ContentScale.FillBounds,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BhagavadGitaTopAppBar(
    currentScreenTitle: String,
    modifier: Modifier = Modifier
) {
    CenterAlignedTopAppBar(
        title = {
            Text(
                text = currentScreenTitle,
                style = MaterialTheme.typography.headlineSmall
            )
        },
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
            containerColor = orange,
            titleContentColor = Color.White,
            navigationIconContentColor = Color.White,
            actionIconContentColor = Color.White
        ),
        modifier = modifier
    )
}