/*dataSource {
    pooled = true
    jmxExport = true
    driverClassName = "org.h2.Driver"
    username = "sa"
    password = ""
}*/
dataSource {
    pooled = true
    driverClassName = "org.postgresql.Driver"
    dialect = org.hibernate.dialect.PostgreSQL9Dialect

    username = "postgres"
    password = "postgres"
}
hibernate {
    cache.use_second_level_cache = true
    cache.use_query_cache = true
//    cache.region.factory_class = 'net.sf.ehcache.hibernate.EhCacheRegionFactory' // Hibernate 3
    //cache.region.factory_class = 'org.hibernate.cache.ehcache.EhCacheRegionFactory' // Hibernate 4
    cache.provider_class = 'org.hibernate.cache.EhCacheProvider'
    //singleSession = true // configure OSIV singleSession mode
    //flush.mode = 'manual' // OSIV session flush mode outside of transactional context
}

// environment specific settings
environments {
    development {
        dataSource {
            dbCreate = "create-drop" //"create-drop" // one of 'create', 'create-drop', 'update', 'validate', ''
            //url = "jdbc:h2:mem:devDb;MVCC=TRUE;LOCK_TIMEOUT=10000;DB_CLOSE_ON_EXIT=FALSE"
            //url = "jdbc:postgresql://ala-devmaps-db.vm.csiro.au/keys"
            url = "jdbc:postgresql://localhost/keys"
            username = "postgres"
            password = "postgres"
        }
    }
    test {
        dataSource {
            dbCreate = "update"
            url = "jdbc:h2:mem:testDb;MVCC=TRUE;LOCK_TIMEOUT=10000;DB_CLOSE_ON_EXIT=FALSE"
        }
    }
    production {
        dataSource {
            dbCreate = "create-drop" //"create-drop" // one of 'create', 'create-drop', 'update', 'validate', ''
            //url = "jdbc:h2:mem:devDb;MVCC=TRUE;LOCK_TIMEOUT=10000;DB_CLOSE_ON_EXIT=FALSE"
            //url = "jdbc:postgresql://ala-devmaps-db.vm.csiro.au/keys"
            url = "jdbc:postgresql://localhost/keys"
            username = "postgres"
            password = "postgres"
        }
    }
}
