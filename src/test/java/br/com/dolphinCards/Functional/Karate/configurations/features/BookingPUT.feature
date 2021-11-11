Feature: PUT Booking

    Background:
        * url "https://treinamento-api.herokuapp.com"
        * header Content-Type = 'application/json'
        * header Accept = 'application/json'
        * def book = call read('../utils/criarReserva.feature')
        * def bookingId = book.response.bookingid
        * call read('../utils/BookingAuth.feature')
        * def bookingRequest = read('../data/bookingJson.json')
        * set bookingRequest.firstname = 'Alteradaco'

    @acceptance
    Scenario: Alterar uma reserva usando o token
        Given path 'booking/' + bookingId
        And header Cookie = "token="+tokenAuth
        And request bookingRequest
        When method put
        Then status 200

    @acceptance
    Scenario: Alterar uma reserva usando o Basic auth
        Given path 'booking/' + bookingId
        And header Authorization = 'Basic YWRtaW46cGFzc3dvcmQxMjM='
        And request bookingRequest
        When method put
        Then status 200

    @e2e
    Scenario: Tentar alterar uma reserva quando o token não for enviado
        Given path 'booking/' + bookingId
        And request bookingRequest
        When method put
        Then status 403

    @e2e
    Scenario: Tentar alterar uma reserva quando o token enviado for inválido
        Given path 'booking/' + bookingId
        And header Authorization = 'Basic blablablablabla='
        And request bookingRequest
        When method put
        Then status 403

    @e2e
    Scenario: Tentar alterar uma reserva que não existe
        * def bookingIdNotExist = bookingId + 1000
        Given path 'booking/' + bookingIdNotExist
        And header Authorization = 'Basic YWRtaW46cGFzc3dvcmQxMjM='
        And request bookingRequest
        When method put
        Then status 405