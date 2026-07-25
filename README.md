# Session Android + React Native

Experimental fork of [Session Android](https://github.com/session-foundation/session-android)
testing brownfield support for existing Android codebases. Commits serve as reference for
integrating React Native (via Expo) into an existing native app without refactoring the project
structure.

Uses Expo's brownfield **isolated** approach: the RN + Expo code is built into a prebuilt **AAR**
and consumed like any other Maven dependency. A floating "Expo" button launches an `ExpoActivity`
that renders the React Native screen (JS bundle embedded in the release AAR, so no Metro).

## Integration steps

1. `npx create-expo-app@latest expo-app --template default@canary` (in `./expo-app`,
   package `session.brownfield`)
2. Build the AAR:
   ```sh
   cd expo-app
   npx expo install expo-brownfield expo-build-properties
   npx expo prebuild -p android --clean
   # android/gradle.properties: brownfield.fused.host-provided=com.github.bumptech.glide
   npx expo-brownfield build:android --release --fused --verbose
   ```
   Publishes `session.brownfield:brownfield-fused-release` to local Maven.
3. Consume it from `app/build.gradle.kts`, launch from `ExpoActivity`. Build: `:app:assemblePlayDebug`.

### Notes

- **`mavenLocal()`** added to the `allprojects` repositories, scoped to the `session.brownfield` group.
- **`host-provided=com.github.bumptech.glide`** — Session uses Glide directly and `expo-image` fuses
  it → duplicate classes without this.
- The first-run **onboarding** (`LandingActivity`) is always shown before an account exists, so the
  "Expo" button is added there via `addContentView`.
- No SDK/Kotlin bump (Kotlin 2.3.20, Gradle 9.4, compileSdk 36, minSdk 26). `libsession-util-android`
  is a **prebuilt** Maven AAR, so no local native build is required. Built the **play** flavor.

---

<details>
<summary>Session Android (original README)</summary>

# Session Android 

[Download on the Google Play Store](https://getsession.org/android)

Add the [F-Droid repo](https://fdroid.getsession.org/)

[Download the APK from here](https://github.com/session-foundation/session-android/releases/latest)

## Summary

Session integrates directly with [Oxen Service Nodes](https://docs.oxen.io/about-the-oxen-blockchain/oxen-service-nodes), which are a set of distributed, decentralized and Sybil resistant nodes. Service Nodes act as servers which store messages offline, and a set of nodes which allow for onion routing functionality obfuscating users' IP addresses. For a full understanding of how Session works, read the [Session Whitepaper](https://getsession.org/whitepaper).

<img src="https://i.imgur.com/wcdAGBh.png" width="320" />

## Architecture and documentation

You can refer to [this document](ARCHITECTURE.md) to learn about the app's architecture and to get an overview of the important parts of the code and how they fit together.

## Want to contribute? Found a bug or have a feature request?

Please search for any [existing issues](https://github.com/session-foundation/session-android/issues) that describe your bugs in order to avoid duplicate submissions. Submissions can be made by making a pull request to our `dev` branch. If you don't know where to start contributing, try reading the Github issues page for ideas.

## Build instructions

Build instructions can be found in [RELEASE.md](RELEASE.md).

## Translations

Want to help us translate Session into your language? You can do so at https://getsession.org/translate

## Verifying signatures

**Step 1:**

Add Jason's GPG key. Jason Rhinelander, a member of the [Session Technology Foundation](https://session.foundation/) and is the current signer for all Session Android releases. His GPG key can be found on his GitHub and other sources.

```
wget https://github.com/jagerman.gpg
gpg --import jagerman.gpg
```

**Step 2:**

Get the signed hashes for this release. `SESSION_VERSION` needs to be updated for the release you want to verify.

```
export SESSION_VERSION=1.20.8
wget https://github.com/session-foundation/session-android/releases/download/$SESSION_VERSION/signature.asc
```

**Step 3:**

Verify the signature of the hashes of the files.

```
gpg --verify signature.asc 2>&1 |grep "Good signature from"
```

The command above should print "`Good signature from "Jason Rhinelander...`". If it does, the hashes are valid but we still have to make the sure the signed hashes match the downloaded files.

**Step 4:**

Make sure the two commands below return the same hash for the file you are checking. If they do, file is valid.

```
sha256sum session-$SESSION_VERSION-universal.apk
grep universal.apk signature.asc
```

## Testing
### BrowserStack

This project is tested with BrowserStack.

## License

Copyright 2011 Whisper Systems

Copyright 2013-2017 Open Whisper Systems

Copyright 2019-2024 The Oxen Project

Copyright 2024-2025 Session Technology Foundation

Licensed under the GPLv3: http://www.gnu.org/licenses/gpl-3.0.html

## Attributions

This project uses [Lucide Icon Font](https://lucide.dev/), which is licensed under the
[ISC License](third_party_licenses/LucideLicense.txt).

## Socials
<a href="https://twitter.com/session_app">
  <img align="left" width="26px" src="https://www.vectorlogo.zone/logos/twitter/twitter-official.svg" />
</a>
<a href="mailto:support@getsession.org">
  <img align="left" width="26px" src="https://www.vectorlogo.zone/logos/gmail/gmail-icon.svg" />
</a>


</details>
