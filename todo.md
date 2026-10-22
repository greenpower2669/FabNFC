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

## Diagnostic CI vérifié le 22/10/2026 (reprise FabNFC-004)
- [x] HEAD de la branche vérifié : `b84ce732eb8ce88684c5417c268da880ce5d7e34` avant ce correctif.
- [x] Consultation de l'API GitHub `GET /repos/greenpower2669/FabNFC/actions/runs?per_page=25` : `total_count: 0`, pas seulement le connecteur filtré sur les PR.
- [x] Consultation de `temp/` sur la branche : 404, aucun APK présent et vérifié.
- [x] Le workflow est absent de `main` : le déclenchement manuel `workflow_dispatch` n'est pas utilisable comme solution autonome tant que le fichier manque sur la branche par défaut.
- [x] Risque réel corrigé dans la configuration : ne jamais exécuter `git push` après un checkout de PR (commit de fusion temporaire). Publication restreinte au `push` ou `workflow_dispatch` sur la branche de travail.
- [x] Ajout de `upload-artifact@v4` après vérification de l'APK pour conserver un témoin de compilation dans Actions, même si la publication directe échoue ; **ce témoin est une archive GitHub Actions, et non le livrable direct souhaité**.
- [x] Le correctif de workflow a été poussé au commit `7e69d7ad0044c0228ecbd80e1d21096c4595d2b6` et son contenu a été relu via `fetch_file`.
- [x] Immédiatement après ce push, `GET /actions/runs` : zéro exécution ; `GET /commits/7e69d7ad.../check-runs` : zéro check ; `temp/` reste absent (404).
- [ ] **Prochaine dépendance bloquante** : inspecter `Settings → Actions → General` dans le navigateur connecté à GitHub et vérifier si les workflows sont autorisés ; ensuite provoquer un événement `push` depuis l'interface GitHub ou Git classique sur la branche de travail, car le mécanisme de publication par le connecteur peut ne pas déclencher d'Actions. Lire alors les logs et compiler.
- [ ] Les paramètres réels `Settings → Actions → General` restent **non vérifiés** : l'API de lecture des autorisations Actions est refusée par le connecteur (endpoint non pris en charge). Ne pas conclure qu'Actions est désactivé sans cette vérification.
- [ ] Si toujours zéro run, faire vérifier dans GitHub par Fab : Actions activées, autorisation des actions tierces `actions/*` et `gradle/actions/*`, puis relancer via nouveau commit sur la branche de travail (sans fusion).
- [ ] Confirmer fichier réel, taille, SHA-256, résultat Gradle et accessibilité de l'URL `temp/FabNFC-v0.1.0-debug.apk` avant livraison.
- [ ] Aucun SDK Android / Gradle local accessible dans l'environnement d'exécution de ce diagnostic ; tentative d'accès externe à Gradle échouée (résolution DNS). Aucune compilation locale attestée.

## Essai de déclenchement demandé par Fab — 22/10/2026
- [x] Capture GitHub `Settings → Actions → General` fournie par Fab : `Allow all actions and reusable workflows` est sélectionné. La désactivation générale des Actions n'explique donc pas le blocage observé.
- [x] `Workflow permissions` visible sur la capture : permissions par défaut en lecture. Le YAML du workflow contient déjà `permissions: contents: write` ; le choix de permission par défaut n'explique pas à lui seul l'absence totale de runs.
- [x] Fab a demandé un commit de test créé par l'assistante sur la branche de travail, sans modifier `main` ; cette édition de `todo.md` est cet essai.
- [ ] Après le commit, revérifier la liste des runs et `temp/`. Une absence de run après un commit via connecteur n'établit pas pourquoi le système ignore ses événements ; essayer un commit par l'éditeur web GitHub avec l'identité utilisateur si nécessaire.
