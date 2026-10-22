# FabNFC — À faire / preuves

## Fait dans la proposition de code 0.1.0
- [x] Licence MIT sur main (commit `376e147328d3532d44c95f24de28519b18cdfa6d`).
- [x] Branche `feature/native-nfc-accessible-v1`.
- [x] Implémentation lecteur NFC natif lecture seule, quatre formats copiables, TTS, noms et historique.
- [x] Export/import JSON avec contrôles et fusion, sauvegarde locale.
- [x] Gradle, CI APK debug direct `temp/FabNFC-v0.1.0-debug.apk`, tests unitaires de conversion.

## Vérification à effectuer
- [ ] **Bloquant au 22/10** : l'API GitHub indique `total_count: 0` dans les runs Actions du dépôt, après commit du workflow, push de branche et création PR #1. Vérifier Actions → General (autorisation des workflows / approbation éventuelle), puis déclencher une compilation. Aucun APK confirmé dans `temp/`; ne donner aucun lien d'installation avant preuve.
- [ ] Installer et tester sur Samsung Android 16 : NFC et affichage, lecture vocale, clavier, copie, différences d'ordre, import/export puis redémarrage.
- [ ] Vérifier plusieurs lectures d'un même badge et examiner la stabilité de l'UID.
- [ ] Demander à Agesca l'application et la correspondance avec « code provisoire »; l'appli ne sait pas le fabriquer.
- [ ] Obtenir validation de Fab avant merge main / release finale.

## Blocages / état
- Aucun test sur téléphone dans l'environnement de développement.
- Dernier commit de code : `d438e633a6b8852de1f52c14cd3b5229bc9f22e8`; correctif workflow : `df452a7d113d1a870eed980af7823ac3e81f9965`. PR brouillon : #1. Build : pas encore déclenché.
- Étape immédiate : lire logs CI, corriger les erreurs de compilation, fournir APK vérifié.
