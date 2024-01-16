package com.example.soundaryalahariappversion2.ui.shloka

import android.content.Context
import android.media.MediaPlayer
import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.soundaryalahariappversion2.R
import com.example.soundaryalahariappversion2.SoundaryalahariTopAppBar
import com.example.soundaryalahariappversion2.data.Shloka
import com.example.soundaryalahariappversion2.ui.AppViewModelProvider
import com.example.soundaryalahariappversion2.ui.navigation.NavigationDestination
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

object ShlokaDetailsDestination: NavigationDestination {
    override val route = "shloka_details"
    override val titleRes = R.string.shloka_details //Shloka_Details
    const val shlokaNumberArg = "shlokaNumber"
    val routeWithArgs ="$route/{$shlokaNumberArg}" // This is VERY IMPORTANT
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShlokaDetailsScreen(
    navigateToEditShloka: (Int) -> Unit,
    navigateBack: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel:ShlokaDetailsViewModel = viewModel(factory = AppViewModelProvider.Factory)
){
    val coroutineScope = rememberCoroutineScope()
    val uiState = viewModel.shlokaDetailUiState.collectAsState()
    Scaffold(
        topBar = {
            SoundaryalahariTopAppBar(
                title = stringResource(ShlokaDetailsDestination.titleRes),
                canNavigateBack = true,
                navigateUp = navigateBack
            )
        },
        floatingActionButton =  {
            FloatingActionButton(
                onClick = { navigateToEditShloka(uiState.value.shlokaNum) },
                shape = MaterialTheme.shapes.medium,
                modifier = Modifier.padding(dimensionResource(id = R.dimen.padding_large))
            ) {
                Icon(
                    imageVector = Icons.Default.Edit,
                    contentDescription = "Edit Shloka details"
                )
            }
        }, modifier = modifier
    ) { innerPadding ->
        ShlokaDetailsBody(
            shlokaDetailsUiState = uiState.value,
            onDelete = {
                coroutineScope.launch {
                    viewModel.deleteShloka()
                    navigateBack()
                }
            },
            modifier = Modifier
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
        )
    }
}

@Composable
fun ShlokaDetailsBody(
    shlokaDetailsUiState: ShlokaDetailsUiState,
    onDelete: () -> Unit,
    modifier: Modifier = Modifier
){
    Column(
        modifier = modifier.padding(dimensionResource(id = R.dimen.padding_small)),
        verticalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.padding_small))
    ) {
        var deleteConfirmationRequired by rememberSaveable { mutableStateOf(false)}

        ShlokaDetails(
            shloka = shlokaDetailsUiState.shloka,

            modifier = Modifier.fillMaxWidth()
        )
        OutlinedButton(
            onClick = { deleteConfirmationRequired = true },
            shape = MaterialTheme.shapes.small,
            modifier = Modifier.wrapContentSize()
        ) {
            Text("Delete")
        }

        if(deleteConfirmationRequired) {
            DeleteConfirmationDialog(
                onDeleteConfirm = {
                    deleteConfirmationRequired = false
                    onDelete()
                },
                onDeleteCancel = {deleteConfirmationRequired = false },
                modifier = Modifier.padding(dimensionResource(id = R.dimen.padding_medium))
            )
        }
    }
}

@Composable
fun ShlokaDetails(
    shloka: Shloka,
    modifier:Modifier = Modifier
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.padding_medium))
    )  {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(dimensionResource(id = R.dimen.padding_small)),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "Shloka "+shloka.number.toString(),
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .wrapContentSize()
            )
        }


        ShlokaAudio(
            context = LocalContext.current,
            title = "Chant",
            shloka = shloka,
        )

        ShlokaAudio(
            context = LocalContext.current,
            title = "Sing",
            shloka = shloka, //replace this resource with shloka1sing
        )

        ShlokaDetailsRow(
            shlokaDetail = shloka.meaning,//replacing content with meaning for now
            // maxLines = 6,
            modifier = Modifier.padding(
                horizontal = dimensionResource(id = (R.dimen.padding_medium))
            )
        )
    }
}

@Composable
fun ShlokaDetailsRow(
    shlokaDetail: String,
    // maxLines:Int,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier, colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer,
            contentColor = MaterialTheme.colorScheme.onPrimaryContainer
        )
    ) {
        Text(
            text = shlokaDetail,
            modifier = Modifier.padding(dimensionResource(id = R.dimen.padding_medium)),
        )
    }
}

//@RequiresApi(Build.VERSION_CODES.O)
@Composable
private fun ShlokaAudio(
    context: Context,
    title: String,
    shloka: Shloka,
) {
    var mediaPlayer by remember { mutableStateOf(MediaPlayer()) }

    val audioResStrng = "shloka" + shloka.number + title.lowercase()
    //The following is discouraged because app crashes if resource not found
    val audioResId =
        LocalContext.current.resources.getIdentifier(audioResStrng, "raw", context.packageName)

    var isPlaying by remember { mutableStateOf(false) }
    var currentPlaybackPosition by remember { mutableStateOf(0)}
    var shlokaDuration by remember { mutableStateOf(0) }
    val coroutineScope = rememberCoroutineScope()
    var currSliderPos:Int by remember { mutableStateOf(0) }

    Column(
        modifier = Modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = title,//"Chant",
            style = TextStyle(fontWeight = FontWeight.Bold),
            fontSize = 15.sp
        )

        Row(
            horizontalArrangement = Arrangement.Start,
            modifier = Modifier
        ) {
            IconButton(
                onClick = {
                    coroutineScope.launch {
                        if (!isPlaying) {
                            mediaPlayer = MediaPlayer.create(context, audioResId)
                            //IMPORTANT - because of this, able to play again
                            mediaPlayer.setOnCompletionListener {
                                isPlaying = false
                                currSliderPos = 0
                            }
                            Log.e("ShlokaDetailsScreen", "starting first time")
                            mediaPlayer.start()
                            shlokaDuration = mediaPlayer.duration
                            isPlaying = true

                            //this will update slider position continuously
                            while (true) {
                                currSliderPos = mediaPlayer.currentPosition
                                delay(100)
                            }
                        } else {
                            if (mediaPlayer.isPlaying) {
                                Log.e("ShlokaDetailsScreen", "pausing")
                                mediaPlayer.pause()
                                currentPlaybackPosition = mediaPlayer.currentPosition
                            } else {
                                if (mediaPlayer.isPlaying) {
                                    mediaPlayer.seekTo((currentPlaybackPosition))
                                }
                                Log.e("ShlokaDetailsScreen", "seek position received")
                                mediaPlayer.start()
                                isPlaying = true
                            }
                        }
                    }
                }
            ) {
                Icon(
                    // imageVector   = if(isPlaying){Icons.Default.PlayArrow} else{Icons.Default.Refresh},
                    if (!isPlaying) {
                        painterResource(id = R.drawable.baseline_play_circle_filled_24)
                    } else {
                        painterResource(id = R.drawable.baseline_pause_24)
                    },
                    contentDescription = "Play"
                )
            }

            Box(
                modifier = Modifier
                    .clip(MaterialTheme.shapes.small)
            ) {
                Slider(
                    value = currSliderPos.toFloat(),
                    onValueChange = { newPosition ->
                        //Seek to the desired position based on slider value - when paused and restarted
                        mediaPlayer.seekTo(newPosition.toInt())
                        currSliderPos = newPosition.toInt()
                        Log.e("ShlokaDetailsScreen","${mediaPlayer.duration} and $currSliderPos")
                    },
                    valueRange = 0f..shlokaDuration.toFloat(),
                )
            }

            DisposableEffect(key1 = Unit) {
                onDispose {
                    mediaPlayer.release()
                }
            }
        }
    }
}


@Composable
private fun DeleteConfirmationDialog(
    onDeleteConfirm: () -> Unit,
    onDeleteCancel:() -> Unit,
    modifier: Modifier = Modifier
) {
    AlertDialog(onDismissRequest = { /*do Nothing*/ },
        title = { Text("Attention")},
        text = { Text("Are you sure you want to delete?")},
        modifier = modifier,
        dismissButton = {
            TextButton(onClick = onDeleteCancel) {
                Text ("No")
            }
        },
        confirmButton = {
            TextButton(onClick = onDeleteConfirm) {
                Text ("Yes")
            }
        })
}

@Preview
@Composable
fun ShlokaDetailsPreview() {
    val shloka = Shloka(
        number = 1,
        content = "शिवः शक्त्या युक्तो यदि भवति शक्तः प्रभवितुं\n" +
                "न चेदेवं देवो न खलु कुशलः स्पन्दितुमपि ।\n" +
                "अतस्त्वामाराध्यां हरिहरविरिञ्चादिभिरपि\n" +
                "प्रणन्तुं स्तोतुं वा कथमकृतपुण्यः प्रभवति ॥ 1 ||",
        chant = "R.raw.shloka1chant",
        sing = "R.raw.shloka1sing",
        meaning = "Salutations to Lord Shiva"
    )

    ShlokaDetails(shloka = shloka)
}

@Preview
@Composable
fun ShlokaAudioPreview(){
    // ShlokaAudio(context = LocalContext.current,title = "Chant", shloka = Shloka(0,0,"",R.raw.shloka1chant,"",""))
}




