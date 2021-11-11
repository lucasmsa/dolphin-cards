Feature: Criar reserva

    Background:
        * url 'https://treinamento-api.herokuapp.com'
        * header Content-Type = 'application/json'
        * header Accept = 'application/json'
        * def bookingJson = read('../data/bookingJson.json')

    @acceptance
    Scenario: Criar uma nova reserva
        Given path 'booking'
        And request bookingJson
        When method post
        Then status 200
        * print bookingJson
        * print response
        And match response.bookingid == "#notnull"
        And match response.booking == bookingJson

    @e2e
    Scenario: Validar retorno 500 quando o payload da reserva estiver inválido
        Given path 'booking'
        * remove bookingJson.firstname
        And request bookingJson
        When method post
        Then status 500
        * print bookingJson
        * print response

    @e2e
    Scenario: Validar a criacao de mais de um livro em sequencia
        #book 1
        * def book1 = call read('../utils/criarReserva.feature')

        #book 2
        * def book2 = call read('../utils/criarReserva.feature')
        Then match book2.response.bookingid == "#notnull"

    @e2e
    Scenario: Criar uma reserva enviando mais parametros no payload da reserva
        Given path 'booking'
        * set bookingJson.extra = "valor sobrando"
        And request bookingJson
        When method post
        Then status 200

    @e2e
    Scenario: Validar retorno 418 quando o header Accept for invalido
        Given path 'booking'
        And header Accept = 'application/joaquim'
        And request bookingJson
        When method post
        Then status 418