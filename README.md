# ZAROC - The Board Game

This repository contains one project split into four parts: a Java desktop game app, a static website, PostgreSQL scripts for initial setup, and Linux automation scripts used around website deployment/db export/backups.

## Folder structure

- `Java/` — JavaFX desktop game (`src/`) plus UI/media assets (`resources/`).
- `HTML/` — website pages, styles, scripts, images, and download/static data assets.
- `PostgreSQL/` — SQL setup scripts (`CREATE_TABLES.sql`, `VIEWS_CREATION.sql`, `INDEXES.sql`, `FILL_TABLES.sql`).
- `Linux/opt/` — operational shell scripts for backup, DB export, and website sync.

## Quick run guide

1. **Database**
   - Create and prepare schema using scripts in `PostgreSQL/` (tables/views/indexes), then (optionally) load sample data from `FILL_TABLES.sql`.
2. **Java app**
   - Change DB/SMTP credentials to yours in `Java/src/be/kdg/integration/gameapplication/model/credentials/CredentialsManager.java`.
   - Run `GameRun` from `Java/src/be/kdg/integration/gameapplication/GameRun.java` (JavaFX + PostgreSQL JDBC + Jakarta Mail required).
3. **Website**
   - Serve the `HTML/` folder with a web server.
   - `submit.php` expects a writable backend CSV location on the webserver.

## Notes

- The website, database, and Linux scripts all need to run on a single server.
- Linux scripts are environment-specific, so the server needs manual setup, like creations of users, groups, and folder hierarchy (inside /opt/ and /var/www/).
