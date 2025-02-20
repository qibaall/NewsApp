package com.sample.newsapp.setting

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun SkeletonLoadingItem() {

    Card(
        modifier = Modifier
            .padding(12.dp)
            .fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
        shape = androidx.compose.foundation.shape.RoundedCornerShape(16.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(12.dp)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Placeholder for Image
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(220.dp)
                    .background(Color.Gray.copy(alpha = 0.2f)) // Skeleton background color
            )
            Spacer(modifier = Modifier.height(12.dp))
            // Placeholder for Title
            Box(
                modifier = Modifier
                    .fillMaxWidth(0.7f) // Simulate title width
                    .height(20.dp)
                    .background(Color.Gray.copy(alpha = 0.3f))
            )
            Spacer(modifier = Modifier.height(8.dp))
            // Placeholder for Subtitle or Description
            Box(
                modifier = Modifier
                    .fillMaxWidth(0.5f)
                    .height(14.dp)
                    .background(Color.Gray.copy(alpha = 0.3f))
            )
        }
    }
}
