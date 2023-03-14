package com.android.template.ui.compose

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import android.os.Bundle
import android.view.View
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.view.isVisible
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import coil.compose.AsyncImage
import com.android.template.R
import com.android.template.data.models.api.response.Article
import com.android.template.databinding.FragmentComposeBinding
import com.android.template.ui.base.BaseFragment
import com.android.template.ui.compose.ui.theme.TemplateTheme
import com.android.template.ui.compose.viewmodel.ComposeViewModel
import com.android.template.utils.helpers.*
import kotlinx.coroutines.launch

class ComposeFragment : BaseFragment<FragmentComposeBinding, ComposeViewModel>() {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.toolbar.initUpNavigation()
        binding.composeView.setContent {
            TemplateTheme {
                MyApp()
            }
        }

        viewModel.article.observe(viewLifecycleOwner) {
            it.map { articles ->
                binding.composeView.setContent {
                    TemplateTheme {
                        Greetings(articles)
                    }
                }
            }
        }
    }

    private fun launchProgress() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.numbersFlow().collect { percentage ->
                    binding.saveProgressBar.progress = percentage
                    val percentageText = "$percentage %"
                    binding.saveProgressBarText.text = percentageText
                    binding.saveProgressBar.isVisible = percentage != 100
                    binding.saveProgressBarText.isVisible = percentage != 100
                    binding.composeView.isVisible = percentage == 100
                }
            }
        }
    }

    @Composable
    fun MyApp() {
        var shouldShowBoarding by rememberSaveable { mutableStateOf(true) }

        if (shouldShowBoarding) {
            OnboardingScreen(
                onContinueClicked = {
                    launchProgress()
                    viewModel.getNews()
                    shouldShowBoarding = false
                }
            )
        }
    }

    @Composable
    private fun Greetings(
        /*articles: List<Article>*/
        articles: List<Article> = listOf(Article(
            null, "Manish Singh", "US strengthens tech ties with India but doesn't seek decoupling from China, Raimondo says",
        null, null, "https://techcrunch.com/wp-content/uploads/2023/03/GettyImages-1247414173-1.jpg?resize=1200,800",
        "2023-03-10T16:43:05Z", "The U.S. government is not seeking to decouple from China, nor is it seeking technological decoupling, but Washington would like to see India achieve its aspirations to play a larger role in the elec… [+2503 chars]")
    )) {
        LazyColumn(modifier = Modifier.padding(vertical = 4.dp)) {
            items(items = articles) { article ->
                Greeting(article)
            }
        }
    }

    @Composable
    fun Greeting(article: Article) {
        var expanded by remember { mutableStateOf(false) }


/*val extraPadding by animateDpAsState(
        targetValue = if (expanded) 48.dp else 0.dp,
        animationSpec = tween(
            durationMillis = 1000
        )
        */

/*spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        )
    )*/

        Surface(
            color = MaterialTheme.colors.primary,
            modifier = Modifier.padding(vertical = 4.dp, horizontal = 8.dp)
        ) {
            Row(
                modifier = Modifier
                    .padding(12.dp)
                    .animateContentSize(
                        animationSpec = spring(
                            dampingRatio = Spring.DampingRatioMediumBouncy,
                            stiffness = Spring.StiffnessLow
                        )
                    )
            ) {
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .padding(12.dp)
                    //.padding(bottom = extraPadding.coerceAtLeast(0.dp))
                ) {
                    article.author?.let { author -> Text(text = author) }
                    article.publishedAt?.let { date -> Text(text = convert(date, newsServerDateFormat, newsUIDateFormat), fontSize = 10.sp) }
                    article.title?.let { title ->
                        Text(
                            text = title, style = MaterialTheme.typography.h6.copy(
                                fontWeight = FontWeight.ExtraBold
                            )
                        )
                    }
                    if (expanded) {
                        article.content?.let { content ->
                            Text(
                                text = content,
                            )
                        }
                        article.urlToImage?.let { url ->
                            AsyncImage(
                                model = url, contentDescription = null,
                                modifier = Modifier
                                    .padding(top = 12.dp)
                            )
                        }
                    }
                }
                //OutlinedButton
                IconButton(
                    onClick = { expanded = !expanded }
                ) {
                    Icon(
                        imageVector = if (expanded) Icons.Filled.ExpandLess else Icons.Filled.ExpandMore,
                        contentDescription = if (expanded) {
                            stringResource(R.string.show_less)
                        } else {
                            stringResource(R.string.show_more)
                        }
                    )
                }
            }
        }
    }

    @Preview(
        showBackground = true,
        widthDp = 320,
        uiMode = UI_MODE_NIGHT_YES,
        name = "DefaultPreviewDark"
    )
    @Preview(showBackground = true, widthDp = 320)
    @Composable
    fun DefaultPreview() {
        TemplateTheme {
              Greetings()
        }
    }

    @Composable
    fun OnboardingScreen(onContinueClicked: () -> Unit) {
        Surface {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text("Welcome to Onboarding screen!")
                Button(
                    modifier = Modifier.padding(vertical = 24.dp),
                    onClick = onContinueClicked
                ) {
                    Text("Continue")
                }
            }
        }
    }

    @Preview(
        showBackground = true, widthDp = 320, heightDp = 320,
        uiMode = UI_MODE_NIGHT_YES,
        name = "OnboardingPreviewDark"
    )
    @Preview(showBackground = true, widthDp = 320, heightDp = 320)
    @Composable
    fun OnboardingPreview() {
        TemplateTheme {
            OnboardingScreen(onContinueClicked = {})
        }
    }
}



