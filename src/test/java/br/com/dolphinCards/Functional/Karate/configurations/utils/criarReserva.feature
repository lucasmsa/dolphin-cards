Feature: Criar um livro
    Background:
        * url "https://treinamento-api.herokuapp.com"
        * header Content-Type = 'application/json'
        * header Accept = 'application/json'
        * def bookingRequest = read('../data/bookingJson.json')

    Scenario: Criar um livro com sucesso
        Given path 'booking'
        And request 
        """
        {
            "firstname" : "Jim",
            "lastname" : "Brown",
            "totalprice" : 111,
            "depositpaid" : true,
            "bookingdates" : {
                "checkin" : "2018-01-01",
                "checkout" : "2019-01-01"
            },
            "additionalneeds" : "Breakfast"
        }
        """
        When method POST
        Then status 200

#  * def book = call read('./create.feature')
# book.response.bookingid