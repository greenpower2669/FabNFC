# FabNFC — Historique de débogage

## 2026-10-22 — Création v0.1.0
- Contexte : numéros de badges Vigik visuellement effacés ; nécessité de lecture numérique fiable pour comparaison et copier-coller.
- Cause externe non résolue : Agesca parle d'une application NFC dédiée et de « codes provisoires » sans donner son nom ; l'UID NFC n'est pas automatiquement ce code.
- Réponse technique : lire uniquement `Tag.getId()`; ajouter une note explicite et des formats alternatifs. Aucun test sur le matériel réel à ce stade.
- Vérifications à mener : compilation GitHub Actions, comportement sur Samsung SM-A576B Android 16, scannage de différents badges, TTS, export/import, intégrité des fichiers, éventuelle variation de l'UID entre scans.

## 2026-10-22 — GitHub Actions sans exécution confirmée
- **Observation confirmée** : branche `feature/native-nfc-accessible-v1` au commit `b84ce732` avant cette intervention ; fichier YAML présent sur la branche, absent de `main` ; `GET /actions/runs?per_page=25` renvoie `total_count=0` ; `temp/` renvoie 404.
- **Hypothèses non départagées** : Actions désactivées, permissions/restrictions du dépôt, suppression d'événements déclenchés par l'intégration GitHub, autre défaut de prise en charge. Le connecteur refuse l'endpoint administratif `/actions/permissions` : **ce n'est pas une preuve que les Actions sont désactivées**.
- **Vrai défaut préventivement corrigé** : le workflow pouvait tenter de pousser le HEAD synthétique d'une PR vers la branche de développement. Publication conditionnée aux événements de branche adaptés ; conservation d'un artefact de diagnostic après compilation.
- **Non testé** : compilation Android réelle, logs, installation et lecture NFC. L'environnement local n'a pas de SDK Android ni de Gradle et n'a pas pu joindre les distributions Gradle (DNS).
- **Contrôle à effectuer** : observer le résultat de la nouvelle écriture sur branche dans GitHub Actions ; si aucun run, inspection humaine `Settings → Actions → General`, sans toucher à `main`.

### Vérification du correctif poussé (même soirée)
- Le commit `7e69d7ad0044c0228ecbd80e1d21096c4595d2b6` contient effectivement le garde-fou de publication ; l'API globale `/actions/runs` renvoie toujours zéro ; l'API `/commits/{sha}/check-runs` renvoie zéro ; `temp/` reste absent.
- GitHub documente que les événements déclenchés par le `GITHUB_TOKEN` d'un workflow ne créent généralement pas de nouveau run (hors exceptions). Le type de jeton employé par le connecteur lors des commits **n'a pas été confirmé** : cette règle est une piste, pas une cause prouvée.
- Demande de contrôle humain circonscrite : réglages des Actions du dépôt et relance d'un push via l'interface ou un client Git autorisé. Pas de fusion sur `main`, aucune promesse de fichier APK tant que le résultat binaire n'a pas été inspecté.

## 2026-10-22 — Déblocage réel et build Android vérifié
- Fab a autorisé la continuation de la solution présentée : workflow d'amorçage léger sur `main`, sans fusion de l'application. Commit `56eea889fef1f385a76ff3b090d686cb08571f0e`.
- L'ajout de ce workflow sur `main` a **réellement** déclenché un run `push`, ID `39847469389`, statut final `completed/success`. Le job a téléchargé la branche de travail `feature/native-nfc-accessible-v1` et non l'application sur `main`.
- Logs consultés : `:app:testDebugUnitTest` et `:app:assembleDebug` terminés, `BUILD SUCCESSFUL in 47s`; vérification `unzip -t` réussie ; publication directe de l'APK via `git push` confirmée.
- Chemin `temp/FabNFC-v0.1.0-debug.apk`, taille 21 758 octets ; SHA-256 recalculé depuis la réponse base64 GitHub `615c69204ef51eebb3db529330e9315f900dc772633240c213a079cb6088bb3c`, identique au fichier `.sha256`.
- Commit de publication sur branche de travail : `4bafdac1e621a45b595b62d0dd0447d53f2eb536`. L'APK est un **debug**, jamais testé sur NFC matériel pour le moment ; le code provisoire AGESCA reste non identifié.
- Des avertissements non bloquants liés à la dépréciation de certaines actions Node 20 / setup-java@v4 figurent dans le log ; mise à jour à prévoir.
- Hypothèse sur les événements du connecteur : non démontrée. Le succès de l'amorçage prouve qu'Actions fonctionnait au moins pour cet événement GitHub sur `main`.
