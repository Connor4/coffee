package com.inno.coffee.ui.home

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.inno.coffee.ui.theme.CoffeeTheme

//const val INSTALL_SETTING = "install_setting"
//const val DRINK_PAGE = "drink_page"

//@Composable
//fun NavContent() {
//    val navController = rememberNavController()
//    val preferencesManager = PreferencesManager.getInstance(LocalContext.current)
//    var isFirstLaunch by rememberSaveable {
//        mutableStateOf(preferencesManager.isFirstInstall)
//    }
//
//    if (true) {
//        NavHost(navController = navController, startDestination = INSTALL_SETTING) {
//            composable(INSTALL_SETTING) {
//                InstallSetting {
//                    preferencesManager.isFirstInstall = false
//                    isFirstLaunch = false
//                    navController.navigate(DRINK_PAGE) {
//                        popUpTo(INSTALL_SETTING) { inclusive = true }
//                    }
//                }
//            }
//            composable(DRINK_PAGE) {
//                DrinkPage()
//            }
//        }
//    } else {
//        NavHost(navController = navController, startDestination = DRINK_PAGE) {
//            composable(DRINK_PAGE) {
//                DrinkPage()
//            }
//        }
//    }
//}

//@Composable
//fun InstallSetting(onSetComplete: () -> Unit) {
//    Column {
//        Text(text = "Settings Page", modifier = Modifier.padding(start = 10.dp, top = 20.dp))
//        Spacer(modifier = Modifier.height(10.dp))
//        Button(onClick = onSetComplete) {
//            Text(text = "Complete Settings")
//        }
//    }
//}

//@Composable
//fun DrinkPage(
//    modifier: Modifier = Modifier,
//    viewModel: DrinksViewModel = hiltViewModel()
//) {
//    val drinksData by viewModel.drinksTypes.collectAsStateWithLifecycle()
//    DrinkList(modifier = modifier, drinksData = drinksData)
//}

@Preview
@Composable
fun ContentPreview() {
    CoffeeTheme {
    }
}