Feature: Sign In an user

    Background:
        * url 'http://localhost:8081'
        * header Content-Type = 'application/json'
        * header Accept = 'application/json'
        * def now = function(){ return java.lang.System.currentTimeMillis() }
        * def signUpJson = read('../data/signUp.json')
        * signUpJson.email = 'test' + now() + '@test.com'
        * def signInJson = {email: "", password: ""}
        * signInJson.email = signUpJson.email
        * signInJson.password = signUpJson.password
        * def fakeStudentJson = { email: test, password: faketest }

    Scenario: Signs in an user, returns forbidden if user does not exist
        Given path 'auth/signup'
        And request signUpJson
        When method post
        Then status 201
        * print signUpJson
        * print response
        And match response.name == signUpJson.name
        And match response.email == signUpJson.email
        * print signInJson

        Given path 'auth/signin'
        And request signInJson
        When method post
        Then status 200
        * print signInJson
        * print response

        And match response.student.email == signInJson.email
        And match response.token == "#notnull"

        Given path 'auth/signin'
        And request fakeStudentJson
        When method post
        Then status 403
        