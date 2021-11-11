Feature: Excluir uma reserva
    Background:
        * url "https://treinamento-api.herokuapp.com"
        * header Content-Type = 'application/json'
        * header Accept = 'application/json'
        * header Authorization = 'Basic YWRtaW46cGFzc3dvcmQxMjM='
        * def book = call read('../utils/criarReserva.feature')
        * def bookingId = book.response.bookingid

    @acceptance
    Scenario: Excluir um reserva com sucesso
        Given path 'booking', bookingId
        When method DELETE
        Then status 201

    @e2e
    Scenario: Tentar excluir um reserva que não existe
        Given path 'booking', 3215
        When method DELETE
        Then status 405

    @e2e
    Scenario: Tentar excluir uma reserva sem autorização
        Given path 'booking', bookingId
        And header Authorization = 'Basic tokenInvalido'
        When method DELETE
        Then status 403