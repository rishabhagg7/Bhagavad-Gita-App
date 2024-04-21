package com.example.bhagavadgita.ui.screens

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import com.example.bhagavadgita.R
import com.example.bhagavadgita.model.BhagavadGitaChapter
import com.example.bhagavadgita.ui.theme.numberBox
import com.example.bhagavadgita.ui.theme.orange
import com.example.bhagavadgita.ui.theme.red
import com.example.bhagavadgita.ui.theme.shapes
import com.example.bhagavadgita.ui.theme.yellow

@Composable
fun AllChaptersScreen(
    modifier: Modifier = Modifier,
    bhagavadGitaUiState: BhagavadGitaUiState,
    onChapterItemClicked: (Int) -> Unit,
    onRetryButtonClicked: () -> Unit
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
        is BhagavadGitaUiState.Success -> ChaptersList(
            chapters = bhagavadGitaUiState.chapters!!,
            onChapterItemClicked = onChapterItemClicked,
            modifier = Modifier
                .fillMaxSize()
                .padding(dimensionResource(id = R.dimen.padding_medium))
        )
    }
}

@Composable
fun ChaptersList(
    chapters: List<BhagavadGitaChapter>,
    onChapterItemClicked: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier
    ) {
        items(
            count = chapters.size,
            key = {
                chapters[it].chapterNumber
            }
        ){index->
            ChapterCard(
                chapter = chapters[index],
                onChapterItemClicked = onChapterItemClicked,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = dimensionResource(id = R.dimen.padding_small))
            )
        }
    }
}

@Composable
fun ChapterCard(
    chapter: BhagavadGitaChapter,
    onChapterItemClicked: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    var expanded by remember{
        mutableStateOf(false)
    }
    OutlinedCard(
        modifier = modifier
            .animateContentSize(
            animationSpec = spring(
                dampingRatio = Spring.DampingRatioNoBouncy,
                stiffness = Spring.StiffnessMedium
            )
        ),
        colors = CardDefaults.cardColors(
            containerColor = yellow
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
        ){
            Text(
                text = chapter.chapterNumber.toString(),
                modifier = Modifier
                    .clip(shape = numberBox.medium)
                    .align(Alignment.TopStart)
                    .background(red)
                    .padding(dimensionResource(id = R.dimen.padding_small)),
                color = Color.White
            )
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier
                    .align(Alignment.Center)
                    .padding(dimensionResource(id = R.dimen.padding_medium))
            ) {
                Text(
                    text = chapter.name,
                    style = MaterialTheme.typography.titleLarge,
                    color = red,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = stringResource(id = R.string.chapter_english,chapter.translation),
                    color = red,
                    style = MaterialTheme.typography.titleSmall
                )
                if(expanded){
                    ChapterDetailsSection(
                        chapter = chapter,
                        modifier = Modifier
                            .padding(dimensionResource(id = R.dimen.padding_medium))
                            .align(Alignment.Start)
                    )
                    Button(
                        onClick = {
                            onChapterItemClicked(chapter.chapterNumber)
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = orange,
                            contentColor = Color.White
                        ),
                        shape = shapes.medium
                    ) {
                        Text(
                            text = stringResource(R.string.read_shloks)
                        )
                    }
                }
            }
            ExpandableButton(
                expanded = expanded,
                onClick = {
                    expanded = !expanded
                },
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(
                        top = dimensionResource(id = R.dimen.padding_small),
                        end = dimensionResource(id = R.dimen.padding_small)
                    )
            )
        }
    }
}

@Composable
fun ChapterDetailsSection(
    chapter: BhagavadGitaChapter,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.padding_small))
    ) {
        Text(
            text = buildAnnotatedString {
                append(stringResource(id = R.string.meaning))
                withStyle(
                    style = SpanStyle(
                        color = red
                    )
                ){
                    append(chapter.meaning.hi)
                }
            },
            textAlign = TextAlign.Justify
        )
        Text(
            text = buildAnnotatedString {
                append(stringResource(id = R.string.summary))
                withStyle(
                    style = SpanStyle(
                        color = red
                    )
                ){
                    append(chapter.summary.hi)
                }
            },
            textAlign = TextAlign.Justify
        )
    }
}

@Composable
fun ExpandableButton(
    expanded: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    IconButton(
        onClick = onClick,
        modifier = modifier
            .clip(shape = shapes.extraLarge)
            .background(color = orange)
    ) {
        Icon(
            imageVector = if(expanded) Icons.Filled.ExpandLess else Icons.Filled.ExpandMore,
            contentDescription = stringResource(R.string.expand_button_content_description),
            tint = Color.White
        )
    }
}
