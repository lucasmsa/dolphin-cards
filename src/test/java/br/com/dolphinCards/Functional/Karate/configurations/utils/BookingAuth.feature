Feature: Booking - Auth

    Scenario: Gerar token

        Given url 'https://treinamento-api.herokuapp.com/auth'
        And header Content-Type = 'application/json'
        And request 
        """
        {
            "username" : "admin",
            "password" : "password123"
        }
        """
        When method post
        * print response
        * def tokenAuth = response.token
        * print tokenAuth
        Then match response.token == tokenAuth
