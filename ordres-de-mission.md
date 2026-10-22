# FabNFC — Ordres de mission

## IMMUTABLE / À PRÉSERVER
- Dépôt `greenpower2669/FabNFC`, code sous MIT, branche de travail sans fusion main sans accord de Fab.
- Application Android **native** (pas une web app), première cible Samsung Android 16.
- Pas de publication Play ni de changement d'identité de package sans accord ; applicationId provisoire `fr.fabnfc.reader`.

## FABNFC-001 — Reader badge (Fab, 22/10/2026)
**Statut : implémenté en branche, tests matériels à faire.** Lecteur NFC lecture seule pour relever des badges Vigik aux numéros effacés, gros caractères adaptés à la vision, voix, vérification des chiffres partiellement lisibles.

## FABNFC-002 — Copies distinctes (Fab, 22/10/2026)
**Statut : implémenté en branche, à valider.** Deux représentations copiables, éventuellement plusieurs, boutons copier individuels et copier ensemble pour ne pas recopier manuellement des chiffres.

## FABNFC-003 — Sauvegardes (Fab, 22/10/2026)
**Statut : implémenté en branche, tests Android à faire.** Sauvegarde locale automatique de l'historique, export/import de fichiers de sauvegarde.

## FABNFC-004 — MIT / APK (Fab, 22/10/2026)
**Statut : APK debug livré sur la branche, compilation et tests unitaires validés par GitHub Actions n°39847469389 ; test sur Samsung à effectuer.** Licence MIT, build Android et APK direct disponible pour téléphone sans ZIP. Publication main/release finale soumise à accord.

## Point extérieur bloquant
Lescuyer/AGESCA n'a pas indiqué le nom de l'application NFC requise ni le format du « code provisoire ». Ne jamais dire que l'UID brut est ce code.

## FABNFC-005 — Déblocage GitHub Actions (Fab, 22/10/2026)
**Statut : livré techniquement, à tester sur téléphone.** À la suite de l'accord « Cool continu », ajout sur `main` d'un seul workflow d'amorçage sans fusionner l'application ; il compile exclusivement `feature/native-nfc-accessible-v1` et publie l'APK direct dans `temp/` de cette branche. Exécution GitHub n°39847469389 : succès, tests et assemblage validés. Aucun merge de la PR n°1.
