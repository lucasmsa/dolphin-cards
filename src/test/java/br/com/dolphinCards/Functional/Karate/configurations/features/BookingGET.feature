Feature: Listar reservas
    Background: 
        * call read('../utils/BookingAuth.feature')
        * url 'https://treinamento-api.herokuapp.com'
        * header Accept = 'application/json'


    @contract
    Scenario: Garantir o contrato do retorno da lista de reservas
        Given path 'booking'
        When method get
        Then status 200
        And match each response == { bookingid: "#number" }

    @contract
    Scenario: Garantir o contrato do retorno de uma reserva específica
        Given path 'booking/1'
        When method get
        Then status 200
        * def specificBooking = read('../data/specificBooking.json')
        And match response == specificBooking


    @acceptance
    Scenario: Listar IDs das reservas
        Given path 'booking'
        When method get
        Then status 200
        * print response

    @acceptance
    Scenario: Listar uma reserva específica
        Given path 'booking/1'
        When method get
        * print response
        Then status 200

    @acceptance
    Scenario Outline: Listar IDs de reservas utilizando o filtro <parametro>
        * def varParams = <valorParametros>
        Given path 'booking'
        And params varParams
        When method get   
        Then status 200
        * print response
        Examples:
        | by    | parametro                        | valorParametros    |  deveConter   | 
        | by    | firstname                        | {firstname:Mark}  |               |
        | by    | lastname                         | {lastname:Smith}                   |               |
        | by    | checkin                          | {checkin:'2019-07-05'}                   |               |
        | by    | checkout                         | {checkout:'2019-09-10'}                   |               |
        | by    | checkout and checkout            | {checkin:'2019-07-05', checkout:'2019-09-10'}                   |               |
        | for   | name, checkin and checkout date  | {firstname:Mark, lastname:Smith, checkin:'2019-07-05', checkout:'2019-09-10' }                   |               |

    @e2e
    Scenario: Visualizar erro de servidor 500 quando enviar filtro mal formatado
        Given path 'booking'
        And param checkout = '2013-02-0'
        When method get
        Then status 500
