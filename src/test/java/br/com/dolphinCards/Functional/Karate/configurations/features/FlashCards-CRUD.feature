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

        * def flashCardJson = read('../data/flashCard.json')
        * flashCardJson.disciplineName = response.name

    Scenario: Creates a new flash card, get its specific route, answers it and deletes it
        
        Given path 'flash-cards'
        And header Authorization = 'Bearer ' + token
        And request flashCardJson
        When method post
        Then status 200
        * print flashCardJson
        * print response
        And match response.question == flashCardJson.question
        And match response.answer == flashCardJson.answer
        And match response.timesAnswered == 0
        * def flashCardId = response.id

        Given path 'flash-cards/' + flashCardId
        And header Authorization = 'Bearer ' + token
        When method get
        Then status 200
        * print response
        And match response.timesAnswered == 0
        And match response.discipline.name == newDiscipline.name
        And match response.discipline.creator.email == signInJson.email

        Given path 'flash-cards/answer/' + flashCardId
        And header Authorization = 'Bearer ' + token
        And request { answerType: "EASY" }
        When method patch
        Then status 200
        * print response
        And match response.timesAnswered == 1

        Given path 'flash-cards/' + flashCardId
        And header Authorization = 'Bearer ' + token
        When method delete
        Then status 204
        * print response
        And match response == ''