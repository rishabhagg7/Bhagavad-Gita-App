package com.example.bhagavadgita.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.example.bhagavadgita.R
import com.example.bhagavadgita.model.BhagavadGitaVerse
import com.example.bhagavadgita.ui.theme.orange
import com.example.bhagavadgita.ui.theme.red
import com.example.bhagavadgita.ui.theme.yellow

@Composable
fun AllVersesScreen(
    bhagavadGitaUiState: BhagavadGitaUiState,
    onRetryButtonClicked: () -> Unit,
    modifier: Modifier = Modifier
) {
    when(bhagavadGitaUiState){
        is BhagavadGitaUiState.Loading -> LoadingScreen(
            modifier = Modifier
                .fillMaxSize()
                .padding(dimensionResource(id = R.dimen.padding_medium))
        )
        is BhagavadGitaUiState.Error -> ErrorScreen(
            onRetryButtonClicked = onRetryButtonClicked,
            modifier = modifier
                .fillMaxSize()
                .padding(dimensionResource(id = R.dimen.padding_medium))
        )
        is BhagavadGitaUiState.Success -> VersesList(
            verses = bhagavadGitaUiState.versesPager!!.collectAsLazyPagingItems(),
            onRetryButtonClicked = onRetryButtonClicked,
            modifier = Modifier
                .fillMaxSize()
                .padding(dimensionResource(id = R.dimen.padding_medium))
        )
    }
}

@Composable
fun VersesList(
    verses: LazyPagingItems<BhagavadGitaVerse>,
    onRetryButtonClicked: () -> Unit,
    modifier: Modifier = Modifier
) {
    when(verses.loadState.refresh){
        is LoadState.Loading -> {
            Box(
                modifier = modifier
                    .fillMaxWidth()
            ) {
                CircularProgressIndicator(
                    modifier = Modifier
                        .padding(dimensionResource(id = R.dimen.padding_medium))
                        .align(Alignment.Center)
                        .size(100.dp),
                    color = red,
                    strokeWidth = 4.dp
                )
            }
        }
        is LoadState.Error -> ErrorScreen(
            onRetryButtonClicked = onRetryButtonClicked,
            modifier = modifier
                .fillMaxSize()
                .padding(dimensionResource(id = R.dimen.padding_medium))
        )
        is LoadState.NotLoading -> {
            LazyColumn(
                modifier = modifier
            ) {
                items(
                    count = verses.itemCount,
                    key = {
                        verses[it]!!.id
                    }
                ){index->
                    VerseCard(
                        verse = verses[index]!!,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = dimensionResource(id = R.dimen.padding_small))
                    )
                }
                item {
                    if(verses.loadState.append is LoadState.Loading){
                        Box(
                            modifier = Modifier.fillMaxWidth(),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator(
                                color = red
                            )
                        }
                    }
                    if(verses.loadState.append is LoadState.Error){
                        Box(
                            modifier = Modifier.fillMaxWidth(),
                            contentAlignment = Alignment.Center
                        ) {
                            Column(
                                verticalArrangement = Arrangement.Center,
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                CircularProgressIndicator(
                                    color = red
                                )
                                Text(
                                    text = stringResource(R.string.connection_lost),
                                    color = red
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun VerseCard(
    verse: BhagavadGitaVerse,
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
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
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
                        text = stringResource(id = R.string.shlok_s,verse.verse),
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
                verse = verse.slok,
                borderImage = R.drawable.ic_border_1,
                modifier = Modifier.padding(vertical = dimensionResource(id = R.dimen.padding_medium))
            )
            if(expanded) {
                Text(
                    text = buildAnnotatedString {
                        withStyle(
                            style = SpanStyle(
                                color = red
                            )
                        ) {
                            append(verse.chinmay.hc)
                        }
                    },
                    textAlign = TextAlign.Justify
                )
            }
        }
    }
}
