package com.hassuk1.feature.authentication

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.ClipboardManager
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.rememberAsyncImagePainter
import com.hassuk1.core.designsystem.icons.AppIcons
import com.hassuk1.core.designsystem.icons.AppImageIcons
import com.hassuk1.core.designsystem.theme.AIChatTheme
import com.hassuk1.core.model.ApiConfig
import com.hassuk1.feature.authentication.components.InfoAlertDialog
import com.hassuk1.feature.authentication.components.InputUserKeyBar

const val GIT_LINK = "https://github.com/Hasuk1/AI-Chat-Jetpack-Compose"

@Composable
fun AuthenticationScreen(
  viewModel: AuthenticationViewModel = hiltViewModel(), goChat: () -> Unit
) {
  val context = LocalContext.current
  val state by viewModel.state.collectAsState()
  var hideKeyboard by remember { mutableStateOf(false) }
  var isAlertDialogOpen by remember { mutableStateOf(false) }

  AIChatTheme {
    Column(modifier = Modifier
      .fillMaxSize()
      .background(color = MaterialTheme.colorScheme.background)
      .clickable(
        interactionSource = remember { MutableInteractionSource() }, indication = null
      ) {
        hideKeyboard = !hideKeyboard
      }) {
      Box(
        modifier = Modifier
          .fillMaxWidth()
          .height(90.dp)
          .background(color = MaterialTheme.colorScheme.primaryContainer),
        contentAlignment = Alignment.BottomStart
      ) {
        Row(
          modifier = Modifier
            .fillMaxWidth()
            .height(40.dp),
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.Center
        ) {
          Image(painter = rememberAsyncImagePainter(model = AppImageIcons.Ai),
            contentDescription = "ai_logo",
            modifier = Modifier
              .clip(RoundedCornerShape(10.dp))
              .size(35.dp)
              .clickable {
                context.startActivity(
                  Intent(
                    Intent.ACTION_VIEW, Uri.parse(GIT_LINK)
                  )
                )
              })
        }

        Row(
          modifier = Modifier
            .fillMaxWidth()
            .height(40.dp),
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.End
        ) {
          IconButton(
            onClick = { isAlertDialogOpen = true }, colors = IconButtonDefaults.iconButtonColors(
              contentColor = MaterialTheme.colorScheme.primary
            )
          ) {
            Icon(AppIcons.Info, contentDescription = "Info")
          }
        }
        if (isAlertDialogOpen) {
          InfoAlertDialog(
            onDismissRequest = { isAlertDialogOpen = false },
            onConfirmation = { isAlertDialogOpen = false },
            dialogTitle = "Information",
            dialogText = "Some information.\nSome information.\nSome information.\nSome information.",
          )
        }
      }
      Column(
        modifier = Modifier
          .fillMaxWidth()
          .weight(1f),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
      ) {
        InputUserKeyBar(state = state,
          hint = "ApiKey",
          hideKeyboard = hideKeyboard,
          onFocusClear = { hideKeyboard = false },
          onDone = { viewModel.updateEnteredKey(it) })
        Button(
          onClick = { viewModel.saveUserData() },
          modifier = Modifier
            .padding(horizontal = 20.dp)
            .fillMaxWidth(0.95f)
            .height(50.dp),
          shape = RoundedCornerShape(20.dp),
          colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.primary,
            contentColor = MaterialTheme.colorScheme.onPrimary
          )
        ) {
          Text(text = "Connect", fontSize = 20.sp)
        }
        TextButton(onClick = {
          context.startActivity(
            Intent(
              Intent.ACTION_VIEW, Uri.parse(state.userSelectedApi.docsUsl)
            )
          )
        }) {
          Text("Read more about ${state.userSelectedApi.apiName} apikey")
        }
      }
      Row(
        modifier = Modifier
          .fillMaxWidth()
          .height(60.dp)
          .background(color = MaterialTheme.colorScheme.primaryContainer),
        verticalAlignment = Alignment.CenterVertically,
      ) {
        val activateButtonColorFilter =
          ColorFilter.tint(MaterialTheme.colorScheme.onPrimaryContainer)
        val unActivateButtonColorFilter = ColorFilter.tint(MaterialTheme.colorScheme.secondary)
        Box(contentAlignment = Alignment.Center,
          modifier = Modifier
            .fillMaxHeight()
            .fillMaxWidth(0.5f)
            .clip(RoundedCornerShape(10.dp))
            .clickable {
              viewModel.updateSelectedApi(ApiConfig.NEURO)
            }) {
          Image(
            painter = rememberAsyncImagePainter(model = AppImageIcons.NeuroApi),
            modifier = Modifier.fillMaxSize(0.5f),
            contentDescription = "neuro_logo",
            colorFilter = if (state.userSelectedApi == ApiConfig.NEURO) activateButtonColorFilter else unActivateButtonColorFilter
          )
        }
        Box(contentAlignment = Alignment.Center,
          modifier = Modifier
            .fillMaxSize()
            .clip(RoundedCornerShape(10.dp))
            .clickable {
              viewModel.updateSelectedApi(ApiConfig.OPENAI)
            }) {
          Image(
            painter = rememberAsyncImagePainter(model = AppImageIcons.GptApi),
            modifier = Modifier.fillMaxSize(0.5f),
            contentDescription = "gpt_logo",
            colorFilter = if (state.userSelectedApi == ApiConfig.OPENAI) activateButtonColorFilter else unActivateButtonColorFilter
          )
        }
      }
    }

  }
}
