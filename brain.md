# FabNFC — Contrat fonctionnel v0.1.0

Fab veut scanner lui-même ses badges NFC dont le numéro imprimé est effacé, sans risque de mauvaise transcription dû à sa vision. Gros texte, boutons confortables, copie sans retranscription, annonce vocale, sauvegarde locale et export/import.

- NFC Android via Reader Mode sur la seule activité au premier plan, lecture de `Tag.getId()` uniquement ; aucune commande APDU / authentification / écriture NFC.
- Quatre représentations explicites de la **même** valeur : UID hexadécimal ordre NFC, entier décimal big-endian, UID hex octets inversés, entier décimal inversé. Copie individuelle et copie groupée.
- Les quatre valeurs sont une aide à comparer avec les chiffres imprimés ; aucune ne garantit le format du syndic ou d'AGESCA.
- Historique auto dans stockage privé, dédoublonnage UID, renommage et sélection, suppression totale sous confirmation.
- Backup JSON schema=1 limité à 500 badges / 1Mo, import sans effacer; export avec le sélecteur Android.
- Voix TextToSpeech français, annonces lors du scan et des confirmations. Pas d'accès internet ni transfert externe automatique.
- Prototype sans test physique; avertir que certains badges changent d'UID ou ne sont pas compatibles.
