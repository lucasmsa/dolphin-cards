Feature: Create a new student

    Background:
        * url 'http://localhost:8081'
        * header Content-Type = 'application/json'
        * header Accept = 'application/json'
        * def now = function(){ return java.lang.System.currentTimeMillis() }
        * def signUpJson = read('../data/signUp.json')
        * signUpJson.email = 'test' + now() + '@test.com'

    Scenario: Creates a new student and returns conflict error when trying to create a student with an existing e-mail
        Given path 'auth/signup'
        And request signUpJson
        When method post
        Then status 201
        * print signUpJson
        * print response
        And match response.name == signUpJson.name
        And match response.email == signUpJson.email

        Given path 'auth/signup'
        And request signUpJson
        When method post
        Then status 409
        * print signUpJson
        * print response
        And match response.status == 409
        And match response.message == "Student with this e-mail already exists"