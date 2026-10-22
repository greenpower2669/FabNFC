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
