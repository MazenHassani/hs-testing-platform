# External credentials

Provide `DB_USERNAME`, `DB_PASSWORD`, `MAIL_USERNAME`, `MAIL_PASSWORD`, `MAIL_FROM`, `SPHERE_PROBLEMS_TOKEN`, `SPHERE_COMPILERS_TOKEN`, and `SENDGRID_API_KEY` through your deployment secret store or process environment. Do not commit real values. Spring properties intentionally have no credential defaults; the test configuration also requires externally supplied test-service tokens.

Docker Compose uses DB_USERNAME and DB_PASSWORD for PostgreSQL. The application service forwards the same required environment variables explicitly. Preserve the existing database credentials for existing volumes: changing an environment variable alone does not update PostgreSQL users/passwords or rotate a remote credential. Provision safely before redeploying. Mail settings are read from environment when the mail constants class initializes.

This commit removes current-source hardcoded credentials. Existing Git history and external credentials are unchanged. Rotate any still-active exposed credentials separately. No live deployment or external service has been modified or tested.
