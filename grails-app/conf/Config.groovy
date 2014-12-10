//locations to search for config files that get merged into the main config
//config files can either be Java properties files or ConfigSlurper scripts
def appName = "ala-keys"
grails.project.groupId = "au.org.ala" // change this to alter the default package name and Maven publishing destination
appContext = grails.util.Metadata.current.'app.name'

grails.config.locations = ["classpath:${appName}-config.properties",
                           "classpath:${appName}-config.groovy",
                           "file:${userHome}/.grails/${appName}-config.properties",
                           "file:/data/${appName}/config/${appName}-config.groovy",
]

if (System.properties["${appName}.config.location"]) {
    grails.config.locations << "file:" + System.properties["${appName}.config.location"]
}

/******************************************************************************\
 *  EXTERNAL SERVERS
 \******************************************************************************/
runWithNoExternalConfig = true

if (!bie.baseURL) {
    bie.baseURL = "http://bie.ala.org.au"
}
if (!bie.searchPath) {
    bie.searchPath = "/search"
}

if (!headerAndFooter.baseURL) {
    headerAndFooter.baseURL = 'http://www2.ala.org.au/commonui'
}

/******************************************************************************\
 *  SECURITY
 \******************************************************************************/
if (!security.cas.casServerName) {
    security.cas.casServerName = 'https://auth.ala.org.au'
}
if (!security.cas.uriFilterPattern) {
    //security.cas.uriFilterPattern = "/admin, /admin/.*"// pattern for pages that require authentication
}
if (!security.cas.uriExclusionFilterPattern) {
    //security.cas.uriExclusionFilterPattern = "/images.*,/css.*/less.*,/js.*,.*json,.*xml"
}
if (!security.cas.authenticateOnlyIfLoggedInPattern) {
    //security.cas.authenticateOnlyIfLoggedInPattern = "/species/.*" // pattern for pages that can optionally display info about the logged-in user
}
if (!security.cas.loginUrl) {
    security.cas.loginUrl = 'https://auth.ala.org.au/cas/login'
}
if (!security.cas.logoutUrl) {
    security.cas.logoutUrl = 'https://auth.ala.org.au/cas/logout'
}
if (!security.cas.casServerUrlPrefix) {
    security.cas.casServerUrlPrefix = 'https://auth.ala.org.au/cas'
}
if (!security.cas.bypass) {
    security.cas.bypass = false
}
if (!auth.admin_role) {
    auth.admin_role = "ROLE_ADMIN"
}

springcache {
    defaults {
        // set default cache properties that will apply to all caches that do not override them
        eternal = false
        diskPersistent = false
        timeToLive = 600
        timeToIdle = 600
    }
    caches {
        userListCache {
            // set any properties unique to this cache
            memoryStoreEvictionPolicy = "LRU"
        }
    }
}

grails.project.groupId = appName // change this to alter the default package name and Maven publishing destination
grails.mime.file.extensions = true // enables the parsing of file extensions from URLs into the request format
grails.mime.use.accept.header = false
grails.mime.types = [html         : ['text/html', 'application/xhtml+xml'],
                     xml          : ['text/xml', 'application/xml'],
                     text         : 'text/plain',
                     js           : 'text/javascript',
                     rss          : 'application/rss+xml',
                     atom         : 'application/atom+xml',
                     css          : 'text/css',
                     csv          : 'text/csv',
                     all          : '*/*',
                     json         : ['application/json', 'text/json'],
                     form         : 'application/x-www-form-urlencoded',
                     multipartForm: 'multipart/form-data'
]

// URL Mapping Cache Max Size, defaults to 5000
//grails.urlmapping.cache.maxsize = 1000

// What URL patterns should be processed by the resources plugin
grails.resources.adhoc.patterns = ['/images/*', '/css/*', '/js/*', '/plugins/*']

grails.project.war.file = "ala-keys.war"
// The default codec used to encode data with ${}
grails.views.default.codec = "none" // none, html, base64
grails.views.gsp.encoding = "UTF-8"
grails.converters.encoding = "UTF-8"
// enable Sitemesh preprocessing of GSP pages
grails.views.gsp.sitemesh.preprocess = true
// scaffolding templates configuration
grails.scaffolding.templates.domainSuffix = 'Instance'

// Set to false to use the new Grails 1.2 JSONBuilder in the render method
grails.json.legacy.builder = false
// enabled native2ascii conversion of i18n properties files
grails.enable.native2ascii = true
// packages to include in Spring bean scanning
grails.spring.bean.packages = []
// whether to disable processing of multi part requests
grails.web.disable.multipart = false

// request parameters to mask when logging exceptions
grails.exceptionresolver.params.exclude = ['password']

// enable query caching by default
grails.hibernate.cache.queries = true

// set per-environment serverURL stem for creating absolute links
environments {
    development {
        grails.logging.jul.usebridge = true
        grails.host = "http://localhost:8080"
        grails.serverURL = "${grails.host}/${appName}"
        security.cas.appServerName = "${grails.host}"
        security.cas.contextPath = "/${appName}"
        // cached-resources plugin - keeps original filenames but adds cache-busting params
        grails.resources.debug = true
        uploadFolder = "/data/ala-keys/uploads"
    }
    test {
        grails.logging.jul.usebridge = false
        grails.host = "localhost"
        grails.serverURL = "${grails.host}/${appName}"
        security.cas.appServerName = grails.serverURL
        security.cas.contextPath = ""
        uploadFolder = "/data/ala-keys/uploads"
    }
    production {
        rails.logging.jul.usebridge = false
        grails.host = "http://localhost:8080"
        grails.serverURL = "${grails.host}/${appName}"
        security.cas.appServerName = "${grails.host}"
        security.cas.contextPath = "/${appName}"
        uploadFolder = "/data/ala-keys/uploads"
    }
}

logging.dir = (System.getProperty('catalina.base') ? System.getProperty('catalina.base') + '/logs' : '/var/log/tomcat6')

// log4j configuration
log4j = {
    appenders {
        environments {
            production {
                rollingFile name: "keys-prod",
                        maxFileSize: 104857600,
                        file: logging.dir + "/ala-keys.log",
                        threshold: org.apache.log4j.Level.DEBUG,
                        layout: pattern(conversionPattern: "%-5p: %d [%c{1}]  %m%n")
                rollingFile name: "stacktrace",
                        maxFileSize: 1024,
                        file: logging.dir + "/ala-keys-stacktrace.log"
            }
            development {
                console name: "stdout", layout: pattern(conversionPattern: "%d %-5p [%c{1}]  %m%n"),
                        threshold: org.apache.log4j.Level.DEBUG
            }
        }
    }

    root {
        debug 'keys-prod'
    }

    error 'org.codehaus.groovy.grails.web.servlet',  //  controllers
            'org.codehaus.groovy.grails.web.pages', //  GSP
            'org.codehaus.groovy.grails.web.sitemesh', //  layouts
            'org.codehaus.groovy.grails.web.mapping.filter', // URL mapping
            'org.codehaus.groovy.grails.web.mapping', // URL mapping
            'org.codehaus.groovy.grails.commons', // core / classloading
            'org.codehaus.groovy.grails.plugins', // plugins
            'org.springframework.jdbc',
            'org.springframework.transaction',
            'org.codehaus.groovy',
            'org.grails',
            'org.grails.plugin.resource',
            'org.apache',
            'grails.spring',
            'au.org.ala.cas',
            'grails.util.GrailsUtil',
            'net.sf.ehcache',
            'org.grails.plugin',
            'org.grails.plugin.resource',
            'org.grails.plugin.resource.ResourceTagLib',
            'org.grails.plugin.cachedresources',
            'grails.app.services.org.grails.plugin.resource',
            'grails.app.taglib.org.grails.plugin.resource',
            'grails.app.resourceMappers.org.grails.plugin.resource'
    debug 'au.org.ala.keys'
}
// Uncomment and edit the following lines to start using Grails encoding & escaping improvements

grails.gorm.default.constraints = {
    '*'(nullable: true)
}
