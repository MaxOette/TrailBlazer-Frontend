package de.max.trailblazerfrontendv1.screens

import android.app.Activity
import android.content.Intent
import android.widget.Toast
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.EditNote
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.filled.PersonAdd
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.AbsoluteAlignment
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import de.max.trailblazerfrontendv1.Api.AddFriendApi
import de.max.trailblazerfrontendv1.Api.DeleteFriendApi
import de.max.trailblazerfrontendv1.Api.Friend
import de.max.trailblazerfrontendv1.Api.FriendIdApi
import de.max.trailblazerfrontendv1.Api.FriendInviteApi
import de.max.trailblazerfrontendv1.Api.FriendProfilePictureApi
import de.max.trailblazerfrontendv1.Api.Invite

import de.max.trailblazerfrontendv1.Api.LogoutAPI
import de.max.trailblazerfrontendv1.Api.ProfilePictureApi
import de.max.trailblazerfrontendv1.Api.UpdateFriendApi
import de.max.trailblazerfrontendv1.Api.UploadProfilePictureApi
import de.max.trailblazerfrontendv1.LoginActivity
import de.max.trailblazerfrontendv1.R
import de.max.trailblazerfrontendv1.Util.GeneralConstants
import de.max.trailblazerfrontendv1.Util.UserConstants
import de.max.trailblazerfrontendv1.location.LocationService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import retrofit2.HttpException
import java.io.File
import java.io.FileOutputStream


@Composable
fun ProfileScreen() {
    var showDialog by remember { mutableStateOf(false) }
    var friends by remember { mutableStateOf(listOf<Friend>()) }
    var invites by remember { mutableStateOf(listOf<Invite>()) }
    var profilePicture by remember { mutableStateOf<Bitmap?>(null) }
    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current

    //todo figure out why id doesnt update immediately
    suspend fun fetchFriendsAndInvites() {
        try {
            friends = FriendIdApi.friendIdService.getFriendsId()
            invites = listOf()
            val fetchedInvites = FriendInviteApi.friendInviteService.getFriendInvites()


            invites = if (fetchedInvites.isEmpty()) {
                listOf() // Set to an empty list if no pending invites
            } else {
                fetchedInvites
            }


        } catch (e: Exception) {
            println("Error fetching friends and invites: ${e.localizedMessage}")
        }
    }

    suspend fun fetchProfilePicture(): Bitmap? {
        return try {
            val response = ProfilePictureApi.profilePictureService.getProfilePicture()
            response.byteStream()?.use { inputStream ->
                BitmapFactory.decodeStream(inputStream)
            }
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }


    suspend fun uploadProfilePicture(file: File) {
        withContext(Dispatchers.IO) {
            try {
                val requestFile = file.asRequestBody("image/jpeg".toMediaTypeOrNull())
                val body = MultipartBody.Part.createFormData("image", file.name, requestFile)

                val response =
                    UploadProfilePictureApi.uploadProfilePictureService.uploadProfilePicture(
                        image = body,
                        type = "image/jpeg",
                        name = file.name,
                        size = file.length().toInt(),
                        isProfilePicture = true
                    )

            } catch (e: Exception) {
                e.printStackTrace()
                println("Error uploading profile picture: ${e.localizedMessage}")
            } catch (e: HttpException) {  // This catches HTTP-specific errors
                val errorBody = e.response()?.errorBody()?.string()
                println("HTTP Error: $errorBody")
                e.printStackTrace()
            }
        }
    }

    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            coroutineScope.launch {
                val bitmap = BitmapFactory.decodeStream(context.contentResolver.openInputStream(it))
                profilePicture = bitmap

                val file = File(context.cacheDir, "profile_picture.jpg")
                val outputStream = FileOutputStream(file)
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
                outputStream.flush()
                outputStream.close()

                uploadProfilePicture(file)
            }
        }
    }

    LaunchedEffect(Unit) {
        coroutineScope.launch {
            fetchFriendsAndInvites()
            profilePicture = fetchProfilePicture()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(30.dp)
    ) {
        ProfileOverviewCard(profilePicture){ imagePickerLauncher.launch("image/*") }
        Text(
            text = "Freunde",
            modifier = Modifier.padding(top = 16.dp),
            fontWeight = FontWeight.Bold,
            fontSize = 36.sp
        )

        //todo Antwortlogik
        if (showDialog) {
            AddFriendDialog(
                showDialog = showDialog,
                onDismissRequest = { showDialog = false },
                onSubmit = { email ->
                    GlobalScope.launch(Dispatchers.IO) {
                        try {
                            val response = AddFriendApi.addFriendService.addFriend(email)
                        } catch (e: Exception) {
                            println(e.message)
                        }
                    }
                }
            )
        }

        if(friends.isEmpty() && invites.isEmpty()) {
            LazyColumn(
                modifier = Modifier.fillMaxHeight(0.85f),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                items(1) {
                    Text(
                        text= "Du hast noch keine Freunde!",
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(56.dp))
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxHeight(0.85f)
            ) {
                items(invites) { invite ->
                    InviteCard(invite) { fetchFriendsAndInvites() }
                }
                items(friends) { friend ->
                    friendCard(friend) { fetchFriendsAndInvites() }
                }
            }
        }
        Spacer(Modifier.height(8.dp))
        Button(
            onClick = { showDialog = true },
            modifier = Modifier.fillMaxWidth()
        ) {
            Icon(Icons.Default.PersonAdd, contentDescription = "Freund hinzufügen")
            Text("  Freund hinzufügen")
        }
    }
}



@Composable
fun ProfileOverviewCard(profilePicture: Bitmap?, onProfilePictureClick: () -> Unit) {
    ElevatedCard(
        elevation = CardDefaults.cardElevation(
            defaultElevation = 6.dp
        ),
        modifier = Modifier
            .fillMaxWidth()
            .height(220.dp)

    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(150.dp)
                .padding(top = 8.dp, start = 8.dp)
        ) {

            if (profilePicture != null) {
                Image(
                    bitmap = profilePicture.asImageBitmap(),
                    contentDescription = null,
                    modifier = Modifier
                        .size(120.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.primary)
                        .clickable { onProfilePictureClick() }
                )
            } else {
                println("kein Bild gefunden, default verwendet")
                Image(
                    painter = painterResource(id = R.drawable.profile_pic),
                    contentDescription = null,
                    modifier = Modifier
                        .size(120.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.primary)
                        .clickable { onProfilePictureClick() }
                )
            }
            Column(
                modifier = Modifier.padding(top = 20.dp)
            ) {
                //Todo Anbinden des Usernames sobald verfügbar
                Text(
                    text = UserConstants.username,
                    modifier = Modifier.padding(start = 24.dp),
                    fontWeight = FontWeight.Bold,
                    fontSize = 36.sp
                )
                Text(
                    text = UserConstants.email,
                    modifier = Modifier.padding(start = 24.dp),
                    fontWeight = FontWeight.Normal,
                    fontSize = 24.sp
                )
            }
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            EditProfileButton(
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 8.dp, end = 4.dp)
            )
            LogoutButton(
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 4.dp, end = 8.dp)
            )
        }
    }


}

//	•`_´•


data class ExampleFriend(
    var userName: String = "MaxMusterman",
    var id: String = "",
    var progress: Int = 55

)

@Composable
fun InviteCard(invite: Invite, onInviteHandled: suspend () -> Unit) {
    val context = LocalContext.current
    val email: String = invite.email
    val uuid = invite.uuid

    Card(
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant,
        ),
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                text = "Anfrage von $email",
                modifier = Modifier.weight(1f),
                fontWeight = FontWeight.Bold
            )
            Row {
                IconButton(
                    onClick = {
                        GlobalScope.launch(Dispatchers.IO) {
                            try {
                                UpdateFriendApi.updateFriendService.acceptFriend(true, uuid)
                                onInviteHandled()
                                withContext(Dispatchers.Main) {
                                    Toast.makeText(context, "Freundschaftsanfrage angenommen", Toast.LENGTH_LONG).show()
                                }
                            } catch (e: Exception) {
                                println("Error accepting invite: ${e.message}")
                                withContext(Dispatchers.Main) {
                                    Toast.makeText(context, "Fehler bei der Anfrageverarbeitung", Toast.LENGTH_LONG).show()
                                }
                            }
                        }
                    }
                ) {
                    Icon(Icons.Default.CheckCircle, contentDescription = "Accept", tint = Color.Green)
                }
                Spacer(modifier = Modifier.width(4.dp))
                IconButton(
                    onClick = {
                        GlobalScope.launch(Dispatchers.IO) {
                            try {
                                UpdateFriendApi.updateFriendService.acceptFriend(false, uuid)
                                onInviteHandled()
                                withContext(Dispatchers.Main) {
                                    Toast.makeText(context, "Freundschaftsanfrage abgelehnt", Toast.LENGTH_LONG).show()
                                }
                            } catch (e: Exception) {
                                println("Error declining invite: ${e.message}")
                                withContext(Dispatchers.Main) {
                                    Toast.makeText(context, "Fehler bei der Anfrageverarbeitung", Toast.LENGTH_LONG).show()
                                }
                            }
                        }
                    }
                ) {
                    Icon(
                        Icons.Default.Close,
                        contentDescription = "Decline",
                        tint = MaterialTheme.colorScheme.error
                    )
                }
            }
        }
    }
}

@Composable
fun friendCard(friend: Friend, onDeleteFriendHandled: suspend () -> Unit) {
    val context = LocalContext.current
    val email = friend.email
    val progress = friend.stats
    val uuid = friend.uuid
    val coroutineScope = rememberCoroutineScope()
    var friendProfilePicture by remember { mutableStateOf<Bitmap?>(null) }

    suspend fun fetchProfilePicture(): Bitmap? {
        return try {
            val response = FriendProfilePictureApi.friendProfilePictureService.getFriendProfilePicture(uuid)
            response.byteStream()?.use { inputStream ->
                BitmapFactory.decodeStream(inputStream)
            }
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    LaunchedEffect(Unit){
        coroutineScope.launch {
            friendProfilePicture = fetchProfilePicture()
        }
    }

    Card(
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant,
        ),
        modifier = Modifier
            .height(120.dp)
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    val displayPicture = friendProfilePicture // local immutable copy
                    if (displayPicture != null) {
                        Image(
                            bitmap = displayPicture.asImageBitmap(),
                            contentDescription = null,
                            modifier = Modifier
                                .size(50.dp)
                                .clip(CircleShape)
                                .background(MaterialTheme.colorScheme.primary)
                        )
                    } else {
                        Image(
                            painter = painterResource(id = R.drawable.profile_pic),
                            contentDescription = null,
                            modifier = Modifier
                                .size(50.dp)
                                .clip(CircleShape)
                                .background(MaterialTheme.colorScheme.primary)
                        )
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Column(modifier = Modifier.align(Alignment.CenterVertically)) {
                        Text(text = email, fontWeight = FontWeight.Bold)
                        Text(text = String.format("%.3f", progress) + "%")
                    }
                }
                IconButton(
                    onClick = {
                        GlobalScope.launch(Dispatchers.IO) {
                            try {
                                DeleteFriendApi.deleteFriendService.deleteFriend(uuid)
                                onDeleteFriendHandled()
                                withContext(Dispatchers.Main) {
                                    Toast.makeText(
                                        context,
                                        "Die Freundschaft wurde beendet",
                                        Toast.LENGTH_LONG
                                    ).show()
                                }
                            } catch (e: Exception) {
                                println("Error declining invite: ${e.message}")
                                withContext(Dispatchers.Main) {
                                    Toast.makeText(context, "Fehler bei der Anfrageverarbeitung", Toast.LENGTH_LONG).show()
                                }
                            }
                        }
                    }
                ) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Delete friend",
                        tint = MaterialTheme.colorScheme.error
                    )
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
            LinearProgressIndicator(
                progress = progress.div(100f),
                color = MaterialTheme.colorScheme.primary
            )
        }
    }
}


@Composable
fun EditProfileButton(modifier: Modifier) {
    Button(
        modifier = modifier,
        onClick = {

        }
    ) {
        Icon(Icons.Default.EditNote, contentDescription = "Profil bearbeiten")
        Text("  Bearbeiten")
    }
}

@Composable
fun LogoutButton(modifier: Modifier) {
    val context = LocalContext.current
    Button(
        modifier = modifier,
        onClick = {
            try {
                GlobalScope.launch(Dispatchers.IO) {
                    LogoutAPI.logoutService.logout()
                    Intent(GeneralConstants.applicationContext, LocationService::class.java).apply {
                        action = LocationService.ACTION_STOP
                        GeneralConstants.applicationContext.startService(this)
                    }
                    UserConstants.accessToken = ""
                    UserConstants.refreshToken = ""
                    UserConstants.email = ""

                    withContext(Dispatchers.Main) {
                        context.startActivity(Intent(context, LoginActivity::class.java))
                        (context as Activity).finish()
                    }

                }
            } catch (e: Exception) {
                println("Error during logout : ${e.message}")
            }
        }
    ) {
        Icon(Icons.Default.Logout, contentDescription = "Abmelden")
        Text("  Abmelden")
    }
}


@Composable
fun AddFriendDialog(
    showDialog: Boolean,
    onDismissRequest: () -> Unit,
    onSubmit: (String) -> Unit,
) {
    if (showDialog) {
        var email by remember { mutableStateOf("") }
        val context = LocalContext.current

        AlertDialog(
            onDismissRequest = { onDismissRequest() },
            title = { Text(text = "Freund hinzufügen") },
            text = {
                TextField(
                    value = email,
                    onValueChange = { email = it },
                    label = { Text("E-Mail") },
                    singleLine = true
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        onSubmit(email)
                        Toast.makeText(context, "Freundschaftsanfrage versendet", Toast.LENGTH_LONG).show()
                        onDismissRequest()
                    }
                ) {
                    Text("Hinzufügen")
                }
            },
            dismissButton = {
                Button(onClick = { onDismissRequest() }) {
                    Text("Abbrechen")
                }
            }

        )
    }
}

@Composable
fun EditProfileDialog(
    onDismissRequest: () -> Unit,
    onConfirmation: () -> Unit,
) {

    Dialog(onDismissRequest = { onDismissRequest() }) {
        Column {
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
            ) {
                TextButton(
                    onClick = { onDismissRequest() },
                    modifier = Modifier.padding(8.dp),
                ) {
                    Text("Dismiss")
                }
                TextButton(
                    onClick = { onConfirmation() },
                    modifier = Modifier.padding(8.dp),
                ) {
                    Text("Confirm")
                }
            }
        }
    }
}


//LazyColumn(
//
//) {
//    item {
//        Button(
//            onClick = {
//                GlobalScope.launch(Dispatchers.IO) {
//                    try {
//                        val response = FriendIdApi.friendIdService.getFriendsId()
//                        println(response);
//                    } catch (e: Error) {
//                        println(e.message);
//                    }
//                }
//            }
//        ) {
//            Text("get freunde test")
//        }
//    }
//    item {
//        friendCard(ExampleFriend())
//    }
//    item {
//        friendCard(ExampleFriend())
//    }
//    item {
//        friendCard(ExampleFriend())
//    }
//    item {
//        friendCard(ExampleFriend())
//    }
//    item {
//        friendCard(ExampleFriend())
//    }
//    item {
//        friendCard(ExampleFriend())
//    }
//    item { friendCard(ExampleFriend(userName = "Jonson123", progress = 36)) }
//}



//@Composable
//fun AddFriendField(
//    value: String,
//    onChange: (String) -> Unit,
//    modifier: Modifier = Modifier,
//    label: String = "E-Mail",
//    placeholder: String = "Gib Die E-Mail Adresse der Person an"
//) {
//
//    val focusManager = LocalFocusManager.current
//    val leadingIcon = @Composable {
//        Icon(
//            Icons.Default.Email,
//            contentDescription = "",
//            tint = MaterialTheme.colorScheme.primary
//        )
//    }
//    Row() {
//        TextField(
//            value = value,
//            onValueChange = onChange,
//            modifier = modifier,
//            leadingIcon = leadingIcon,
//            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
//            keyboardActions = KeyboardActions(
//                onNext = { focusManager.moveFocus(FocusDirection.Down) }
//            ),
//            //placeholder = { Text(placeholder) },
//            label = { Text(label) },
//            singleLine = true,
//            visualTransformation = VisualTransformation.None
//        )
//
//        Button(
//            onClick = {
//                GlobalScope.launch(Dispatchers.IO) {
//                    try {
//                        val response = AddFriendApi.addFriendService.addFriend(value)
//                    } catch (e: Error) {
//                        println(e.message)
//                    }
//                }
//            }
//
//        ) {
//            Text("Freund hinzufügen")
//        }
//    }
//}

//data class friendData(
//    var email: String = ""
//)
//

