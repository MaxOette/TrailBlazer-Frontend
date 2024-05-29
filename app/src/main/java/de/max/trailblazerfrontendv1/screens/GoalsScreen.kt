package de.max.trailblazerfrontendv1.screens


import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import de.max.trailblazerfrontendv1.Api.Achievement
import de.max.trailblazerfrontendv1.Api.AchievementsApi
import de.max.trailblazerfrontendv1.Util.GeneralConstants

@Composable
fun GoalsScreen() {
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(all = 30.dp)
        ) {
            Text(
                text = "Erfolge",
                fontWeight = FontWeight.Bold,
                fontSize = 36.sp
            )
            Spacer(modifier = Modifier.height(24.dp))
            var achievements by remember { mutableStateOf(emptyList<Achievement>()) }

            LaunchedEffect(key1 = Unit) {
                try {
                    achievements = AchievementsApi.achievementService.getAchievements()
                    achievements.forEach { achievement ->
                        if (achievement.achieved) {
                            achievement.imageResource = GeneralConstants.applicationContext.resources.getIdentifier("check", "drawable", GeneralConstants.applicationContext.packageName)
                        } else {
                            achievement.imageResource = GeneralConstants.applicationContext.resources.getIdentifier("wrong", "drawable", GeneralConstants.applicationContext.packageName)
                        }
                    }
                    achievements = achievements.sortedWith(compareByDescending<Achievement> { it.achieved }.thenBy { it.title })
                    println(achievements)
                } catch (e: Error) {
                    println(e.message)
                }
            }

            LazyColumn {
                itemsIndexed(achievements) { index, achievement ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp),
                        elevation = CardDefaults.cardElevation(
                            defaultElevation = 10.dp
                        )
                    ) {
                        Row(
                            modifier = Modifier
                                .padding(16.dp)
                        ) {
                            Image(
                                painter = painterResource(achievement.imageResource),
                                contentDescription = null,
                                modifier = Modifier
                                    .size(48.dp)
                                    .clip(CircleShape)
                                    .background(MaterialTheme.colorScheme.primary)
                            )
                            Spacer(modifier = Modifier.width(16.dp))
                            Column {
                                Text(text = achievement.title, fontWeight = FontWeight.Bold)
                                Text(text = achievement.description)
                            }
                        }
                    }
                }
            }
        }
    }
}
