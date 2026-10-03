package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.LocalCalcColors
import com.example.ui.theme.LocalCalcShapes

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TermsDialog(onDismiss: () -> Unit) {
    val colors = LocalCalcColors.current
    val shapes = LocalCalcShapes.current
    val scrollState = rememberScrollState()

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        containerColor = colors.surface,
        shape = shapes.sheetShape,
        dragHandle = { BottomSheetDefaults.DragHandle(color = colors.textMuted) }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.85f)
                .padding(horizontal = 24.dp)
                .padding(bottom = 20.dp)
                .testTag("dialog_terms")
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Gavel,
                        contentDescription = null,
                        tint = colors.accentPrimary
                    )
                    Text(
                        text = "Terms and Conditions",
                        color = colors.textPrimary,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
                IconButton(onClick = onDismiss) {
                    Icon(Icons.Default.Close, contentDescription = "Close", tint = colors.textMuted)
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            Column(
                modifier = Modifier
                    .weight(1f)
                    .verticalScroll(scrollState),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                LegalSection(
                    title = "1. Agreement to Terms",
                    body = "By accessing, installing, or operating NovaCalc (hereinafter referred to as \"the Software\"), you explicitly agree to comply with and be legally bound by these Terms and Conditions. If you disagree with any portion of these terms, you are prohibited from utilizing this software."
                )

                LegalSection(
                    title = "2. License & Intellectual Property",
                    body = "NovaCalc is licensed under standard open-source licenses as documented in the Licenses section. The software comprises original code alongside mathematical algorithms derived from open-source scientific projects. You are granted a personal, non-exclusive, revocable license to utilize the software for academic, professional, and personal calculations."
                )

                LegalSection(
                    title = "3. Scope of Mathematical Computations & Disclaimer",
                    body = "The Software facilitates complex numbers, linear evaluation, scientific calculations, and variable storage. While substantial effort has been made to ensure mathematical fidelity following IEEE 754 standards, all computational outputs are provided \"AS IS\" WITHOUT WARRANTY OF ANY KIND, EITHER EXPRESSED OR IMPLIED. Neither the developers nor contributors shall be held liable for any damages, errors, or financial or academic consequences arising from the use of calculation outputs."
                )

                LegalSection(
                    title = "4. Device Sandbox & Data Ownership",
                    body = "All calculation logs and custom variables remain the exclusive property of the user. Stored data is kept locally on your hardware within the app's private sandbox and is never transmitted over any network."
                )

                LegalSection(
                    title = "5. Governing Law and Amendments",
                    body = "These Terms and Conditions may be modified with subsequent updates of the application. Continued usage of the Software following any revisions constitutes full acceptance of the revised Terms."
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PrivacyPolicyDialog(onDismiss: () -> Unit) {
    val colors = LocalCalcColors.current
    val shapes = LocalCalcShapes.current
    val scrollState = rememberScrollState()

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        containerColor = colors.surface,
        shape = shapes.sheetShape,
        dragHandle = { BottomSheetDefaults.DragHandle(color = colors.textMuted) }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.85f)
                .padding(horizontal = 24.dp)
                .padding(bottom = 20.dp)
                .testTag("dialog_privacy")
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Shield,
                        contentDescription = null,
                        tint = colors.accentTertiary
                    )
                    Text(
                        text = "Privacy Policy",
                        color = colors.textPrimary,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
                IconButton(onClick = onDismiss) {
                    Icon(Icons.Default.Close, contentDescription = "Close", tint = colors.textMuted)
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            Column(
                modifier = Modifier
                    .weight(1f)
                    .verticalScroll(scrollState),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Surface(
                    shape = shapes.cardShape,
                    color = colors.accentTertiary.copy(alpha = 0.12f),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.CheckCircle,
                            contentDescription = null,
                            tint = colors.accentTertiary,
                            modifier = Modifier.size(24.dp)
                        )
                        Text(
                            text = "100% Offline & Private: NovaCalc does not collect, record, transmit, or share any personal information or telemetry.",
                            color = colors.accentTertiary,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }

                LegalSection(
                    title = "1. Information Collection and Telemetry",
                    body = "NovaCalc does not gather any personal identifiers, device hardware IDs, IP addresses, location data, or mathematical queries. There are zero third-party advertising SDKs, zero behavioral trackers, and zero analytical trackers incorporated into this application."
                )

                LegalSection(
                    title = "2. Local Database Storage",
                    body = "Your historical calculation logs and saved variables are persisted strictly locally inside an SQLite database managed by Android's Room library within the app's isolated sandbox storage. No calculation ever leaves your device."
                )

                LegalSection(
                    title = "3. System Permissions",
                    body = "• VIBRATE (android.permission.VIBRATE): Utilized solely to provide optional haptic tactile feedback when tapping calculator buttons. This can be toggled off at any time in Settings."
                )

                LegalSection(
                    title = "4. Data Retention & Deletion",
                    body = "You have full autonomy over your stored data. You can clear calculation history logs or delete all saved variables at any time from the Settings menu or by clearing the app's storage in Android Settings."
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LicensesDialog(onDismiss: () -> Unit) {
    val colors = LocalCalcColors.current
    val shapes = LocalCalcShapes.current
    val scrollState = rememberScrollState()

    val apacheLicense2FullText = """
TERMS AND CONDITIONS FOR USE, REPRODUCTION, AND DISTRIBUTION

1. Definitions.
"License" shall mean the terms and conditions for use, reproduction, and distribution as defined by Sections 1 through 9 of this document.
"Licensor" shall mean the copyright owner or entity authorized by the copyright owner that is granting the License.
"Legal Entity" shall mean the union of the acting entity and all other entities that control, are controlled by, or are under common control with that entity.
"You" (or "Your") shall mean an individual or Legal Entity exercising permissions granted by this License.
"Source" form shall mean the preferred form for making modifications, including but not limited to software source code, documentation source, and configuration files.
"Object" form shall mean any form resulting from mechanical transformation or translation of a Source form, including but not limited to compiled object code, generated documentation, and conversions to other media types.
"Work" shall mean the work of authorship, whether in Source or Object form, made available under the License.

2. Grant of Copyright License.
Subject to the terms and conditions of this License, each Contributor hereby grants to You a perpetual, worldwide, non-exclusive, no-charge, royalty-free, irrevocable copyright license to reproduce, prepare Derivative Works of, publicly display, publicly perform, sublicense, and distribute the Work and such Derivative Works in Source or Object form.

3. Grant of Patent License.
Subject to the terms and conditions of this License, each Contributor hereby grants to You a perpetual, worldwide, non-exclusive, no-charge, royalty-free, irrevocable (except as stated in this section) patent license to make, have made, use, offer to sell, sell, import, and otherwise transfer the Work.

4. Redistribution.
You may reproduce and distribute copies of the Work or Derivative Works thereof in any medium, with or without modifications, and in Source or Object form, provided that You meet the following conditions:
(a) You must give any other recipients of the Work or Derivative Works a copy of this License; and
(b) You must cause any modified files to carry prominent notices stating that You changed the files; and
(c) You must retain, in the Source form of any Derivative Works that You distribute, all copyright, patent, trademark, and attribution notices from the Source form of the Work; and
(d) If the Work includes a "NOTICE" text file as part of its distribution, then any Derivative Works that You distribute must include a readable copy of the attribution notices contained within such NOTICE file.

5. Disclaimer of Warranty.
Unless required by applicable law or agreed to in writing, Licensor provides the Work on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied, including, without limitation, any warranties or conditions of TITLE, NON-INFRINGEMENT, MERCHANTABILITY, or FITNESS FOR A PARTICULAR PURPOSE.

6. Limitation of Liability.
In no event and under no legal theory, whether in tort (including negligence), contract, or otherwise, unless required by applicable law, shall any Contributor be liable to You for damages, including any direct, indirect, special, incidental, or consequential damages of any character arising as a result of this License or out of the use or inability to use the Work.
    """.trimIndent()

    val mitLicenseFullText = """
The MIT License (MIT)

Copyright (c) 2020 Alex Barry (AlexCalc Project)

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
    """.trimIndent()

    var expandedLicense by remember { mutableStateOf<String?>("AlexCalc Math Engine (MIT)") }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        containerColor = colors.surface,
        shape = shapes.sheetShape,
        dragHandle = { BottomSheetDefaults.DragHandle(color = colors.textMuted) }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.88f)
                .padding(horizontal = 24.dp)
                .padding(bottom = 20.dp)
                .testTag("dialog_licenses")
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Description,
                        contentDescription = null,
                        tint = colors.accentSecondary
                    )
                    Text(
                        text = "Open Source Licenses",
                        color = colors.textPrimary,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
                IconButton(onClick = onDismiss) {
                    Icon(Icons.Default.Close, contentDescription = "Close", tint = colors.textMuted)
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = "Full legal license texts for open source libraries and mathematical engines incorporated in NovaCalc:",
                color = colors.textMuted,
                fontSize = 12.sp
            )

            Spacer(modifier = Modifier.height(14.dp))

            Column(
                modifier = Modifier
                    .weight(1f)
                    .verticalScroll(scrollState),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                FullLicenseCard(
                    title = "AlexCalc Math Engine (MIT)",
                    subtitle = "Copyright (c) 2020 Alex Barry",
                    licenseText = mitLicenseFullText,
                    isExpanded = expandedLicense == "AlexCalc Math Engine (MIT)",
                    onToggle = {
                        expandedLicense = if (expandedLicense == "AlexCalc Math Engine (MIT)") null else "AlexCalc Math Engine (MIT)"
                    }
                )

                FullLicenseCard(
                    title = "Android Jetpack Compose & UI",
                    subtitle = "Apache License 2.0 • The Android Open Source Project",
                    licenseText = "Copyright 2024 The Android Open Source Project\n\n$apacheLicense2FullText",
                    isExpanded = expandedLicense == "Android Jetpack Compose & UI",
                    onToggle = {
                        expandedLicense = if (expandedLicense == "Android Jetpack Compose & UI") null else "Android Jetpack Compose & UI"
                    }
                )

                FullLicenseCard(
                    title = "Kotlin Coroutines & Standard Library",
                    subtitle = "Apache License 2.0 • JetBrains s.r.o.",
                    licenseText = "Copyright 2010-2024 JetBrains s.r.o. and Kotlin Programming Language contributors.\n\n$apacheLicense2FullText",
                    isExpanded = expandedLicense == "Kotlin Coroutines & Standard Library",
                    onToggle = {
                        expandedLicense = if (expandedLicense == "Kotlin Coroutines & Standard Library") null else "Kotlin Coroutines & Standard Library"
                    }
                )

                FullLicenseCard(
                    title = "AndroidX Room Database",
                    subtitle = "Apache License 2.0 • The Android Open Source Project",
                    licenseText = "Copyright 2024 The Android Open Source Project\n\n$apacheLicense2FullText",
                    isExpanded = expandedLicense == "AndroidX Room Database",
                    onToggle = {
                        expandedLicense = if (expandedLicense == "AndroidX Room Database") null else "AndroidX Room Database"
                    }
                )

                FullLicenseCard(
                    title = "Material Design 3 Components",
                    subtitle = "Apache License 2.0 • Google LLC",
                    licenseText = "Copyright 2024 Google LLC\n\n$apacheLicense2FullText",
                    isExpanded = expandedLicense == "Material Design 3 Components",
                    onToggle = {
                        expandedLicense = if (expandedLicense == "Material Design 3 Components") null else "Material Design 3 Components"
                    }
                )
            }
        }
    }
}

@Composable
private fun FullLicenseCard(
    title: String,
    subtitle: String,
    licenseText: String,
    isExpanded: Boolean,
    onToggle: () -> Unit
) {
    val colors = LocalCalcColors.current
    val shapes = LocalCalcShapes.current

    Surface(
        shape = shapes.cardShape,
        color = colors.surfaceElevated,
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onToggle() }
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = title,
                        color = colors.textPrimary,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = subtitle,
                        color = colors.textMuted,
                        fontSize = 12.sp
                    )
                }
                Icon(
                    imageVector = if (isExpanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                    contentDescription = null,
                    tint = colors.accentPrimary
                )
            }

            AnimatedVisibility(visible = isExpanded) {
                Column(modifier = Modifier.padding(top = 12.dp)) {
                    Divider(color = colors.border.copy(alpha = 0.5f))
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = licenseText,
                        color = colors.textMuted,
                        fontSize = 11.sp,
                        fontFamily = FontFamily.Monospace,
                        lineHeight = 16.sp
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AboutDialog(onDismiss: () -> Unit) {
    val colors = LocalCalcColors.current
    val shapes = LocalCalcShapes.current
    val scrollState = rememberScrollState()

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        containerColor = colors.surface,
        shape = shapes.sheetShape,
        dragHandle = { BottomSheetDefaults.DragHandle(color = colors.textMuted) }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.85f)
                .padding(horizontal = 24.dp)
                .padding(bottom = 24.dp)
                .verticalScroll(scrollState)
                .testTag("dialog_about"),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Official App Icon following dynamic corner styling
            Surface(
                shape = shapes.cardShape,
                color = androidx.compose.ui.graphics.Color(0xFF1A222D),
                border = androidx.compose.foundation.BorderStroke(1.dp, colors.border),
                modifier = Modifier.size(84.dp)
            ) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    androidx.compose.foundation.Image(
                        painter = androidx.compose.ui.res.painterResource(id = com.example.R.drawable.ic_launcher_foreground),
                        contentDescription = "NovaCalc App Icon",
                        modifier = Modifier.fillMaxSize()
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = "NovaCalc",
                color = colors.textPrimary,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "Version 1.2.0 • Scientific & Complex Math",
                color = colors.textMuted,
                fontSize = 13.sp
            )

            Spacer(modifier = Modifier.height(18.dp))

            // Dedicated Credits To The Owner Card
            Surface(
                shape = shapes.cardShape,
                color = colors.accentPrimary.copy(alpha = 0.12f),
                border = androidx.compose.foundation.BorderStroke(1.dp, colors.accentPrimary.copy(alpha = 0.4f)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Favorite,
                            contentDescription = null,
                            tint = colors.accentPrimary,
                            modifier = Modifier.size(18.dp)
                        )
                        Text(
                            text = "Credits To The Owner",
                            color = colors.accentPrimary,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Text(
                        text = "NovaCalc's mathematical engine architecture and expression evaluation logic is based upon and inspired by the open-source AlexCalc project developed by Alex Barry.",
                        color = colors.textPrimary,
                        fontSize = 13.sp,
                        lineHeight = 18.sp
                    )

                    Surface(
                        shape = shapes.pillShape,
                        color = colors.surfaceElevated,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(10.dp)) {
                            Text(
                                text = "Original Project: AlexCalc",
                                color = colors.accentPrimary,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "Author: Alex Barry",
                                color = colors.textMuted,
                                fontSize = 12.sp
                            )
                            Text(
                                text = "Repository: https://github.com/alexbarry/AlexCalc",
                                color = colors.accentSecondary,
                                fontSize = 11.sp,
                                fontFamily = FontFamily.Monospace
                            )
                        }
                    }

                    Text(
                        text = "We express our sincere appreciation and gratitude to Alex Barry and the AlexCalc open-source contributors for their mathematical foundation.",
                        color = colors.textMuted,
                        fontSize = 12.sp,
                        fontStyle = androidx.compose.ui.text.font.FontStyle.Italic
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            Surface(
                shape = shapes.cardShape,
                color = colors.surfaceElevated,
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = "Core Engine Features",
                        color = colors.accentPrimary,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "• Complex algebra: z = a + bi, modulus |z|, phase arg(z), and conjugate z*\n• Trigonometric & hyperbolic evaluation in Radians and Degrees\n• Dynamic variables & formula evaluation\n• Local-only Room database historical logging\n• 5 customizable modern dark aesthetics with dynamic corner styles",
                        color = colors.textMuted,
                        fontSize = 13.sp,
                        lineHeight = 19.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(18.dp))

            Button(
                onClick = onDismiss,
                colors = ButtonDefaults.buttonColors(containerColor = colors.keyOpBg),
                shape = shapes.pillShape,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Close", color = colors.accentPrimary, fontWeight = FontWeight.Bold)
            }
        }
    }
}

@Composable
private fun LegalSection(title: String, body: String) {
    val colors = LocalCalcColors.current
    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
        Text(
            text = title,
            color = colors.textPrimary,
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = body,
            color = colors.textMuted,
            fontSize = 13.sp,
            lineHeight = 18.sp
        )
    }
}
