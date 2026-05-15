## Objectif à court terme (à déplacer dans une fichier à part)
J'ai du code d'une facade de driver JDBC pour typer des objets.

Ce qu'il faut faire (dans le désordre):
* Déplacer le code d'essais des des tests unitaires
* Remplacer les essais PostgresSQL par des essais DuckDB avec l'extension spatial
* Circonscrire l'usage de JdbcCLient et autres trucs Spring aux tests unitaires
* Injecter les transformation de types via une interface adéquate (à définir)
* Préparer le projet pour une publication sur Maven Central
* Propose un nom de paquage de projet. Je suis emcastro sur Github.

## Directives générale

* Écrire les AGENTS.md en anglais
* Ne pas commiter unless direct order from the user
* Don't handle error cases unless specified by the user. In case of error case, you  can propose a solution but don't implement it unless specified by the user
* Don's change tests without direct order from the user
