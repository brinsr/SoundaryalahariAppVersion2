package com.example.soundaryalahariappversion2.ui.shloka

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.res.TypedArrayUtils.getResourceId
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.soundaryalahariappversion2.R
import com.example.soundaryalahariappversion2.SoundaryalahariTopAppBar
import com.example.soundaryalahariappversion2.data.Shloka
import com.example.soundaryalahariappversion2.ui.AppViewModelProvider
import com.example.soundaryalahariappversion2.ui.navigation.NavigationDestination
import com.example.soundaryalahariappversion2.ui.theme.SoundaryalahariAppVersion2Theme
import kotlinx.coroutines.launch

object ShlokaEditDestination: NavigationDestination {
    override val route = "shloka_edit"
    override val titleRes = R.string.edit_shloka // Edit Shloka
    const val shlokaNumArg = "shlokaNum"
    val routeWithArgs ="$route/{$shlokaNumArg}"
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShlokaEditScreen(
    navigateBack: () -> Unit,
    onNavigateUp: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: ShlokaEditViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val coroutineScope = rememberCoroutineScope()

    Scaffold(
        topBar = {
            SoundaryalahariTopAppBar(
                title = stringResource(id = ShlokaEditDestination.titleRes),
                canNavigateBack = true,
                navigateUp = onNavigateUp
            )
        },
        modifier = modifier
    ) { innerPadding ->
        //used a different composable , not entry screen body
        ShlokaEditBody(
            shlokaEditUiState = viewModel.shlokaEditUiState,
            onShlokaContentChange = viewModel::updateUiState,
            onSaveClick = {
                coroutineScope.launch {
                    viewModel.updateShloka()
                    navigateBack()
                }
            },
            modifier = Modifier
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShlokaEditBody(
    shlokaEditUiState: ShlokaEntryViewModel.ShlokaUiState, //we need the same info
    onShlokaContentChange: (Shloka) -> Unit,
    onSaveClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    var singResourceInt by remember{ mutableStateOf(0)}

    Column(
        verticalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.padding_small )),
        modifier = modifier.padding(16.dp)
    ) {
        OutlinedTextField(
            value = shlokaEditUiState.shloka.content,
            onValueChange = { onShlokaContentChange(shlokaEditUiState.shloka.copy(content = it)) },
            label = { Text("Content") },
            colors = TextFieldDefaults.outlinedTextFieldColors(
                focusedSupportingTextColor = MaterialTheme.colorScheme.secondaryContainer,
                unfocusedSupportingTextColor = MaterialTheme.colorScheme.secondaryContainer,
                disabledSupportingTextColor = MaterialTheme.colorScheme.secondaryContainer,
            ),
            modifier = Modifier.fillMaxWidth(),
            enabled = true,
        )

        OutlinedTextField(
            value = shlokaEditUiState.shloka.meaning,
            onValueChange = { onShlokaContentChange(shlokaEditUiState.shloka.copy(meaning = it)) },
            label = { Text("Meaning") },
            colors = TextFieldDefaults.outlinedTextFieldColors(
                focusedSupportingTextColor = MaterialTheme.colorScheme.secondaryContainer,
                unfocusedSupportingTextColor = MaterialTheme.colorScheme.secondaryContainer,
                disabledSupportingTextColor = MaterialTheme.colorScheme.secondaryContainer,
            ),
            modifier = Modifier.fillMaxWidth(),
            enabled = true,
        )

        OutlinedTextField(
            value = shlokaEditUiState.shloka.chant,
            onValueChange = { onShlokaContentChange(shlokaEditUiState.shloka.copy(chant = it)) },
            label = { Text("Chant resource") },
            colors = TextFieldDefaults.outlinedTextFieldColors(
                focusedSupportingTextColor = MaterialTheme.colorScheme.secondaryContainer,
                unfocusedSupportingTextColor = MaterialTheme.colorScheme.secondaryContainer,
                disabledSupportingTextColor = MaterialTheme.colorScheme.secondaryContainer,
            ),
            modifier = Modifier.fillMaxWidth(),
            enabled = true,
            singleLine = true
        )

        OutlinedTextField(
            value = shlokaEditUiState.shloka.sing,
            onValueChange = {
                onShlokaContentChange(shlokaEditUiState.shloka.copy(sing = it))

            },
            label = { Text("Sing Resource Text") },
            colors = TextFieldDefaults.outlinedTextFieldColors(
                focusedSupportingTextColor = MaterialTheme.colorScheme.secondaryContainer,
                unfocusedSupportingTextColor = MaterialTheme.colorScheme.secondaryContainer,
                disabledSupportingTextColor = MaterialTheme.colorScheme.secondaryContainer,
            ),
            modifier = Modifier.fillMaxWidth(),
            enabled = true,
            singleLine = true
        )
        Button(
            onClick = onSaveClick,
            shape = MaterialTheme.shapes.small,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text( text = "Save")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ShlokaEditBodyPreview(){
    SoundaryalahariAppVersion2Theme {
        ShlokaEditBody( ShlokaEntryViewModel.ShlokaUiState(Shloka(0,0,"","","","")), onShlokaContentChange = {}, onSaveClick = {},)
    }
}
