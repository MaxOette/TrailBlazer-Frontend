package de.max.trailblazerfrontendv1.screens

import android.app.Activity
import android.content.Intent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import de.max.trailblazerfrontendv1.Api.AddFriendApi
import de.max.trailblazerfrontendv1.Api.Friend
import de.max.trailblazerfrontendv1.Api.FriendIdApi
import de.max.trailblazerfrontendv1.Api.FriendInviteApi
import de.max.trailblazerfrontendv1.Api.Invite

import de.max.trailblazerfrontendv1.Api.LogoutAPI
import de.max.trailblazerfrontendv1.LoginActivity
import de.max.trailblazerfrontendv1.R
import de.max.trailblazerfrontendv1.Util.GeneralConstants
import de.max.trailblazerfrontendv1.Util.UserConstants
import de.max.trailblazerfrontendv1.location.LocationService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


@Composable
fun ProfileScreen() {
    var showDialog by remember { mutableStateOf(false) }
    var friends by remember { mutableStateOf(listOf<Friend>()) }
    var invites by remember { mutableStateOf(listOf<Invite>())}
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        coroutineScope.launch {
            try {
                // Assuming the API returns the list directly as parsed JSON
                friends = FriendIdApi.friendIdService.getFriendsId()
                invites = FriendInviteApi.friendInviteService.getFriendInvites()
            } catch (e: Exception) {
                println("Error fetching friends: ${e.localizedMessage}")
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(30.dp)
    ) {
        ElevatedCardExample()
        Text(
            text = "Freunde",
            modifier = Modifier.padding(top = 16.dp),
            fontWeight = FontWeight.Bold,
            fontSize = 36.sp
        )
        Button(
            onClick = { showDialog = true }
        ) {
            Text("Freund hinzufügen")
        }
        //todo Antwortlogik
        if (showDialog) {
            AddFriendDialog(
                showDialog = showDialog,
                onDismissRequest = { showDialog = false },
                invites = invites,
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
        LazyColumn {
            items(friends) { friend ->
                friendCard(friend)
            }
        }
    }


}


@Composable
fun ElevatedCardExample() {
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
            Image(
                painter = painterResource(id = R.drawable.profile_pic),
                contentDescription = null,
                modifier = Modifier
                    .size(120.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.primary)
            )
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
fun inviteCard(invite: Invite){
    var email: String = invite.email
    var uuid = invite.uuid

    Card(
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant,
        ),
        modifier = Modifier
            .height(120.dp)
            .fillMaxWidth()
            .padding(8.dp)
    ){
        Row(){
            Text(email)
            Button(
                onClick = {

                }
            ){
                Text("accept")
            }
            Button(
                onClick = {

                }
            ){
                Text("decline")
            }
        }
    }
}

@Composable
fun friendCard(friend: Friend) {
    var email= friend.email
    var progress = friend.stats
    var uuid = friend.uuid
    Card(
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant,
        ),
        modifier = Modifier
            .height(120.dp)
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = painterResource(id = R.drawable.profile_pic),
                    contentDescription = null,
                    modifier = Modifier
                        .size(50.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.primary)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = email,
                    modifier = Modifier.align(Alignment.CenterVertically)
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            LinearProgressIndicator(
                progress = progress,
                modifier = Modifier.fillMaxWidth(),
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
//        Icon(
//            Icons.Default.BorderColor,
//            contentDescription = "",
//            tint = MaterialTheme.colorScheme.surface
//        ) // 	( 0 _ 0 )
        Text(
            modifier = Modifier.padding(start = 4.dp),
            text = "Bearbeiten"
        )

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
        Text("Abmelden")
    }
}




@Composable
fun AddFriendDialog(
    showDialog: Boolean,
    onDismissRequest: () -> Unit,
    onSubmit: (String) -> Unit,
    invites: List<Invite>
) {
    if (showDialog) {
        var email by remember { mutableStateOf("") }  // Declare email here so it's shared

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
                        onSubmit(email)  // Now email is accessible here
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
        Text("Invites")
        LazyColumn {
            items(invites) { invite ->
                inviteCard(invite)
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

