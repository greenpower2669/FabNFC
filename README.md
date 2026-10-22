# 📶 FabNFC — lecteur NFC accessible Android

Application Android native, hors ligne, pour lire l'UID public d'un badge NFC compatible sans jamais écrire dessus.

## Fonctions (prototype 0.1.0)
- Lecture NFC au contact du dos du smartphone; gros caractères contrastés et retour vocal (TTS).
- Affichage de quatre **représentations du même UID** : hexadécimal et décimal, dans l'ordre lu et avec les octets inversés. Chaque affichage est copiable, ainsi qu'un « copier les quatre ».
- Historique local persistant, renommage, sélection et suppression confirmée.
- Export et import JSON via sélecteur de fichiers Android, avec fusion sans écrasement et validation stricte du contenu.
- Zéro permission réseau, zéro écriture sur un badge, zéro tentative de lecture de secteurs protégés.

**Important :** le numéro imprimé peut ne pas correspondre à ces représentations. Certains badges présentent un UID aléatoire, et le code provisoire AGESCA peut être propriétaire. FabNFC ne fabrique **pas** ce code. Il faut faire confirmer par AGESCA la valeur à transmettre.

## Installer / tester

Sur un Samsung compatible NFC, activer NFC puis ouvrir Fab NFC. Présenter un badge au dos du téléphone, vérifier l'affichage, copier la valeur voulue. Si l'application indique que le NFC est absent ou désactivé, vérifier les paramètres. Ne pas transmettre une valeur non confirmée comme un code AGESCA.

## Compiler

JDK 17, Android SDK 35, Gradle 8.10.2 :
`gradle :app:testDebugUnitTest :app:assembleDebug`.

Le workflow GitHub Actions compile et place **l'APK décompressé** dans `temp/FabNFC-v0.1.0-debug.apk` **sur la branche de travail**, seulement si le build réussit. Aucune fusion automatique sur main. Ne pas utiliser de build de test comme application de contrôle d'accès certifiée.

## Sauvegardes

Le fichier JSON exporté contient les identifiants de badges et leurs noms (données potentiellement sensibles). Il doit être conservé dans un emplacement choisi et maîtrisé. La sauvegarde automatique reste dans l'espace privé de l'application et disparaît si les données de l'application sont effacées. Import = ajout sans supprimer les badges existants; un UID existant n'est pas écrasé.

## Licence
MIT — voir [LICENSE](LICENSE).