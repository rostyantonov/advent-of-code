# Ktlint Local Configuration Fix

If ktlint is still checking build/generated folders on your local machine, try these solutions:

## Solution 1: Clean Build Directory
```bash
./gradlew clean
rm -rf */build/generated
./gradlew build
```

## Solution 2: Add Local Gradle Properties
Add to `~/.gradle/gradle.properties`:
```properties
org.gradle.caching=true
ktlint.android=false
```

## Solution 3: IDE Configuration

### IntelliJ IDEA / Android Studio
1. Go to Settings → Editor → Code Style → Kotlin
2. Click "Set from..." → Predefined Style → Kotlin style guide
3. Go to Settings → Editor → Inspections
4. Uncheck inspections for "build" and "generated" folders
5. Right-click on `build` folder → Mark Directory as → Excluded

### VS Code
Add to `.vscode/settings.json`:
```json
{
  "ktlint.exclude": ["**/build/**", "**/generated/**"]
}
```

## Solution 4: Run Ktlint with Explicit Exclusions
```bash
./gradlew ktlintCheck -x :advent-2016:ktlintKotlinScriptCheck
```

Or add to root `build.gradle.kts`:
```kotlin
tasks.withType<org.jlleitschuh.gradle.ktlint.KtlintCheckTask> {
    exclude {
        it.file.path.contains("/build/") || 
        it.file.path.contains("/generated/")
    }
}
```

## Solution 5: Check Gradle Cache
Sometimes cached configurations cause issues:
```bash
./gradlew --stop
rm -rf ~/.gradle/caches/
./gradlew ktlintCheck
```

## Verify the Fix
Run ktlint and check that it skips build directories:
```bash
./gradlew ktlintCheck --info 2>&1 | grep -i "build\|generated" | head -20
```

You should NOT see files from build/generated in the output.
