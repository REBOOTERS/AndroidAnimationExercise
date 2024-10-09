package com.engineer.compose.ui


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.compose.ui.res.stringResource
import androidx.fragment.app.Fragment
import com.alibaba.android.arouter.facade.annotation.Route
import com.engineer.compose.ui.ui.theme.MiniAppTheme
import com.engineer.imitate.R

/**
 * A simple [Fragment] subclass.
 *
 */
@Route(path = "/anim/compose")
class ComposeFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_compose, container, false)
        val composeView = view.findViewById<ComposeView>(R.id.compose_view)
        composeView.apply {
            // Dispose of the Composition when the view's LifecycleOwner
            // is destroyed
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setContent {
//                SimpleScreen()
                MiniAppTheme {
                    // A surface container using the 'background' color from the theme
                    Surface(color = MaterialTheme.colorScheme.background) {
                        MessageCard(Message("lucy", "hello world"))
                    }
                }
            }
        }
        return view
    }
}

//@Composable
//private fun SimpleScreen() {
//    Column(Modifier.fillMaxSize()) {
//        Text(
//            text = stringResource(R.string.content),
//            style = MaterialTheme.typography.headlineMedium
//        )
//        Text(
//            text = stringResource(R.string.content),
//            style = MaterialTheme.typography.headlineSmall
//        )
//        Text(
//            text = stringResource(R.string.content),
//            style = MaterialTheme.typography.bodyMedium
//        )
//        Spacer(modifier = Modifier.weight(1f))
//        Button(onClick = { /* Handle click */ }, Modifier.fillMaxWidth()) {
//            Text(text = stringResource(R.string.content))
//        }
//    }
//}

