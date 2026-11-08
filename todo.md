## FABNFC-007 — Release Android + Web (08/11/2026)
- [x] Publier la prérelease `fabnfc-v0.2.0-preview` sur GitHub depuis le commit web `2fd226d4e29fa4e910089366773073f0bda38754`.
- [x] Vérifier la réussite d'Actions run `42478554802` (completed/success) et les quatre assets de Release.
- [x] APK directement téléchargeable `FabNFC-v0.1.0-debug.apk` (21 758 octets), HTML `FabNFC-Web-v0.2.0-hors-ligne.html` (15 677 octets), PWA ZIP (12 633 octets), SHA256SUMS.txt (282 octets).
- [x] Vérifier les noms, tailles non nulles et URLs d'assets avec l'API GitHub Releases ; intégrité APK vérifiée dans Actions (sha256sum et unzip -t).
- [ ] Tester l'installation APK, la lecture de badge et les sauvegardes sur Samsung Android.
- [ ] Déployer la PWA sur HTTPS et vérifier installation/service worker/scan NDEF hors réseau.
- [ ] Aucune fusion dans main ; l'unique ajout sur main est le workflow de publication `.github/workflows/publish-fabnfc-release.yml` (commit `b38f0de373efddf4af02f03f647d7d9b3b2ae277`).
- Release : https://github.com/greenpower2669/FabNFC/releases/tag/fabnfc-v0.2.0-preview


## NOUVEAU — FABNFC-006 / Web hors ligne (08/11/2026)
- [x] Créer la version HTML autonome et la PWA séparée, sans altérer le code de l'APK native.
- [x] Implémenter quatre conversions UID, fiches locales, import/export JSON schema=1, copies, voix, contrôles d'import, Web NFC NDEF seulement.
- [x] Réaliser 10 contrôles JavaScript locaux via Node (conversions, validation, import, ajout).
- [x] Publier les sept fichiers de la web app sur feature/offline-webapp-v1 (commit d8faf9ff).
- [ ] Configurer l'hébergement HTTPS et vérifier SW, installation et fonctionnement en mode avion sur Samsung.
- [ ] Tester Web NFC avec tag NDEF et constater les limitations Vigik ; comparer UID à l'APK native.
- [ ] Tester transfert JSON dans les deux sens entre web et Android.
- [ ] Tester l'APK native existante sur Samsung Android.
- [ ] Conserver main, PR native #1 et APK existante intacts jusqu'à décision explicite de fusion/release ; aucune nouvelle APK sans build validé.


# FabNFC — À faire / preuves

## ÉTAT ACTUEL — APK debug DISPONIBLE (22/10/2026)
- [x] Fab a autorisé la poursuite du plan d'amorçage sur `main`, sans fusion du code de l'application.
- [x] Workflow d'amorçage minimal créé sur `main` : `.github/workflows/bootstrap-fabnfc.yml`, commit `56eea889fef1f385a76ff3b090d686cb08571f0e`. Le checkout et la compilation concernent **uniquement** `feature/native-nfc-accessible-v1`.
- [x] Exécution GitHub Actions réelle n°`39847469389`, événement `push`, état final `completed/success` ; logs lus : `:app:testDebugUnitTest` puis `:app:assembleDebug`, `BUILD SUCCESSFUL in 47s`.
- [x] Intégrité de l'APK contrôlée par `unzip -t` dans le workflow.
- [x] Publication directe réussie, commit `4bafdac1e621a45b595b62d0dd0447d53f2eb536` : `temp/FabNFC-v0.1.0-debug.apk` (21 758 octets), et `.apk.sha256` (95 octets).
- [x] Empreinte SHA-256 recalculée depuis les octets de l'APK récupérés par l'API GitHub et conforme au fichier publié : `615c69204ef51eebb3db529330e9315f900dc772633240c213a079cb6088bb3c`.
- [x] APK direct : https://raw.githubusercontent.com/greenpower2669/FabNFC/feature/native-nfc-accessible-v1/temp/FabNFC-v0.1.0-debug.apk
- [ ] **PRIORITÉ :** installer sur Samsung SM-A576B Android 16, tester scan NFC, UID recopiable, lecture vocale, sauvegarde persistante, réouverture, import/export, avec au moins deux badges.
- [ ] Comparer les UID aux numéros visibles et demander à Lescuyer/AGESCA le nom de l'application et la méthode de récupération du « code provisoire ». Aucune transformation UID n'est garantie.
- [ ] Aucune fusion sur `main` ou release finale tant que Fab n'a pas donné son accord spécifique.
- [ ] Déprécations dans les logs Actions : `actions/setup-java@v4` et actions Node 20 forcées sur Node 24 ; planifier upgrade/test ultérieur, non bloquant.
- [ ] Les observations « zéro run / zéro APK » plus bas sont l'historique **avant** le workflow d'amorçage et sont désormais résolues. 


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

### Résultat du déclenchement par connecteur — preuve
- [x] Commit de test effectivement créé dans la branche de travail : `044044a820d9a49ff32c25100c885bce88384655` (mise à jour du présent `todo.md`, sans `main`).
- [x] Relecture du HEAD GitHub : correspond au commit de test.
- [x] API GitHub Actions après écriture : `GET /actions/runs?per_page=30` → `total_count=0`; checks sur le SHA → `total_count=0`.
- [x] `temp/FabNFC-v0.1.0-debug.apk` : absent (404). Il n'y a aucune preuve de compilation.
- [ ] Si aucune exécution n'apparaît, poursuivre via déclenchement réalisé **par le navigateur GitHub ou Git classique avec l'identité utilisateur**, non par ce connecteur ; sinon, un workflow manuel nécessitera un fichier de workflow sur la branche par défaut, modification de `main` interdite sans accord explicite de Fab.
