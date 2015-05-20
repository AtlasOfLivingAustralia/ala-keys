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

    def isValidUser(projectId) {
        return true
    }

    def isValidAdmin() {
        return true
    }

    def getUserForUserId(userId) {
        [email: 'test', alaId: '0', firstname: 'test', lastname: 'test', userId: '1']
    }
}
