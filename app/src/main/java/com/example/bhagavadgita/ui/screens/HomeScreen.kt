package com.example.bhagavadgita.ui.screens

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.bhagavadgita.R
import com.example.bhagavadgita.model.BhagavadGitaVerse
import com.example.bhagavadgita.ui.theme.orange
import com.example.bhagavadgita.ui.theme.red
import com.example.bhagavadgita.ui.theme.shapes
import com.example.bhagavadgita.ui.theme.yellow

@Composable
fun HomeScreen(
    bhagavadGitaUiState: BhagavadGitaUiState,
    onStartButtonClicked: () -> Unit,
    onRetryButtonClicked: () -> Unit,
    modifier: Modifier = Modifier
) {
    when(bhagavadGitaUiState){
        is BhagavadGitaUiState.Loading -> LoadingScreen(
            modifier = modifier
                .fillMaxSize()
                .padding(dimensionResource(id = R.dimen.padding_medium))
        )
        is BhagavadGitaUiState.Error -> ErrorScreen(
            onRetryButtonClicked = onRetryButtonClicked,
            modifier = modifier
                .fillMaxSize()
                .padding(dimensionResource(id = R.dimen.padding_medium))
        )
        is BhagavadGitaUiState.Success -> HomeDisplayScreen(
            bhagavadGitaVerse = bhagavadGitaUiState.verseOfTheDay!!,
            onStartButtonClicked = onStartButtonClicked,
            modifier = modifier
                .fillMaxSize()
                .padding(dimensionResource(id = R.dimen.padding_medium))

        )
    }
}

@Composable
fun HomeDisplayScreen(
    bhagavadGitaVerse: BhagavadGitaVerse,
    onStartButtonClicked: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.padding_medium)),
            modifier = Modifier.verticalScroll(rememberScrollState())
        ) {
            VerseOfTheDay(
                bhagavadGitaVerse = bhagavadGitaVerse
            )
            Button(
                onClick = onStartButtonClicked,
                colors = ButtonDefaults.buttonColors(
                    containerColor = orange,
                    contentColor = Color.White
                ),
                shape = shapes.small
            ) {
                Text(
                    text = stringResource(R.string.start_reading),
                    fontSize = 16.sp
                )
            }
        }
    }
}

@Composable
fun VerseOfTheDay(
    bhagavadGitaVerse: BhagavadGitaVerse,
    modifier: Modifier = Modifier
) {
    var expanded by remember{
        mutableStateOf(false)
    }
    OutlinedCard(
        modifier = modifier,
        colors = CardDefaults.cardColors(
            containerColor = yellow
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(dimensionResource(id = R.dimen.padding_medium)),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Box(
                    modifier = Modifier
                        .background(orange)
                        .padding(dimensionResource(id = R.dimen.padding_small))
                        .align(Alignment.Center)
                ) {
                    Text(
                        text = stringResource(R.string.shlok_of_the_day),
                        style = MaterialTheme.typography.titleLarge,
                        fontFamily = FontFamily.Cursive,
                        fontWeight = FontWeight.ExtraBold,
                        color = Color.White
                    )
                }
                ExpandableButton(
                    expanded = expanded,
                    onClick = {
                        expanded = !expanded
                    },
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                )
            }
            VerseSection(
                verse = bhagavadGitaVerse.slok,
                borderImage = R.drawable.ic_border,
                modifier = Modifier.padding(vertical = dimensionResource(id = R.dimen.padding_medium))
            )
            Text(
                text = stringResource(id = R.string.chapter_s,bhagavadGitaVerse.chapter.toString()),
                style = MaterialTheme.typography.titleMedium,
                color = red,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = stringResource(id = R.string.shlok_s,bhagavadGitaVerse.verse.toString()),
                style = MaterialTheme.typography.titleMedium,
                color = red,
                fontWeight = FontWeight.Bold
            )
            if(expanded) {
                Text(
                    text = buildAnnotatedString {
                        withStyle(
                            style = SpanStyle(
                                color = red
                            )
                        ) {
                            append(bhagavadGitaVerse.chinmay.hc)
                        }
                    },
                    textAlign = TextAlign.Justify
                )
            }
        }
    }
}

@Composable
fun VerseSection(
    verse: String,
    @DrawableRes borderImage: Int,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.padding_small))
    ) {
        Image(
            painter = painterResource(id = borderImage),
            contentDescription = null
        )
        CenteredText(
            text = verse
        )
        Image(
            painter = painterResource(id = borderImage),
            contentDescription = null,
            modifier = Modifier
                .rotate(degrees=180f)
        )
    }
}

@Composable
fun CenteredText(
    text: String,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        text.split("\n").forEach {
            Text(
                text = it,
                fontSize = 16.sp,
                textAlign = TextAlign.Center,
                color = red,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
fun ErrorScreen(
    onRetryButtonClicked:()-> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .background(yellow)
                .padding(dimensionResource(id = R.dimen.padding_medium)),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_connection_error),
                contentDescription = stringResource(R.string.no_connection)
            )
            Text(
                text = stringResource(R.string.failed_to_load),
                modifier = Modifier.padding(dimensionResource(id = R.dimen.padding_medium)),
                style = MaterialTheme.typography.titleMedium
            )
            Button(
                onClick = onRetryButtonClicked,
                colors = ButtonDefaults.buttonColors(
                    containerColor = orange,
                    contentColor = Color.White
                )
            ) {
                Text(text = stringResource(R.string.retry))
            }
        }
    }
}

@Composable
fun LoadingScreen(
    modifier: Modifier = Modifier
) {
    Image(
        painter = painterResource(id = R.drawable.loading_img),
        contentDescription = stringResource(R.string.loading),
        modifier = modifier.size(200.dp)
    )
}
