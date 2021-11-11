@health_check
Feature: Healthcheck
    Background: 
        * url "https://treinamento-api.herokuapp.com"

    Scenario: Verificar se API está online
        Given path 'ping'
        When method get
        Then status 201
