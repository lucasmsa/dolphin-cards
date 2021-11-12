Feature: Creates Disciplines

    Background:
        * url 'http://localhost:8081'
        * header Content-Type = 'application/json'
        * header Accept = 'application/json'
        * def now = function(){ return java.lang.System.currentTimeMillis() }
        * def signInJson = read('../data/karateTestsAuthSignIn.json')
        Given path 'auth/signin'
        And request signInJson
        When method post
        Then status 200
        And match response.token == "#notnull"
        * def token = response.token
        * def newDiscipline = { name: "" }
        * newDiscipline.name = 'disciplineTest' + now() + 'karate'
        * print newDiscipline 

    Scenario: Creates a new Discipline, returns conflict if a discipline with the same name was already created, deletes a discipline
        
        Given path 'discipline'
        And header Authorization = 'Bearer ' + token
        And request newDiscipline
        When method post
        Then status 200
        * print signInJson
        * print newDiscipline
        * print response
        And match response.name == newDiscipline.name
        And match response.creator.email == signInJson.email
        * def disciplineId = response.id

        Given path 'discipline'
        And header Authorization = 'Bearer ' + token
        And request newDiscipline
        When method post
        Then status 409
        * print response

        And match response.message == "Discipline with that name already exists for user!"
        And match response.status == 409

        Given path 'discipline/' + disciplineId
        And header Authorization = 'Bearer ' + token
        When method delete
        Then status 204
        * print response

        And match response == ''
        
    Scenario: Returns validadation errors whenever a discipline with invalid parameter is requested
        Given path 'discipline'
        And header Authorization = 'Bearer ' + token
        And request { name: "C" }
        When method post
        Then status 400
        * print response
        And match response.error == "Bad Request"
        And match response.message == "Validation failed for object='disciplinesForm'. Error count: 1"
        And match response.errors[0].defaultMessage == "Discipline length must be between 2 and 50"