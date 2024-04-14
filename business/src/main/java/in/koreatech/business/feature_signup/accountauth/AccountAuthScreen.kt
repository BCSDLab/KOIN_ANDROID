package `in`.koreatech.business.feature_signup.accountauth

/*
@Composable
fun AccountAuthScreen(
    modifier: Modifier = Modifier,
    email: String,
    onBackClicked: () -> Unit = {},
    onNextClicked: () -> Unit = {},
) {
    var authCode by remember { mutableStateOf("") }
    Column(
        modifier = modifier,
    ) {
        IconButton(
            modifier = Modifier.padding(vertical = 24.dp),
            onClick = { onBackClicked() }
        ) {
            Icon(
                modifier = Modifier.padding(start = 10.dp),
                painter = painterResource(id = R.drawable.ic_arrow_back),
                contentDescription = stringResource(id = R.string.back_icon),
            )
        }
        Column(
            modifier = Modifier
                .padding(horizontal = 32.dp),
            verticalArrangement = Arrangement.Center,
        ) {
            Text(
                text = stringResource(id = R.string.master_sign_up),
                fontSize = 24.sp,
                fontWeight = Bold,
            )
            Spacer(modifier = Modifier.height(40.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    modifier = Modifier,
                    color = ColorSecondary,
                    text = stringResource(id = R.string.account_authentication)
                )
                Text(text = stringResource(id = R.string.two_third))
            }
            Canvas(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                drawLine(
                    color = ColorUnarchived,
                    start = Offset(0f - 40, 0f),
                    end = Offset(size.width + 35, size.height),
                    strokeWidth = 4.dp.toPx(),
                    cap = StrokeCap.Round
                )
                drawLine(
                    color = ColorSecondary,
                    start = Offset(0f - 40, 0f),
                    end = Offset((size.width + 40) * 2 / 3, size.height),
                    strokeWidth = 4.dp.toPx(),
                    cap = StrokeCap.Round
                )
            }
            Spacer(modifier = Modifier.height(32.dp))
            Row {
                Text(
                    text = email,
                    fontSize = 15.sp,
                    color = ColorSecondary,
                )
                Text(
                    text = stringResource(id = R.string.by),
                    fontSize = 15.sp,
                    color = ColorDescription,
                )
            }
            Text(
                text = stringResource(id = R.string.verification_code_prompt),
                fontSize = 15.sp,
                color = ColorDescription,
            )
            Spacer(modifier = Modifier.height(38.dp))
            AuthTextField(
                value = authCode,
                onValueChange = { authCode = it },
                modifier = Modifier.fillMaxWidth(),
                label = stringResource(id = R.string.enter_verification_code),
                textStyle = TextStyle.Default.copy(fontSize = 20.sp),
                isPassword = true,
                isError = state.signUpContinuationError != null,
            )
            Spacer(modifier = Modifier.height(8.dp))
            CountdownTimer()
            Spacer(modifier = Modifier.height(183.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {

                Button(modifier = Modifier
                    .width(141.dp)
                    .height(44.dp),
                    shape = RectangleShape,
                    colors = ButtonDefaults.buttonColors(
                        contentColor = Color.White,
                        disabledContentColor = Color.White,
                    ),
                    onClick = { }
                ) {
                    Text(text = stringResource(id = R.string.resend))
                }
                Spacer(modifier = Modifier.width(14.dp))

                Button(modifier = Modifier
                    .width(141.dp)
                    .height(44.dp),
                    shape = RectangleShape,
                    colors = ButtonDefaults.buttonColors(
                        contentColor = Color.White,
                        disabledContentColor = Color.White,
                    ),
                    onClick = {
                        if (state.signupContinuationState == SignupContinuationState.CheckComplete)
                            emailAuthViewModel.onNextButtonClicked()
                        emailAuthViewModel.verifyEmail(email, state.authCode)  }) {
                    Text(text = stringResource(id = R.string.next))
                }
            }
        }
    }
    emailAuthViewModel.collectSideEffect {
        when (it) {
            is EmailAuthSideEffect.NavigateToNextScreen -> onNextClicked()
            EmailAuthSideEffect.NavigateToBackScreen -> onBackClicked()
        }
    }
}


@Composable
fun CountdownTimer() {

    var timeLeft by remember { mutableStateOf(300) }
    var minutes by remember { mutableStateOf(0) }
    var seconds by remember { mutableStateOf(0) }

    LaunchedEffect(key1 = timeLeft) {
        while (timeLeft > 0) {
            delay(1000L)
            timeLeft--
            minutes = timeLeft / 60
            seconds = timeLeft % 60
        }
    }

    val formattedTimeLeft = "%02d".format(minutes)
    val formattedSeconds = "%02d".format(seconds)

    Text(
        modifier = Modifier.fillMaxWidth(),
        textAlign = TextAlign.Center,
        text = stringResource(id = R.string.time_limit) + "$formattedTimeLeft : $formattedSeconds"
    )
}
*/