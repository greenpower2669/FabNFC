# FabNFC — Historique de débogage

## 2026-10-22 — Création v0.1.0
- Contexte : numéros de badges Vigik visuellement effacés ; nécessité de lecture numérique fiable pour comparaison et copier-coller.
- Cause externe non résolue : Agesca parle d'une application NFC dédiée et de « codes provisoires » sans donner son nom ; l'UID NFC n'est pas automatiquement ce code.
- Réponse technique : lire uniquement `Tag.getId()`; ajouter une note explicite et des formats alternatifs. Aucun test sur le matériel réel à ce stade.
- Vérifications à mener : compilation GitHub Actions, comportement sur Samsung SM-A576B Android 16, scannage de différents badges, TTS, export/import, intégrité des fichiers, éventuelle variation de l'UID entre scans.
