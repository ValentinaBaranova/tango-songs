## Tango songs

Tango songs is a service to store and explore tango songs. 
Songs metadata are loaded from audio files by scheduled task, there is also scheduled task to get trackId from Spotify. 
Service provides REST api to search songs by full text search.

### Technic details

Articles about PostgreSQL full text search:

https://www.postgresql.org/docs/current/textsearch-intro.html
https://robconery.medium.com/creating-a-full-text-search-engine-in-postgresql-2022-7b6ed6e81cae
https://medium.com/@yevhenii.kukhol/teach-spring-data-jpa-to-use-postgresql-full-text-search-efficiently-5a0f6f64764c
https://github.com/Eragoo/spring-hibernate-postgres-full-text/tree/migrate-to-java-20-spring-boot-3