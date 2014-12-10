package au.org.ala.keys

import grails.transaction.Transactional

@Transactional
class AuthorisationService {

    def validate(apiKey, alaUserId) {
        return true
    }

    def getUserId() {
        return 0
    }

    def isValidUser() {
        return true
    }

    def isValidAdmin() {
        return true
    }
}
