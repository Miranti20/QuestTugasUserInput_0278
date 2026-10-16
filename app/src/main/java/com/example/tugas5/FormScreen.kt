package com.example.tugas5

import android.app.DatePickerDialog
import androidx.annotation.DimenRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.toggleable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import com.example.tugas5.ui.theme.Tugas5Theme
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ComposableFormBiodata(initialBiodata: Biodata = Biodata()) {
    val context = LocalContext.current
    val focusManager = LocalFocusManager.current
    val keyboard = LocalSoftwareKeyboardController.current
    val experienceOptions = stringArrayResource(R.array.experience_options)
    val datePattern = stringResource(R.string.date_format)
    var namaDepan by rememberSaveable { mutableStateOf(initialBiodata.namaDepan) }
    var namaBelakang by rememberSaveable { mutableStateOf(initialBiodata.namaBelakang) }
    var email by rememberSaveable { mutableStateOf(initialBiodata.email) }
    var tanggalLahir by rememberSaveable { mutableStateOf(initialBiodata.tanggalLahir) }
    var experienceIndex by rememberSaveable { mutableIntStateOf(initialBiodata.experienceIndex) }
    var android by rememberSaveable { mutableStateOf(initialBiodata.android) }
    var uiUx by rememberSaveable { mutableStateOf(initialBiodata.uiUx) }
    var web by rememberSaveable { mutableStateOf(initialBiodata.web) }
    var experienceExpanded by remember { mutableStateOf(false) }
    var showSummary by rememberSaveable { mutableStateOf(false) }
    val biodata = Biodata(namaDepan, namaBelakang, email, tanggalLahir, experienceIndex, android, uiUx, web)
    val complete = biodata.isComplete(System.currentTimeMillis(), experienceOptions.size)
    val emailError = email.isNotEmpty() && !biodata.emailValid
    val formattedDate = tanggalLahir?.let {
        SimpleDateFormat(datePattern, Locale.getDefault()).format(Date(it))
    }.orEmpty()
    val fieldSpacing = dimensionResource(R.dimen.field_spacing)
    val sectionSpacing = dimensionResource(R.dimen.section_spacing)
    val cardShape = RoundedCornerShape(
        topStart = dimensionResource(R.dimen.form_corner),
        topEnd = dimensionResource(R.dimen.form_corner)
    )
    val openDatePicker: () -> Unit = {
        focusManager.clearFocus()
        keyboard?.hide()
        val selected = Calendar.getInstance().apply {
            if (tanggalLahir != null) timeInMillis = requireNotNull(tanggalLahir)
        }
        DatePickerDialog(
            context,
            { _, year, month, day ->
                tanggalLahir = Calendar.getInstance().apply {
                    clear()
                    set(year, month, day)
                }.timeInMillis
            },
            selected.get(Calendar.YEAR),
            selected.get(Calendar.MONTH),
            selected.get(Calendar.DAY_OF_MONTH)
        ).apply {
            datePicker.maxDate = System.currentTimeMillis()
        }.show()
    }

    Scaffold(containerColor = colorResource(R.color.form_background)) { innerPadding ->
        // Header dan Box form berada pada lapisan yang sama di atas Scaffold.
        Box(Modifier.fillMaxSize().padding(innerPadding).imePadding()) {
            Column(
                modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(Modifier.fillMaxWidth()) {
                    Image(
                        painter = painterResource(R.drawable.header_bg),
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxWidth().height(dimensionResource(R.dimen.header_height))
                    )
                    Box(
                        modifier = Modifier.align(Alignment.TopCenter)
                            .padding(top = dimensionResource(R.dimen.form_offset))
                            .widthIn(max = dimensionResource(R.dimen.form_max_width))
                            .fillMaxWidth()
                            .shadow(dimensionResource(R.dimen.form_elevation), cardShape)
                            .clip(cardShape)
                            .background(colorResource(R.color.form_surface))
                    ) {
                        Column(
                            Modifier.fillMaxWidth().padding(dimensionResource(R.dimen.form_padding)),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                stringResource(R.string.title_form),
                                fontSize = resourceTextSize(R.dimen.title_size),
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.primary,
                                textAlign = TextAlign.Center
                            )
                            Spacer(Modifier.height(dimensionResource(R.dimen.title_spacing)))
                            Text(
                                stringResource(R.string.subtitle_form),
                                fontSize = resourceTextSize(R.dimen.subtitle_size),
                                color = colorResource(R.color.form_subtitle),
                                textAlign = TextAlign.Center
                            )
                            Spacer(Modifier.height(dimensionResource(R.dimen.form_heading_spacing)))
                            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(fieldSpacing)) {
                                BiodataInput(
                                    value = namaDepan,
                                    onValueChange = { namaDepan = it },
                                    placeholder = stringResource(R.string.label_nama_depan),
                                    modifier = Modifier.weight(1f).testTag("firstName"),
                                    keyboardOptions = KeyboardOptions(capitalization = KeyboardCapitalization.Words, imeAction = ImeAction.Next)
                                )
                                BiodataInput(
                                    value = namaBelakang,
                                    onValueChange = { namaBelakang = it },
                                    placeholder = stringResource(R.string.label_nama_belakang),
                                    modifier = Modifier.weight(1f).testTag("lastName"),
                                    keyboardOptions = KeyboardOptions(capitalization = KeyboardCapitalization.Words, imeAction = ImeAction.Next)
                                )
                            }
                            Spacer(Modifier.height(fieldSpacing))
                            BiodataInput(
                                value = email,
                                onValueChange = { email = it },
                                placeholder = stringResource(R.string.label_email),
                                modifier = Modifier.fillMaxWidth().testTag("email"),
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email, imeAction = ImeAction.Done),
                                keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus(); keyboard?.hide() }),
                                isError = emailError
                            )
                            if (emailError) {
                                Text(
                                    stringResource(R.string.email_invalid),
                                    modifier = Modifier.fillMaxWidth().padding(top = dimensionResource(R.dimen.skill_gap)),
                                    color = MaterialTheme.colorScheme.error,
                                    style = MaterialTheme.typography.bodySmall
                                )
                            }
                            Spacer(Modifier.height(fieldSpacing))
                            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(fieldSpacing)) {
                                val dateLabel = stringResource(R.string.open_date_picker)
                                Box(Modifier.weight(1f)) {
                                    BiodataInput(
                                        value = formattedDate,
                                        onValueChange = {},
                                        placeholder = stringResource(R.string.label_tgl_lahir),
                                        modifier = Modifier.fillMaxWidth(),
                                        readOnly = true,
                                        trailingIcon = {
                                            Icon(
                                                painterResource(R.drawable.ic_calendar),
                                                contentDescription = null,
                                                modifier = Modifier.size(dimensionResource(R.dimen.icon_size))
                                            )
                                        }
                                    )
                                    // Seluruh bidang tanggal bisa diketuk, bukan hanya ikonnya.
                                    Box(
                                        Modifier.matchParentSize()
                                            .clip(RoundedCornerShape(dimensionResource(R.dimen.field_corner)))
                                            .clickable(role = Role.Button, onClick = openDatePicker)
                                            .semantics { contentDescription = dateLabel }
                                            .testTag("birthDate")
                                    )
                                }
                                ExposedDropdownMenuBox(
                                    expanded = experienceExpanded,
                                    onExpandedChange = {
                                        focusManager.clearFocus()
                                        keyboard?.hide()
                                        experienceExpanded = it
                                    },
                                    modifier = Modifier.weight(1f)
                                ) {
                                    BiodataInput(
                                        value = experienceOptions.getOrNull(experienceIndex).orEmpty(),
                                        onValueChange = {},
                                        placeholder = stringResource(R.string.label_experience),
                                        readOnly = true,
                                        modifier = Modifier.fillMaxWidth()
                                            .menuAnchor(ExposedDropdownMenuAnchorType.PrimaryNotEditable)
                                            .testTag("experience"),
                                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = experienceExpanded) }
                                    )
                                    ExposedDropdownMenu(
                                        expanded = experienceExpanded,
                                        onDismissRequest = { experienceExpanded = false }
                                    ) {
                                        experienceOptions.forEachIndexed { index, option ->
                                            DropdownMenuItem(
                                                text = { Text(option) },
                                                onClick = {
                                                    experienceIndex = index
                                                    experienceExpanded = false
                                                }
                                            )
                                        }
                                    }
                                }
                            }
                            Spacer(Modifier.height(sectionSpacing))
                            Text(
                                stringResource(R.string.section_skill),
                                modifier = Modifier.fillMaxWidth(),
                                fontSize = resourceTextSize(R.dimen.body_text_size),
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(Modifier.height(dimensionResource(R.dimen.skill_gap)))
                            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                SkillOption(stringResource(R.string.skill_android), android, { android = it }, Modifier.testTag("skillAndroid"))
                                SkillOption(stringResource(R.string.skill_uiux), uiUx, { uiUx = it }, Modifier.testTag("skillUiUx"))
                                SkillOption(stringResource(R.string.skill_web), web, { web = it }, Modifier.testTag("skillWeb"))
                            }
                            Spacer(Modifier.height(sectionSpacing))
                            Button(
                                onClick = {
                                    focusManager.clearFocus()
                                    keyboard?.hide()
                                    showSummary = true
                                },
                                enabled = complete,
                                modifier = Modifier.fillMaxWidth()
                                    .height(dimensionResource(R.dimen.button_height)).testTag("proceed"),
                                shape = RoundedCornerShape(dimensionResource(R.dimen.field_corner)),
                                colors = ButtonDefaults.buttonColors(
                                    disabledContainerColor = colorResource(R.color.form_button_disabled),
                                    disabledContentColor = colorResource(R.color.form_surface)
                                )
                            ) {
                                Text(stringResource(R.string.btn_proceed), fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
                Spacer(Modifier.height(sectionSpacing))
            }
        }
    }
    if (showSummary) {
        val skills = buildList {
            if (android) add(stringResource(R.string.skill_android))
            if (uiUx) add(stringResource(R.string.skill_uiux))
            if (web) add(stringResource(R.string.skill_web))
        }.joinToString(stringResource(R.string.summary_separator))
        AlertDialog(
            onDismissRequest = { showSummary = false },
            title = { Text(stringResource(R.string.summary_title)) },
            text = {
                Column(
                    modifier = Modifier.verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.title_spacing))
                ) {
                    Text(stringResource(R.string.summary_name, namaDepan.trim(), namaBelakang.trim()))
                    Text(stringResource(R.string.summary_email, email.trim()))
                    Text(stringResource(R.string.summary_birth_date, formattedDate))
                    Text(stringResource(R.string.summary_experience, experienceOptions.getOrNull(experienceIndex).orEmpty()))
                    Text(stringResource(R.string.summary_skills, skills))
                }
            },
            confirmButton = {
                TextButton(onClick = { showSummary = false }) { Text(stringResource(R.string.btn_close)) }
            }
        )
    }
}

@Composable
private fun BiodataInput(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    modifier: Modifier = Modifier,
    readOnly: Boolean = false,
    isError: Boolean = false,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    trailingIcon: @Composable (() -> Unit)? = null
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        placeholder = { Text(placeholder, maxLines = 1, fontSize = resourceTextSize(R.dimen.field_text_size)) },
        modifier = modifier.heightIn(min = dimensionResource(R.dimen.field_height))
            .semantics { contentDescription = placeholder },
        shape = RoundedCornerShape(dimensionResource(R.dimen.field_corner)),
        textStyle = LocalTextStyle.current.copy(fontSize = resourceTextSize(R.dimen.field_text_size)),
        singleLine = true,
        readOnly = readOnly,
        isError = isError,
        keyboardOptions = keyboardOptions,
        keyboardActions = keyboardActions,
        trailingIcon = trailingIcon
    )
}

@Composable
private fun SkillOption(label: String, checked: Boolean, onCheckedChange: (Boolean) -> Unit, modifier: Modifier = Modifier) {
    Row(
        modifier = modifier.toggleable(value = checked, role = Role.Checkbox, onValueChange = onCheckedChange),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Checkbox(checked = checked, onCheckedChange = null)
        Text(label, fontSize = resourceTextSize(R.dimen.skill_text_size))
    }
}

@Composable
private fun resourceTextSize(@DimenRes id: Int): TextUnit {
    val dimension = dimensionResource(id)
    return with(LocalDensity.current) { dimension.toSp() }
}

@Preview(showBackground = true, widthDp = 400, heightDp = 850)
@Composable
private fun FormBiodataPreview() {
    Tugas5Theme { ComposableFormBiodata() }
}
