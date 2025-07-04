package com.l0123137.tesprojek.ui.screen.signup

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.l0123137.tesprojek.R
import com.l0123137.tesprojek.ToDuhApplication
import com.l0123137.tesprojek.ui.ViewModelFactory
import kotlinx.coroutines.flow.collectLatest
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.runtime.LaunchedEffect

@Composable
fun SignUpScreen(
    navController: NavController
) {
    val application = LocalContext.current.applicationContext as ToDuhApplication
    val viewModel: SignUpViewModel = viewModel(
        factory = ViewModelFactory(
            application.userRepository,
            application.sessionRepository,
            application.eventRepository
        )
    )

    val state = viewModel.uiState

    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(Unit) {
        viewModel.snackbarMessage.collectLatest { message ->
            snackbarHostState.showSnackbar(
                message = message,
                withDismissAction = true,
                duration = SnackbarDuration.Indefinite
            )
            navController.popBackStack()
        }
    }

    var errorMessage by remember { mutableStateOf("") }

    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding) // Gunakan padding dari Scaffold
                .background(MaterialTheme.colorScheme.background)
                .verticalScroll(rememberScrollState())
                .padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(id = R.drawable.logo_to_duh),
                contentDescription = "Logo",
                modifier = Modifier
                    .height(100.dp)
                    .scale(2f)
                    .padding(10.dp, 4.dp, 10.dp, 0.dp)
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Asumsi CustomTextField, CustomDatePicker, dan CustomPasswordField sudah ada
                // di file terpisah atau di file yang sama.
                CustomTextField(
                    label = "First Name",
                    value = state.firstName,
                    onValueChange = viewModel::onFirstNameChange,
                    error = state.firstNameError,
                    modifier = Modifier.weight(1f)
                )
                CustomTextField(
                    label = "Last Name",
                    value = state.lastName,
                    onValueChange = viewModel::onLastNameChange,
                    error = state.lastNameError,
                    modifier = Modifier.weight(1f)
                )
            }

            CustomTextField(
                label = "Username",
                value = state.username,
                onValueChange = viewModel::onUsernameChange,
                error = state.usernameError,
                modifier = Modifier.fillMaxWidth()
            )

            CustomDatePicker(
                selectedDate = state.bornDate,
                onDateSelected = viewModel::onBornDateChanged,
                showError = state.bornDateError,
                modifier = Modifier.fillMaxWidth()
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                CustomPasswordField(
                    label = "Password",
                    value = state.password,
                    onValueChange = viewModel::onPasswordChange,
                    error = state.passwordError,
                    modifier = Modifier.weight(1f)
                )
                CustomPasswordField(
                    label = "Verify Password",
                    value = state.confirmPassword,
                    onValueChange = viewModel::onConfirmPasswordChange,
                    error = state.confirmPasswordError,
                    modifier = Modifier.weight(1f)
                )
            }

            state.errorMessage?.let { msg ->
                Text(
                    text = msg,
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentWidth(Alignment.CenterHorizontally)
                )
            }

            if (listOf(
                    state.firstNameError,
                    state.lastNameError,
                    state.usernameError,
                    state.bornDateError,
                    state.passwordError,
                    state.confirmPasswordError
                ).any { it }) {
                Text(
                    text = "*This field is required",
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentWidth(Alignment.Start)
                )
            }

            Button(
                onClick = { viewModel.onSubmit() },
                modifier = Modifier.fillMaxWidth(),
                shape = androidx.compose.foundation.shape.RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondaryContainer)
            ) {
                Text(text = "Sign up", color = MaterialTheme.colorScheme.onPrimaryContainer)
            }

            TextButton(onClick = { navController.navigate("login") }) {
                Text(
                    text = "Already have an account? Login here!",
                    fontSize = 15.sp,
                    color = MaterialTheme.colorScheme.onBackground
                )
            }
        }
    }
}
