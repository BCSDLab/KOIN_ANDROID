# CORE WebApp Module - AGENTS.md

This file provides prescriptive coding guidelines for AI coding agents working in the CORE WEBAPP module of the KOIN_ANDROID repository.

## Module Overview

The `core:webapp` module provides **WebView integration components** for embedding web applications within the native Android app. It offers a Compose-based WebView wrapper with cookie management, JavaScript interface support, and lifecycle handling.

### Architecture Position
```
┌─────────────────────────────────────────────────────────────┐
│              Feature Modules (using WebApp)                 │
│              (landing pages, external content)              │
└─────────────────────────────────────────────────────────────┘
                            ↓ depends on
┌─────────────────────────────────────────────────────────────┐
│                    core:webapp                              │
│    (WebView Composable, JS Interface, Cookie Management)    │
└─────────────────────────────────────────────────────────────┘
```

## Core Responsibilities

1. **WebApp Composable**: Jetpack Compose wrapper for WebView
2. **JavaScript Interface**: Bridge between web content and native Android
3. **Cookie Management**: Session cookies for authenticated web content
4. **WebView Clients**: Custom `WebViewClient` and `WebChromeClient`
5. **Token Injection**: Passing authentication tokens to web content
6. **Lifecycle Management**: Proper WebView state saving/restoration

## Package Structure

```
core/webapp/src/main/java/in/koreatech/koin/core/webapp/
├── KoinWebAppInterface.kt         # JavaScript interface for native calls
├── KoinWebAppWebChromeClient.kt   # Chrome client for progress, dialogs
├── KoinWebAppWebViewClient.kt     # WebView client for navigation
├── Tokens.kt                      # Token data class
└── WebApp.kt                      # Main WebApp Composable
```

## Implementation Patterns

### WebApp Composable Pattern

**MUST** use the `WebApp` composable for all WebView needs:

```kotlin
@Composable
fun WebApp(
    url: String,
    modifier: Modifier = Modifier,
    exposedInterfaceName: String = "Android",
    windowInsets: WindowInsets = WindowInsets.safeDrawing,
    cookies: List<Pair<String, String>> = emptyList(),
    koinWebAppInterface: KoinWebAppInterface = KoinWebAppInterface(),
    koinWebAppWebViewClient: KoinWebAppWebViewClient = KoinWebAppWebViewClient(),
    koinWebChromeClient: KoinWebAppWebChromeClient = KoinWebAppWebChromeClient(),
    backHandler: @Composable (view: WebView?) -> Unit = {}
)
```

**Parameters**:
- `url`: The web app URL to load
- `exposedInterfaceName`: JavaScript interface name (default: "Android")
- `windowInsets`: WindowInsets for edge-to-edge display
- `cookies`: List of cookie key-value pairs
- `koinWebAppInterface`: Custom JavaScript interface
- `koinWebAppWebViewClient`: Custom WebViewClient
- `koinWebChromeClient`: Custom WebChromeClient
- `backHandler`: Back navigation handler

### Usage Example

```kotlin
@Composable
fun OrderWebAppScreen(
    accessToken: String,
    refreshToken: String,
    onBack: () -> Unit
) {
    val koinWebAppInterface = remember {
        object : KoinWebAppInterface() {
            @JavascriptInterface
            override fun getUserTokens(): String {
                return Tokens(
                    accessToken = accessToken,
                    refreshToken = refreshToken
                ).toJson()
            }
        }
    }
    
    WebApp(
        url = "https://order.koreatech.in",
        cookies = listOf(
            "accessToken" to accessToken,
            "refreshToken" to refreshToken
        ),
        koinWebAppInterface = koinWebAppInterface,
        backHandler = { webView ->
            BackHandler(enabled = webView?.canGoBack() == true) {
                webView?.goBack()
            }
            
            BackHandler(enabled = webView?.canGoBack() != true) {
                onBack()
            }
        }
    )
}
```

### JavaScript Interface Pattern

**MUST** extend `KoinWebAppInterface` for custom JavaScript interfaces:

```kotlin
open class KoinWebAppInterface {
    @JavascriptInterface
    open fun getUserTokens(): String {
        return "" // Override in subclass
    }
}
```

**Custom Implementation**:
```kotlin
class AuthenticatedWebAppInterface(
    private val tokenRepository: TokenRepository
) : KoinWebAppInterface() {
    
    @JavascriptInterface
    override fun getUserTokens(): String {
        return runBlocking {
            val accessToken = tokenRepository.getAccessToken()
            val refreshToken = tokenRepository.getRefreshToken()
            Tokens(accessToken, refreshToken).toJson()
        }
    }
    
    @JavascriptInterface
    fun logout() {
        // Handle logout from web
        runBlocking {
            tokenRepository.clearTokens()
        }
    }
    
    @JavascriptInterface
    fun navigateToNative(route: String) {
        // Handle navigation request from web
    }
}
```

**Rules**:
- **MUST** annotate all methods with `@JavascriptInterface`
- **MUST** use `open` modifier for overridable methods
- **SHOULD** handle async operations carefully (runBlocking or callbacks)
- **NEVER** expose sensitive operations without proper validation

### WebView Configuration

**MUST** follow secure WebView settings:

```kotlin
@SuppressLint("SetJavaScriptEnabled")
private fun createWebView(
    context: Context,
    exposedInterfaceName: String,
    koinWebAppInterface: KoinWebAppInterface,
    koinWebAppWebViewClient: KoinWebAppWebViewClient,
    koinWebChromeClient: KoinWebAppWebChromeClient
) = WebView(context).apply {
    layoutParams = ViewGroup.LayoutParams(
        ViewGroup.LayoutParams.MATCH_PARENT,
        ViewGroup.LayoutParams.MATCH_PARENT
    )
    webViewClient = koinWebAppWebViewClient
    webChromeClient = koinWebChromeClient
    addJavascriptInterface(koinWebAppInterface, exposedInterfaceName)
    overScrollMode = View.OVER_SCROLL_NEVER
    settings.apply {
        builtInZoomControls = false
        domStorageEnabled = true
        javaScriptEnabled = true
        loadWithOverviewMode = true
        blockNetworkLoads = false
        mixedContentMode = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW
        setSupportZoom(false)
    }
}
```

**Security Considerations**:
- **MUST** only load trusted URLs
- **SHOULD** validate URLs before loading
- **MUST** use `MIXED_CONTENT_ALWAYS_ALLOW` only for known secure environments
- **NEVER** enable JavaScript for untrusted content

### Cookie Management Pattern

**MUST** handle cookies properly:

```kotlin
val cookieManager = CookieManager.getInstance()
val baseUrl = URL(url).let { url ->
    "${url.protocol}://${url.host}"
}

cookieManager.apply {
    removeSessionCookies { }
    removeAllCookies { }
    acceptCookie()
    acceptThirdPartyCookies(webView)
    cookies.forEach { cookie ->
        setCookie(baseUrl, "${cookie.first}=${cookie.second}")
    }
}
```

**Rules**:
- **MUST** clear existing cookies before setting new ones
- **MUST** enable third-party cookies for cross-domain auth
- **SHOULD** use secure cookies (HTTPS only) in production
- **MUST** set cookies before loading URL

### Lifecycle Management Pattern

**MUST** handle WebView lifecycle properly:

```kotlin
val bundle = rememberSaveable { bundleOf() }

LifecycleEventEffect(Lifecycle.Event.ON_PAUSE) {
    webView?.saveState(bundle)
}

LifecycleEventEffect(Lifecycle.Event.ON_RESUME) {
    webView?.restoreState(bundle)
}
```

**Rules**:
- **MUST** save WebView state on pause
- **MUST** restore WebView state on resume
- **MUST** clear WebView reference on release
- **SHOULD** handle configuration changes gracefully

### Token Data Class Pattern

**MUST** use `Tokens` data class for token serialization:

```kotlin
data class Tokens(
    val accessToken: String,
    val refreshToken: String
) {
    fun toJson(): String = JSONObject().apply {
        put("accessToken", accessToken)
        put("refreshToken", refreshToken)
    }.toString()
    
    companion object {
        fun fromJson(json: String): Tokens {
            val jsonObject = JSONObject(json)
            return Tokens(
                accessToken = jsonObject.getString("accessToken"),
                refreshToken = jsonObject.getString("refreshToken")
            )
        }
    }
}
```

### Custom WebViewClient Pattern

**MUST** extend for navigation control:

```kotlin
open class KoinWebAppWebViewClient : WebViewClient() {
    
    override fun shouldOverrideUrlLoading(
        view: WebView?,
        request: WebResourceRequest?
    ): Boolean {
        val url = request?.url?.toString() ?: return false
        
        // Handle external URLs
        if (!url.startsWith("https://koreatech.in")) {
            // Open in external browser
            return true
        }
        
        return false // Load in WebView
    }
    
    override fun onPageFinished(view: WebView?, url: String?) {
        super.onPageFinished(view, url)
        // Handle page load completion
    }
    
    override fun onReceivedError(
        view: WebView?,
        request: WebResourceRequest?,
        error: WebResourceError?
    ) {
        super.onReceivedError(view, request, error)
        // Handle errors
    }
}
```

### Custom WebChromeClient Pattern

**MUST** extend for UI events:

```kotlin
open class KoinWebAppWebChromeClient : WebChromeClient() {
    
    override fun onProgressChanged(view: WebView?, newProgress: Int) {
        super.onProgressChanged(view, newProgress)
        // Update loading progress
    }
    
    override fun onJsAlert(
        view: WebView?,
        url: String?,
        message: String?,
        result: JsResult?
    ): Boolean {
        // Handle JavaScript alerts
        return super.onJsAlert(view, url, message, result)
    }
    
    override fun onJsConfirm(
        view: WebView?,
        url: String?,
        message: String?,
        result: JsResult?
    ): Boolean {
        // Handle JavaScript confirms
        return super.onJsConfirm(view, url, message, result)
    }
}
```

## Back Navigation Pattern

**MUST** implement proper back navigation:

```kotlin
WebApp(
    url = "...",
    backHandler = { webView ->
        // First: try to go back in WebView history
        BackHandler(enabled = webView?.canGoBack() == true) {
            webView?.goBack()
        }
        
        // Second: exit WebApp screen
        BackHandler(enabled = webView?.canGoBack() != true) {
            onNavigateBack()
        }
    }
)
```

## Testing Guidelines

### WebApp Testing

```kotlin
@Test
fun webApp_loads_url_correctly() {
    composeTestRule.setContent {
        WebApp(
            url = "https://test.koreatech.in",
            koinWebAppInterface = mockInterface
        )
    }
    
    // Verify WebView loaded
    verify { mockWebView.loadUrl("https://test.koreatech.in") }
}

@Test
fun cookies_are_set_correctly() {
    val cookies = listOf("token" to "abc123")
    
    composeTestRule.setContent {
        WebApp(
            url = "https://test.koreatech.in",
            cookies = cookies
        )
    }
    
    // Verify cookies
    val cookieManager = CookieManager.getInstance()
    val setCookies = cookieManager.getCookie("https://test.koreatech.in")
    assertThat(setCookies).contains("token=abc123")
}
```

## Import Organization

```kotlin
// 1. Android imports
import android.annotation.SuppressLint
import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.webkit.CookieManager
import android.webkit.JavascriptInterface
import android.webkit.WebChromeClient
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient

// 2. Compose imports
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView

// 3. Lifecycle imports
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LifecycleEventEffect

// 4. Internal imports
import `in`.koreatech.koin.core.webapp.KoinWebAppInterface
```

## Critical Rules

These rules are **non-negotiable**:

1. **JavaScript Security**: **MUST** annotate all JavaScript interface methods with `@JavascriptInterface`

2. **URL Validation**: **MUST** validate URLs before loading. **NEVER** load arbitrary user input.

3. **Cookie Handling**: **MUST** clear cookies before setting new ones to prevent stale sessions.

4. **Lifecycle Management**: **MUST** save/restore WebView state on lifecycle events.

5. **Memory Management**: **MUST** clear WebView reference on release to prevent leaks.

6. **Thread Safety**: **MUST** handle JavaScript interface calls on appropriate threads.

7. **Mixed Content**: **SHOULD** only use `MIXED_CONTENT_ALWAYS_ALLOW` for known secure environments.

8. **Back Navigation**: **MUST** implement proper back navigation with WebView history support.

## Common Anti-Patterns to Avoid

### No URL Validation
```kotlin
// WRONG: Loading arbitrary URLs
WebApp(url = userInput)

// CORRECT: Validate URL
val validatedUrl = if (url.startsWith("https://koreatech.in")) url else DEFAULT_URL
WebApp(url = validatedUrl)
```

### Missing JavaScript Annotation
```kotlin
// WRONG: Missing annotation
class MyInterface : KoinWebAppInterface() {
    fun getData(): String { ... } // Will not be callable from JS
}

// CORRECT: With annotation
class MyInterface : KoinWebAppInterface() {
    @JavascriptInterface
    fun getData(): String { ... }
}
```

### Memory Leak
```kotlin
// WRONG: Not clearing reference
AndroidView(
    factory = { WebView(it).also { webView = it } }
    // Missing onRelease
)

// CORRECT: Clearing reference
AndroidView(
    factory = { WebView(it).also { webView = it } },
    onRelease = { webView = null }
)
```

## Build Commands

```bash
# Build webapp module
./gradlew :core:webapp:build

# Run webapp tests
./gradlew :core:webapp:test

# Check ktlint for webapp
./gradlew :core:webapp:ktlintCheck
```

## Security Considerations

1. **Trusted Origins Only**: Only load URLs from trusted domains
2. **Token Protection**: Never log or expose tokens
3. **HTTPS Only**: Prefer HTTPS URLs in production
4. **Input Sanitization**: Sanitize any data passed to JavaScript
5. **Permission Handling**: Handle WebView permissions carefully

---

**Last Updated**: 2026-01-06  
**For**: AI Coding Agents working on CORE WEBAPP module  
**Maintainers**: BCSD Android Track
