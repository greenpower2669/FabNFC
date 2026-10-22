# FabNFC — À faire / preuves

## Fait dans la proposition de code 0.1.0
- [x] Licence MIT sur main (commit `376e147328d3532d44c95f24de28519b18cdfa6d`).
- [x] Branche `feature/native-nfc-accessible-v1`.
- [x] Implémentation lecteur NFC natif lecture seule, quatre formats copiables, TTS, noms et historique.
- [x] Export/import JSON avec contrôles et fusion, sauvegarde locale.
- [x] Gradle, CI APK debug direct `temp/FabNFC-v0.1.0-debug.apk`, tests unitaires de conversion.

## Vérification à effectuer
- [ ] **Bloquant au 22/10 soir** : aucun build GitHub Actions confirmé. Le workflow `.github/workflows/build-android.yml` existe uniquement sur `feature/native-nfc-accessible-v1`; nouveau push diagnostic `6ed0015c5423739799f2a2bf837c26c28b3cf0f2`, mais aucun run `pull_request` ni statut de commit retourné par les connecteurs. Le connecteur des runs filtre `pull_request` et ne prouve donc pas l'absence d'un run `push`. Vérifier personnellement `Settings → Actions → General` et `Actions` pour l'état complet. **Important :** `workflow_dispatch` (bouton Run workflow) exige que le workflow existe sur la branche par défaut `main`; pas de modification de `main` sans accord explicite. Aucun APK confirmé dans `temp/`; ne donner aucun lien d'installation avant preuve.
- [ ] Installer et tester sur Samsung Android 16 : NFC et affichage, lecture vocale, clavier, copie, différences d'ordre, import/export puis redémarrage.
- [ ] Vérifier plusieurs lectures d'un même badge et examiner la stabilité de l'UID.
- [ ] Demander à Agesca l'application et la correspondance avec « code provisoire »; l'appli ne sait pas le fabriquer.
- [ ] Obtenir validation de Fab avant merge main / release finale.

## Blocages / état
- Aucun test sur téléphone dans l'environnement de développement.
- Dernier commit de code : `d438e633a6b8852de1f52c14cd3b5229bc9f22e8`; correctif workflow : `df452a7d113d1a870eed980af7823ac3e81f9965`. PR brouillon : #1. Build : pas encore déclenché.
- Étape immédiate : lire logs CI, corriger les erreurs de compilation, fournir APK vérifié.
