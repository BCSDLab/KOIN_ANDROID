# CORE DesignSystem Module - AGENTS.md

This file provides prescriptive coding guidelines for AI coding agents working in the CORE DESIGNSYSTEM module of the KOIN_ANDROID repository.

## Module Overview

The `core:designsystem` module provides the unified design language and reusable UI components for the KOIN_ANDROID application. It implements the "Koin" design system using Jetpack Compose and Material 3.

## Core Responsibilities

1. **Design Tokens**: Colors, typography, shapes, and spacing
2. **UI Components**: Reusable Composable components
3. **Theme System**: Light/dark theme support
4. **Component Extensions**: Compose utilities and helpers
5. **Design Guidelines**: Consistent visual patterns

## Package Structure

```
core/designsystem/src/main/java/in/koreatech/koin/core/designsystem/
├── theme/
│   ├── Color.kt              # Color definitions and palettes
│   ├── Theme.kt              # Main theme composable
│   ├── Type.kt               # Typography definitions
│   └── Shape.kt              # Shape definitions
├── component/
│   ├── button/               # Button components
│   ├── dialog/               # Dialog components
│   ├── chip/                 # Chip components
│   ├── navigation/           # Navigation components
│   └── [other]/              # Other UI components
├── foundation/
│   ├── ComposeExtensions.kt   # Compose utilities
│   └── EdgeToEdgeExtensions.kt # System UI utilities
└── icon/                     # Icon resources and definitions
```

## Implementation Patterns

### Theme System Pattern

**MUST** provide consistent theme structure:

```kotlin
@Composable
fun RebrandKoinTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) {
        DarkKoinColorPalette
    } else {
        LightKoinColorPalette
    }

    CompositionLocalProvider(
        LocalKoinColorPalette provides colorScheme,
        LocalKoinTypography provides KoinTypography,
        LocalShapes provides Shapes,
        LocalSpacing provides Spacing
    ) {
        MaterialTheme(
            colorScheme = colorScheme.toMaterialColorScheme(),
            typography = KoinTypography.toMaterialTypography(),
            shapes = Shapes,
            content = content
        )
    }
}
```

**Rules**:
- **MUST** provide both light and dark themes
- **MUST** use CompositionLocalProvider for custom tokens
- **MUST** wrap MaterialTheme to provide consistency
- **SHOULD** detect system dark theme by default

### Color Pattern

The design system uses **scale-based color naming** (not semantic naming). Colors are organized by category with numeric intensity scales.

**MUST** follow the scale-based color structure:

```kotlin
// ColorPalette.kt - Actual implementation
data class KoinColorPalette(
    // Primary colors (brand blue) - 900 to 100 scale
    val primary900: Color,
    val primary800: Color,
    val primary700: Color,
    val primary600: Color,
    val primary500: Color,
    val primary400: Color,
    val primary300: Color,
    val primary200: Color,
    val primary100: Color,
    
    // Sub colors (secondary purple) - 900 to 100 scale
    val sub900: Color,
    val sub800: Color,
    val sub700: Color,
    val sub600: Color,
    val sub500: Color,
    val sub400: Color,
    val sub300: Color,
    val sub200: Color,
    val sub100: Color,
    
    // Neutral colors (grayscale) - 800 to 0 scale
    val neutral800: Color,
    val neutral700: Color,
    val neutral600: Color,
    val neutral500: Color,
    val neutral400: Color,
    val neutral300: Color,
    val neutral200: Color,
    val neutral100: Color,
    val neutral50: Color,
    val neutral0: Color,
    
    // Status colors - 700 to 50 scale
    val danger700: Color,
    val danger600: Color,
    val danger500: Color,
    val danger100: Color,
    val danger50: Color,
    
    val warning700: Color,
    val warning600: Color,
    val warning500: Color,
    val warning100: Color,
    val warning50: Color,
    
    val success700: Color,
    val success600: Color,
    val success500: Color,
    val success100: Color,
    val success50: Color,
    
    val info700: Color,
    val info600: Color,
    val info500: Color,
    val info100: Color,
    val info50: Color
)
```

**Four color palettes are defined** in `Color.kt`:

```kotlin
// Color.kt - Light theme (rebrand)
val RebrandKoinLightColorPalette = KoinColorPalette(
    primary900 = Color(0xFF002B5C),
    primary800 = Color(0xFF003D7A),
    primary700 = Color(0xFF005099),
    primary600 = Color(0xFF1766AC),
    primary500 = Color(0xFF4D8FCA),
    primary400 = Color(0xFF83B8E8),
    primary300 = Color(0xFFB0D4F5),
    primary200 = Color(0xFFD6EAFC),
    primary100 = Color(0xFFF0F7FF),
    // ... (other colors follow same pattern)
)

// Color.kt - Dark theme (rebrand)
val RebrandKoinDarkColorPalette = KoinColorPalette(
    // Dark theme inverts some scales for proper contrast
    primary900 = Color(0xFFF0F7FF),
    primary800 = Color(0xFFD6EAFC),
    // ...
)

// Legacy palettes (for backward compatibility)
val KoinLightColorPalette = KoinColorPalette(...)
val KoinDarkColorPalette = KoinColorPalette(...)
```

**Color usage examples**:
```kotlin
@Composable
fun ExampleComponent() {
    val colors = LocalKoinColorPalette.current
    
    // Primary actions and emphasis
    Button(colors = ButtonDefaults.buttonColors(
        containerColor = colors.primary500
    )) { ... }
    
    // Text hierarchy
    Text(color = colors.neutral800)  // Primary text
    Text(color = colors.neutral500)  // Secondary text
    Text(color = colors.neutral400)  // Placeholder text
    
    // Status indicators
    Text(color = colors.danger500)   // Error text
    Text(color = colors.success500)  // Success text
    
    // Backgrounds
    Surface(color = colors.neutral0) { ... }  // White background
    Surface(color = colors.neutral50) { ... } // Subtle background
}
```

**Rules**:
- **MUST** use scale-based naming (`primary500`, not `primary`)
- **MUST** use numeric scales consistently (higher = darker in light theme)
- **MUST** create both light and dark variants with appropriate inversions
- **MUST** use `LocalKoinColorPalette.current` to access colors
- **NEVER** hardcode hex values in components

### Typography Pattern

The design system uses **weight+size naming** with the **Pretendard** font family (not Material 3 scale naming).

**MUST** follow the weight+size typography structure:

```kotlin
// Type.kt - Font family definition
internal val Pretendard = FontFamily(
    Font(R.font.pretendard_bold, FontWeight.Bold, FontStyle.Normal),
    Font(R.font.pretendard_medium, FontWeight.Medium, FontStyle.Normal),
    Font(R.font.pretendard_regular, FontWeight.Normal, FontStyle.Normal)
)

// Type.kt - Typography data class
data class KoinTypography(
    // Regular weight (FontWeight.Normal) - sizes 10, 12, 13, 14, 15, 16, 18
    val regular10: TextStyle,
    val regular12: TextStyle,
    val regular13: TextStyle,
    val regular14: TextStyle,
    val regular15: TextStyle,
    val regular16: TextStyle,
    val regular18: TextStyle,
    
    // Medium weight (FontWeight.Medium) - sizes 12, 13, 14, 15, 16, 18
    val medium12: TextStyle,
    val medium13: TextStyle,
    val medium14: TextStyle,
    val medium15: TextStyle,
    val medium16: TextStyle,
    val medium18: TextStyle,
    
    // Bold weight (FontWeight.Bold) - sizes 12, 13, 14, 15, 16, 18, 20
    val bold12: TextStyle,
    val bold13: TextStyle,
    val bold14: TextStyle,
    val bold15: TextStyle,
    val bold16: TextStyle,
    val bold18: TextStyle,
    val bold20: TextStyle
)

// Type.kt - Typography instance
internal val Typography = KoinTypography(
    regular10 = TextStyle(
        fontFamily = Pretendard,
        fontWeight = FontWeight.Normal,
        fontSize = 10.sp,
        lineHeight = 15.sp
    ),
    regular12 = TextStyle(
        fontFamily = Pretendard,
        fontWeight = FontWeight.Normal,
        fontSize = 12.sp,
        lineHeight = 18.sp
    ),
    // ... other styles follow same pattern
    
    medium14 = TextStyle(
        fontFamily = Pretendard,
        fontWeight = FontWeight.Medium,
        fontSize = 14.sp,
        lineHeight = 21.sp
    ),
    
    bold16 = TextStyle(
        fontFamily = Pretendard,
        fontWeight = FontWeight.Bold,
        fontSize = 16.sp,
        lineHeight = 24.sp
    )
    // ... etc.
)
```

**Typography usage examples**:
```kotlin
@Composable
fun ExampleComponent() {
    val typography = LocalKoinTypography.current
    
    // Headlines and titles
    Text(
        text = "Page Title",
        style = typography.bold20
    )
    
    // Section headers
    Text(
        text = "Section Header",
        style = typography.bold16
    )
    
    // Body text
    Text(
        text = "Body content goes here",
        style = typography.regular14
    )
    
    // Button text
    Text(
        text = "Button",
        style = typography.medium14
    )
    
    // Captions and labels
    Text(
        text = "Small label",
        style = typography.regular12
    )
}
```

**Rules**:
- **MUST** use weight+size naming (`bold16`, not `titleLarge`)
- **MUST** use Pretendard font family for all text
- **MUST** include appropriate lineHeight (typically 1.5x fontSize)
- **MUST** use `LocalKoinTypography.current` to access styles
- **NEVER** create TextStyle inline with hardcoded values
- **NEVER** use `FontFamily.Default` - always use `Pretendard`

### Button Component Pattern

**MUST** create accessible, consistent buttons using scale-based tokens:

```kotlin
@Composable
fun FilledButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    loading: Boolean = false,
    icon: ImageVector? = null
) {
    val colors = LocalKoinColorPalette.current
    val typography = LocalKoinTypography.current
    
    Button(
        onClick = onClick,
        modifier = modifier,
        enabled = enabled && !loading,
        colors = ButtonDefaults.buttonColors(
            containerColor = colors.primary500,
            contentColor = colors.neutral0,
            disabledContainerColor = colors.neutral200,
            disabledContentColor = colors.neutral400
        ),
        shape = RoundedCornerShape(8.dp)
    ) {
        if (loading) {
            CircularProgressIndicator(
                modifier = Modifier.size(16.dp),
                strokeWidth = 2.dp,
                color = colors.neutral0
            )
        } else {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                icon?.let {
                    Icon(
                        imageVector = it,
                        contentDescription = null,
                        modifier = Modifier.size(18.dp),
                        tint = colors.neutral0
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                }
                Text(
                    text = text,
                    style = typography.medium14,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}
```

**Rules**:
- **MUST** use scale-based color tokens (e.g., `colors.primary500`, `colors.neutral0`)
- **MUST** use weight+size typography tokens (e.g., `typography.medium14`)
- **MUST** provide loading state
- **MUST** handle overflow text gracefully
- **SHOULD** support icons
- **MUST** be accessible (proper content descriptions)

### Dialog Component Pattern

**MUST** create consistent dialog experiences using scale-based tokens:

```kotlin
@Composable
fun ChoiceDialog(
    title: String,
    message: String,
    confirmText: String = "Confirm",
    dismissText: String = "Cancel",
    onConfirm: () -> Unit,
    onDismiss: () -> Unit,
    onDismissRequest: () -> Unit,
    modifier: Modifier = Modifier
) {
    val colors = LocalKoinColorPalette.current
    val typography = LocalKoinTypography.current
    
    AlertDialog(
        onDismissRequest = onDismissRequest,
        modifier = modifier,
        title = {
            Text(
                text = title,
                style = typography.bold18,
                color = colors.neutral800
            )
        },
        text = {
            Text(
                text = message,
                style = typography.regular14,
                color = colors.neutral600
            )
        },
        confirmButton = {
            TextButton(onClick = onConfirm) {
                Text(
                    text = confirmText,
                    style = typography.medium14,
                    color = colors.primary500
                )
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(
                    text = dismissText,
                    style = typography.medium14,
                    color = colors.neutral500
                )
            }
        },
        containerColor = colors.neutral0,
        shape = RoundedCornerShape(16.dp)
    )
}
```

**Rules**:
- **MUST** use scale-based color tokens (e.g., `colors.neutral800`, `colors.primary500`)
- **MUST** use weight+size typography tokens (e.g., `typography.bold18`, `typography.regular14`)
- **MUST** be customizable with sensible defaults
- **MUST** handle dismiss request properly
- **SHOULD** follow Material Design dialog patterns

### Composition Local Access Pattern

**MUST** provide access to theme tokens via CompositionLocal:

```kotlin
// Theme.kt - CompositionLocal providers
val LocalKoinColorPalette = staticCompositionLocalOf<KoinColorPalette> {
    error("No KoinColorPalette provided")
}

val LocalKoinTypography = staticCompositionLocalOf<KoinTypography> {
    error("No KoinTypography provided")
}

// Usage in components - access via .current
@Composable
fun ExampleComponent() {
    val colors = LocalKoinColorPalette.current
    val typography = LocalKoinTypography.current
    
    Text(
        text = "Hello",
        style = typography.bold16,
        color = colors.primary500
    )
}
```

**Theme structure** (from `Theme.kt`):
```kotlin
@Composable
fun RebrandKoinTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorPalette = if (darkTheme) {
        RebrandKoinDarkColorPalette
    } else {
        RebrandKoinLightColorPalette
    }

    CompositionLocalProvider(
        LocalKoinColorPalette provides colorPalette,
        LocalKoinTypography provides Typography
    ) {
        content()
    }
}

// Legacy theme (for backward compatibility)
@Composable  
fun KoinTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorPalette = if (darkTheme) {
        KoinDarkColorPalette
    } else {
        KoinLightColorPalette
    }

    CompositionLocalProvider(
        LocalKoinColorPalette provides colorPalette,
        LocalKoinTypography provides Typography
    ) {
        content()
    }
}
```

**Rules**:
- **MUST** access tokens via `LocalKoinColorPalette.current` and `LocalKoinTypography.current`
- **MUST** wrap all Compose content in `RebrandKoinTheme` (or `KoinTheme` for legacy)
- **MUST** use `staticCompositionLocalOf` for tokens that don't change frequently
- **NEVER** access tokens outside of Composable context

## Testing Guidelines

### Component Testing

```kotlin
class ButtonTest {
    
    @get:Rule
    val composeTestRule = createComposeRule()
    
    @Test
    fun button_displays_text_correctly() {
        composeTestRule.setContent {
            RebrandKoinTheme {
                FilledButton(
                    text = "Test Button",
                    onClick = { }
                )
            }
        }
        
        composeTestRule
            .onNodeWithText("Test Button")
            .assertIsDisplayed()
    }
    
    @Test
    fun button_handles_click() {
        var clicked = false
        
        composeTestRule.setContent {
            RebrandKoinTheme {
                FilledButton(
                    text = "Click Me",
                    onClick = { clicked = true }
                )
            }
        }
        
        composeTestRule
            .onNodeWithText("Click Me")
            .performClick()
        
        assertThat(clicked).isTrue()
    }
    
    @Test
    fun button_shows_loading_state() {
        composeTestRule.setContent {
            RebrandKoinTheme {
                FilledButton(
                    text = "Loading",
                    onClick = { },
                    loading = true
                )
            }
        }
        
        composeTestRule
            .onNodeWithText("Loading")
            .assertDoesNotExist()
        
        composeTestRule
            .onNodeWithContentDescription("Loading")
            .assertIsDisplayed()
    }
}
```

## Import Organization

```kotlin
// 1. Compose imports
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// 2. Internal theme imports
import `in`.koreatech.koin.core.designsystem.theme.*
import `in`.koreatech.koin.core.designsystem.component.button.FilledButton
```

## Critical Rules

These rules are **non-negotiable**:

1. **Theme Usage**: **MUST** always use theme tokens, never hardcode values
2. **Consistency**: **MUST** follow established component patterns
3. **Accessibility**: **MUST** ensure all components are accessible
4. **Theme Support**: **MUST** support both light and dark themes
5. **Composition**: **MUST** use CompositionLocal for theme tokens
6. **Testing**: **MUST** test all interactive components

## File Organization

### New Component Template

When creating a new component:

```
component/[type]/[ComponentName].kt
```

Example: `component/card/ProfileCard.kt`

```kotlin
@Composable
fun ProfileCard(
    name: String,
    email: String,
    avatarUrl: String?,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    // Component implementation
}

@Preview(showBackground = true)
@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun ProfileCardPreview() {
    RebrandKoinTheme {
        ProfileCard(
            name = "John Doe",
            email = "john@koreatech.ac.kr",
            avatarUrl = null,
            onClick = { }
        )
    }
}
```

## Common Anti-Patterns to Avoid

### ❌ WRONG: Hardcoded values
```kotlin
@Composable
fun BadButton() {
    Button(
        colors = ButtonDefaults.buttonColors(
            containerColor = Color(0xFF005099) // VIOLATION: Hardcoded color
        )
    ) {
        Text(
            text = "Button",
            style = TextStyle(        // VIOLATION: Hardcoded style
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium
            )
        )
    }
}
```

### ✅ CORRECT: Theme-based values
```kotlin
@Composable
fun GoodButton() {
    val colors = LocalKoinColorPalette.current
    val typography = LocalKoinTypography.current
    
    Button(
        colors = ButtonDefaults.buttonColors(
            containerColor = colors.primary500
        )
    ) {
        Text(
            text = "Button",
            style = typography.medium14
        )
    }
}
```

### ❌ WRONG: Semantic/M3 naming (not used in this codebase)
```kotlin
// WRONG: This codebase does NOT use semantic naming
Text(color = colors.primary)        // VIOLATION: Use colors.primary500
Text(color = colors.textPrimary)    // VIOLATION: Use colors.neutral800
Text(style = typography.bodyMedium) // VIOLATION: Use typography.regular14
Text(style = typography.titleLarge) // VIOLATION: Use typography.bold20
```

### ✅ CORRECT: Scale-based naming
```kotlin
// CORRECT: Use scale-based naming
Text(color = colors.primary500)      // Primary brand color
Text(color = colors.neutral800)      // Dark text
Text(style = typography.regular14)   // Regular body text
Text(style = typography.bold20)      // Large title
```

### ❌ WRONG: No theme support
```kotlin
@Preview
@Composable
fun ComponentPreview() {
    MyComponent() // VIOLATION: No theme wrapper
}
```

### ✅ CORRECT: Theme-wrapped preview
```kotlin
@Preview(showBackground = true)
@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun ComponentPreview() {
    RebrandKoinTheme {
        MyComponent()
    }
}
```

### ❌ WRONG: Using FontFamily.Default
```kotlin
// VIOLATION: Must use Pretendard font
val style = TextStyle(
    fontFamily = FontFamily.Default,
    fontSize = 14.sp
)
```

### ✅ CORRECT: Using Pretendard via typography tokens
```kotlin
// CORRECT: Access through typography tokens which use Pretendard
val typography = LocalKoinTypography.current
Text(style = typography.regular14)
```

## Build Commands

```bash
# Build designsystem module
./gradlew :core:designsystem:build

# Run designsystem tests
./gradlew :core:designsystem:test

# Run UI tests
./gradlew :core:designsystem:connectedAndroidTest
```

## Design System Guidelines

1. **Component First**: Always design components first, then compose screens
2. **Consistent Spacing**: Use the Spacing system for all measurements
3. **Accessibility First**: Ensure all components meet WCAG standards
4. **Responsive Design**: Components should adapt to different screen sizes
5. **Theme Coverage**: Test components in both light and dark themes

---

**Last Updated**: 2026-01-06  
**For**: AI Coding Agents working on CORE DESIGNSYSTEM module  
**Maintainers**: BCSD Android Track