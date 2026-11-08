# 📶 FabNFC Web v0.2.0 — application web autonome

## Deux utilisations

- **Fichier local autonome** : télécharger `FabNFC-Web-hors-ligne.html`, l'ouvrir dans le navigateur. Les quatre conversions UID, saisie, historique, copie, voix et import/export JSON fonctionnent sans réseau, sous réserve des capacités locales du navigateur (le stockage local peut être bloqué pour `file://`). **Le scan Web NFC n'est pas possible depuis `file://`.**
- **PWA installable** : servir les six fichiers web (HTML, manifest, service worker, SVG et deux PNG) en **HTTPS**, par exemple sur GitHub Pages après configuration. Ouvrir la page une première fois en ligne, puis installer depuis Chrome Android. Après mise en cache, l'interface peut fonctionner hors ligne. Le scan reste limité aux tags NDEF pris en charge par Web NFC, sous réserve de la politique du navigateur, de l'autorisation utilisateur et des capacités de la puce. La possibilité du scan sans réseau doit être validée sur le téléphone réel.

## Interopérabilité

Le fichier JSON `schema: 1`, avec `badges: [{uidHex,label,seenAt}]`, correspond à l'APK native v0.1.0. Les mêmes quatre vues de l'UID sont calculées sur l'ordre des octets; les décimaux utilisent un entier non signé, donc `BigInt` en JavaScript. Maximum 500 badges, 1 Mo par sauvegarde, UID de 1 à 32 octets. L'import fusionne sans effacer les anciens badges.

## Limites et confidentialité

**Une application web ne remplace pas le mode lecteur natif Android.** Web NFC ne lit pas tous les badges MIFARE/ISO-DEP/Vigik. Une détection NDEF peut ne pas exposer de `serialNumber`. Le numéro UID n'est ni une preuve d'identité ni nécessairement un numéro imprimé ou un code provisoire Agesca. Pas d'écriture NFC, pas de clé, pas d'authentification, pas de réseau par le code de l'application. L'historique `localStorage` et les exports ne sont pas chiffrés; la suppression des données du navigateur peut détruire les fiches sans export préalable.

## Tests conseillés

1. Ouvrir le HTML seul depuis les fichiers du téléphone en mode avion. Entrer `04A1B2C3` et contrôler HEX `04A1B2C3`, décimal `77705923`, HEX inverse `C3B2A104`, décimal inverse `3283263748` (recalculer indépendamment avant de considérer ces nombres comme valeurs de référence).
2. Tester la copie des quatre formats, le renommage et la lecture vocale; fermer et rouvrir, vérifier la persistance ou le message de stockage indisponible.
3. Exporter JSON, importer dans l'APK Android et effectuer la migration dans le sens inverse avec des badges réels. Tester doublons, UID invalides, JSON corrompu, >1 Mo, >500 badges.
4. Héberger la PWA sous HTTPS; vérifier installation, actualisation et lancement en mode avion après le premier cache. Scanner un tag NDEF **compatible**, puis un badge Vigik non-NDEF pour constater honnêtement la limitation.

Aucune compilation Android ni vérification de NFC matériel n'est effectuée par le simple ajout de cette version web. L'APK native existante demeure inchangée.
