package com.example.soundaryalahariappversion2.ui.home

import android.util.Log
import androidx.annotation.StringRes
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SearchBar
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.soundaryalahariappversion2.R
import com.example.soundaryalahariappversion2.SoundaryalahariTopAppBar
import com.example.soundaryalahariappversion2.data.Shloka
import com.example.soundaryalahariappversion2.ui.AppViewModelProvider
import com.example.soundaryalahariappversion2.ui.navigation.NavigationDestination
import com.example.soundaryalahariappversion2.ui.shloka.ShlokaDetailsBody
import com.example.soundaryalahariappversion2.ui.shloka.ShlokaEntryBody
import com.example.soundaryalahariappversion2.ui.shloka.ShlokaEntryViewModel
import com.example.soundaryalahariappversion2.ui.theme.SoundaryalahariAppVersion2Theme
import kotlinx.coroutines.launch

object HomeDestination: NavigationDestination {
    override val route = "home"
    override val titleRes = R.string.soundaryalahari//"Soundaryalahari"
}
/**
 * Entry route for the Home screen
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    navigateToShlokaEntry: () -> Unit,
    navigateToShlokaDetails: (Int) -> Unit,
    //onShlokaSearch:(Int) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel = viewModel(factory = AppViewModelProvider.Factory),
    searchViewModel: SearchViewModel
){
    val homeUiState by viewModel.homeUiState.collectAsState()
    var searchNum = searchViewModel.searchNum

    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()

    val TAG = "HomeScreen"
    Scaffold(
        modifier = modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            SoundaryalahariTopAppBar(
                title = stringResource(id = HomeDestination.titleRes),
                canNavigateBack = false,
                scrollBehavior = scrollBehavior
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = navigateToShlokaEntry,
                shape = MaterialTheme.shapes.medium,
                modifier = Modifier.padding(20.dp)//(dimensionResource(id = R.dimen.padding_large))
            ){
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = stringResource(R.string.shloka_entry)//"Add Shloka"
                )
            }
        },
    ) { innerPadding ->
        Box(
            contentAlignment = Alignment.TopCenter,
            modifier = Modifier.padding(innerPadding)
        ) {
            Column {
                var active by remember { mutableStateOf(false) }
                SearchBar(
                    query = searchNum,
                    onQueryChange = { newQuery->
                        searchNum?.let{ //null safety
                            searchNum = newQuery
                        }
                        searchViewModel.updateSearchNum(newQuery)
                        Log.e(TAG, "${searchNum}and${newQuery}")
                    },
                    onSearch = {
                        active = false
                        navigateToShlokaDetails(it.toInt())
                    },
                    active = true,
                    shape = MaterialTheme.shapes.medium,
                    onActiveChange = { active = it },
                    leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                    trailingIcon = {
                        if(searchNum?.isNotEmpty() == true) {
                            IconButton(onClick = { searchNum = "" }) {
                                Icon(Icons.Filled.Clear, contentDescription = null)
                            }
                        }
                    }
                ) {
//
                    AnimatedVisibility(visible = searchNum == "")
                    {
                        HomeBody(
                            shlokaList = homeUiState.shlokaList,
                            onShlokaClick = navigateToShlokaDetails,
                            modifier = Modifier
                                .padding(innerPadding)
                                .fillMaxSize()
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun HomeBody(
    shlokaList: List<Shloka>,
    onShlokaClick: (Int) -> Unit,
    modifier: Modifier = Modifier
){
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
    ) {
        if(shlokaList.isEmpty()) {
            Text(
                text = stringResource(R.string.no_shlokas_yet),
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.titleLarge
            )
        } else {
            ShlokaList(
                shlokaList = shlokaList,
                onShlokaClick = {onShlokaClick(it.number) },
                modifier = Modifier.padding(horizontal = 8.dp)
            )
        }
    }
}

@Composable
private fun ShlokaList(
    shlokaList: List<Shloka>,
    onShlokaClick: (Shloka) -> Unit,
    modifier: Modifier = Modifier
){
    LazyColumn(modifier = modifier) {
        items( items = shlokaList, key = { it.id}){ shloka ->
            ShlokaItem(
                shloka = shloka,
                modifier = Modifier
                    .padding(dimensionResource(id = R.dimen.padding_small))
                    .clickable { onShlokaClick(shloka) })
        }
    }
}

@Composable
private fun ShlokaItem(
    shloka: Shloka,
    modifier: Modifier = Modifier
){
    Card(
        modifier = modifier
            .fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation =  2.dp)
    ) {
        Text(
            text = shloka.number.toString(),
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(dimensionResource(R.dimen.card_text_vertical_space))
        )
        Text(
            text = shloka.content,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.secondary,
            overflow = TextOverflow.Ellipsis,
            maxLines = 4,
            modifier = Modifier.padding(8.dp)
        )
    }
}
@Preview
@Composable
fun HomeScreenPreview() {
    SoundaryalahariAppVersion2Theme {
        HomeScreen(
            navigateToShlokaEntry = {},
            navigateToShlokaDetails = {},
            searchViewModel = SearchViewModel())
    }
}

