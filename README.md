# sokos-os-ekstern-api

API for vedtaksløsninger som skal sende tilbakekrevingsoppdrag til OS via zOS connect

* [1. Dokumentasjon](dokumentasjon/dokumentasjon.md)
* [2. Funksjonelle krav](#2-funksjonelle-krav)
* [3. Utviklingsmiljø](#3-utviklingsmiljø)
* [4. Programvarearkitektur](#4-programvarearkitektur)
* [5. Deployment](#5-deployment)
* [6. Autentisering](#6-autentisering)
* [7. Drift og støtte](#7-drift-og-støtte)
* [8. Swagger](#8-swagger)
* [9. Henvendelser og tilgang](#9-henvendelser-og-tilgang)

---

# 2. Funksjonelle Krav

API for å håndtere tilbakekrevingsvedtak og kravgrunnlag. Applikasjonen tilbyr følgende funksjonalitet:

- **Send tilbakekrevingsvedtak** - Sender vedtak om tilbakekreving til OS via zOS Connect
- **Hent kravgrunnlag liste** - Henter liste over kravgrunnlag basert på søkekriterier
- **Hent kravgrunnlag detaljer** - Henter detaljert informasjon om et spesifikt kravgrunnlag
- **Annuler kravgrunnlag** - Annullerer et eksisterende kravgrunnlag

# 3. Utviklingsmiljø

### Forutsetninger

* Java 25
* [Gradle](https://gradle.org/)
* [Kotest IntelliJ Plugin](https://plugins.jetbrains.com/plugin/14080-kotest)

### Bygge prosjekt

`./gradlew build`

### Lokal utvikling

1. Kjør `./gradlew build` for å bygge prosjektet
2. Start applikasjonen ved å kjøre main-metoden i `Application.kt`
3. API er tilgjengelig på `http://localhost:8080/api/v1/tilbakekreving`

For testing:
- Generer AzureAD token via [azure-token-generator](https://azure-token-generator.intern.dev.nav.no/)
- Send requests med `Authorization: Bearer <token>` header

# 4. Programvarearkitektur

TODO: Mermaid chart

# 5. Deployment

Distribusjon av tjenesten er gjort med bruk av Github Actions.
[sokos-os-ekstern-api CI / CD](https://github.com/navikt/sokos-os-ekstern-api/actions)

Push/merge til main branche vil teste, bygge og deploye til produksjonsmiljø og testmiljø.

# 6. Autentisering

Applikasjonen bruker [AzureAD](https://docs.nais.io/security/auth/azure-ad/) autentisering.
- Bruk https://azure-token-generator.intern.dev.nav.no/api/obo?aud=cluster:namespace:app for å generere obo-token testing mot dev.
- Bruk https://azure-token-generator.intern.dev.nav.no/api/m2m?aud=cluster:namespace:app for å generere m2m-token testing mot dev.

# 7. Drift og støtte

### Logging

Feilmeldinger og infomeldinger som ikke innheholder sensitive data logges til [Grafana Loki](https://docs.nais.io/observability/logging/#grafana-loki).  
Sensitive meldinger logges til [Team Logs](https://doc.nais.io/observability/logging/how-to/team-logs/).

### Kubectl

For dev-gcp:

```shell script
kubectl config use-context dev-gcp
kubectl get pods -n okonomi | grep sokos-os-ekstern-api
kubectl logs -f sokos-os-ekstern-api-<POD-ID> --namespace okonomi -c sokos-os-ekstern-api
```

For prod-gcp:

```shell script
kubectl config use-context prod-gcp
kubectl get pods -n okonomi | grep sokos-os-ekstern-api
kubectl logs -f sokos-os-ekstern-api-<POD-ID> --namespace okonomi -c sokos-os-ekstern-api
```

### Alarmer

Applikasjonen bruker [Grafana Alerting](https://grafana.nav.cloud.nais.io/alerting/) for overvåkning og varsling.

Alarmene overvåker metrics som:

- HTTP-feilrater
- JVM-metrikker

Varsler blir sendt til følgende Slack-kanaler:

- Dev-miljø: [#team-mob-alerts-dev](https://nav-it.slack.com/archives/C042SF2FEQM)
- Prod-miljø: [#team-mob-alerts-prod](https://nav-it.slack.com/archives/C042ESY71GX)

### Grafana

Grafana dashboards for overvåkning og metrikker blir opprettet etter deploy.

---

# 8. Swagger

- [dev-gcp](https://sokos-os-ekstern-api.intern.dev.nav.no/api/v1/tilbakekreving/docs)

# 9. Henvendelser og tilgang

Spørsmål knyttet til koden eller prosjektet kan stilles som issues her på Github.
Interne henvendelser kan sendes via Slack i kanalen [#utbetaling](https://nav-it.slack.com/archives/CKZADNFBP)

