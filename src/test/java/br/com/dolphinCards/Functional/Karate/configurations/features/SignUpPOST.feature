Feature: Create a new student

    Background:
        * url 'http://localhost:8081'
        * header Content-Type = 'application/json'
        * header Accept = 'application/json'
        * def now = function(){ return java.lang.System.currentTimeMillis() }
        * def signUpJson = read('../data/signUp.json')
        * signUpJson.email = 'test' + now() + '@test.com'

    Scenario: Creates a new student
        Given path 'auth/signup'
        And request signUpJson
        When method post
        Then status 201
        * print signUpJson
        * print response
        And match response.name == signUpJson.name
        And match response.email == signUpJson.email

    # @e2e
    # Scenario: Validar retorno 500 quando o payload da reserva estiver inválido
    #     Given path 'booking'
    #     * remove bookingJson.firstname
    #     And request bookingJson
    #     When method post
    #     Then status 500
    #     * print bookingJson
    #     * print response

    # @e2e
    # Scenario: Validar a criacao de mais de um livro em sequencia
    #     #book 1
    #     * def book1 = call read('../utils/criarReserva.feature')

    #     #book 2
    #     * def book2 = call read('../utils/criarReserva.feature')
    #     Then match book2.response.bookingid == "#notnull"

    # @e2e
    # Scenario: Criar uma reserva enviando mais parametros no payload da reserva
    #     Given path 'booking'
    #     * set bookingJson.extra = "valor sobrando"
    #     And request bookingJson
    #     When method post
    #     Then status 200

    # @e2e
    # Scenario: Validar retorno 418 quando o header Accept for invalido
    #     Given path 'booking'
    #     And header Accept = 'application/joaquim'
    #     And request bookingJson
    #     When method post
    #     Then status 418