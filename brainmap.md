# FabNFC — Architecture exhaustive v0.1

## Entrées
- `app/src/main/AndroidManifest.xml` : NFC permission + feature optionnelle, Activity principale, aucune permission réseau.
- `MainActivity.java` : Activity native programmée en Java sans bibliothèques UI tierces, ReaderCallback, état et actions.

## NFC
`onResume` active `NfcAdapter.enableReaderMode` (NFC A/B/F/V; `SKIP_NDEF_CHECK`) seulement au premier plan. `onTagDiscovered` récupère les seuls octets `Tag.getId()`, filtre les répétitions de 2,2s, répercute sur thread UI, stocke l'UID hex.

## Calcul
`UidFormats.java` : conversion hex unsigned, entier décimal positif BigInteger, inversion de l'ordre des octets, validation des entrées importées. Tests dans `UidFormatsTest.java`.

## UI / accessibilité
`MainActivity.buildView` : ScrollView, contrastes, affichage large, écrans compatibles insets Android 15+, boutons haut 62dp, TextToSpeech français, champs sélectionnables, ClipboardManager.

## Stockage / sauvegarde
`Entry` = hex, label, seenAt; `badges.json` en stockage privé, sérialisation dans `backupJson`; partage manuel via Storage Access Framework (ACTION_CREATE_DOCUMENT et ACTION_OPEN_DOCUMENT). Import vérifié et fusion dédoublonnée.

## Livraison
Gradle Android application plugin 8.7.3, SDK 35, Java 17, version 0.1.0. `.github/workflows/build-android.yml` teste, compile puis pousse le debug APK directement dans `temp/` sur la branche de travail. Aucun merge main automatique.

## Limites
Impossible d'affirmer que l'UID exposé est le numéro de série imprimé ou le code provisoire Agesca ; format dépend badge et prestataire. Aucune lecture de données protégées.

## Chaîne de CI — garde-fous ajoutés le 22/10/2026
- `.github/workflows/build-android.yml` : `push` cible seulement `feature/native-nfc-accessible-v1` (ignore `temp/**`), `pull_request` cible `main`, `workflow_dispatch` est déclaré mais requiert un fichier de workflow sur la branche par défaut pour être déclenché manuellement.
- Après `gradle :app:testDebugUnitTest :app:assembleDebug` et `test -s`, `actions/upload-artifact@v4` conserve une copie diagnostic. Le fichier directement installable n'existe qu'après un push réussi dans `temp/`.
- Le push de `temp/FabNFC-v0.1.0-debug.apk` et du SHA-256 ne fonctionne que pour `push` ou `workflow_dispatch` sur la référence exacte de la branche de travail. Une PR construit et teste **sans publier ni pousser son checkout de fusion temporaire**.
- L'API GitHub `/actions/runs` a répondu avec `total_count=0` le 22/10/2026. Les droits effectifs du service GitHub Actions ne sont pas vérifiés : l'accès à son endpoint administratif est indisponible par le connecteur.
