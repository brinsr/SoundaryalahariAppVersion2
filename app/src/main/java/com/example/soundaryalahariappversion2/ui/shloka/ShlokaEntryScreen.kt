package com.example.soundaryalahariappversion2.ui.shloka

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.soundaryalahariappversion2.R
import com.example.soundaryalahariappversion2.SoundaryalahariTopAppBar
import com.example.soundaryalahariappversion2.data.Shloka
import com.example.soundaryalahariappversion2.ui.AppViewModelProvider
import com.example.soundaryalahariappversion2.ui.navigation.NavigationDestination
import com.example.soundaryalahariappversion2.ui.theme.SoundaryalahariAppVersion2Theme
import kotlinx.coroutines.launch


object ShlokaEntryDestination: NavigationDestination {
    override val route = "shloka_entry"
    override val titleRes = R.string.add_shloka //"Add Shloka"
}
private val TAG ="ShlokaEntryScreen"

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShlokaEntryScreen(
    navigateBack: () -> Unit,
    onNavigateUp: () -> Unit,
    canNavigateBack: Boolean = true,
    viewModel: ShlokaEntryViewModel = viewModel(factory = AppViewModelProvider.Factory)
){
    val coroutineScope = rememberCoroutineScope()
    Scaffold(
        topBar = {
            SoundaryalahariTopAppBar(
                title = stringResource(ShlokaEntryDestination.titleRes),
                canNavigateBack = canNavigateBack,
                navigateUp = onNavigateUp
            )
        }
    ) { innerPadding ->
        ShlokaEntryBody(
            shlokaUiState = viewModel.shlokaUiState,
            onShlokaContentChange = viewModel::updateUiState,
            onSaveClick = {
                coroutineScope.launch {
                    viewModel.saveShloka()
                    navigateBack()
                }
            },
            modifier = Modifier
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .fillMaxWidth()
        )
    }
}

@Composable
fun ShlokaEntryBody(
    shlokaUiState: ShlokaEntryViewModel.ShlokaUiState,
    onShlokaContentChange: (Shloka) -> Unit,
    onSaveClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.padding_large )),
        modifier = modifier.padding(16.dp)
    ) {

        ShlokaInputForm (
            shloka = shlokaUiState.shloka,
            onContentChange = onShlokaContentChange,
            modifier = Modifier.fillMaxWidth()
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

//In this clipboard branch, I'm attempting to copy-paste shloka from clipboard
// to outlinedtextfield since it's in Sanskrit and cannot be
//typed from English keyboard
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShlokaInputForm(
    shloka: Shloka,
    modifier: Modifier = Modifier,
    onContentChange: (Shloka) -> Unit,
    enabled: Boolean = true
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement
            .Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        //Shloka number entry
        OutlinedTextField(
            value = shloka.number.toString(),
            onValueChange = { newValue ->
                val parsedValue = newValue.toIntOrNull()?:0 //.toInt()
                onContentChange(shloka.copy(number = parsedValue))
            },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            textStyle = androidx.compose.ui.text.TextStyle(
                color = Color.Black,
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold
            ),
            colors = TextFieldDefaults.outlinedTextFieldColors(
                focusedSupportingTextColor = MaterialTheme.colorScheme.secondaryContainer,
                unfocusedSupportingTextColor = MaterialTheme.colorScheme.secondaryContainer,
                disabledSupportingTextColor = MaterialTheme.colorScheme.secondaryContainer,
            ),
            modifier = Modifier
                .width(50.dp),//Intrinsic.Max
            enabled = enabled,
            singleLine = true
        )

        Spacer(modifier = Modifier.height(8.dp))
        //Shloka content entry
        ClipboardOutlinedTextField(context = LocalContext.current,shloka,true)
        //Shloka meaning entry
        ClipboardOutlinedTextField(context = LocalContext.current,shloka,false)

        //Added the Compose elements for entry of chant and sing resource
        OutlinedTextField(
            value = shloka.chant,
            onValueChange = { onContentChange(shloka.copy(chant = it)) },
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
            value = shloka.sing,
            onValueChange = {
                onContentChange(shloka.copy(sing = it))
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
    }
}

@Composable
fun ClipboardOutlinedTextField(
    context: Context,
    shloka:Shloka,
    contentOrMeaning:Boolean
) {
    var textState by remember { mutableStateOf(TextFieldValue())} //substituting textState to shloka.content
    val clipboardManager = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager

    BasicTextField(
        value = textState,
        onValueChange = { newText ->
            textState = newText
        },
        modifier = Modifier.padding(8.dp)
    )
    Row{
        //copy button
        Button(onClick = {
            val clip = ClipData.newPlainText("text",textState.text)
            clipboardManager.setPrimaryClip(clip)
        }) {
            Text( "Copy")
        }
        //Paste button
        Button(onClick = {
            val clipData = clipboardManager.primaryClip
            if(clipData != null && clipData.itemCount >0) {
                val item = clipData.getItemAt(0)
                val pasteText = item.text.toString()
                textState = TextFieldValue(pasteText)
                if(contentOrMeaning) { // if true paste content. If false, paste meaning
                    shloka.content = textState.text
                } else{
                    shloka.meaning = textState.text
                }
            }
        }) {
            Text("Paste")
        }

    }
}

@Preview(showBackground = true)
@Composable
private fun ShlokaEntryScreenPreview() {
    SoundaryalahariAppVersion2Theme {
        ShlokaEntryBody(
            shlokaUiState = ShlokaEntryViewModel.ShlokaUiState(

            ),
            onShlokaContentChange = {},
            onSaveClick = { /*TODO*/ })

    }
}