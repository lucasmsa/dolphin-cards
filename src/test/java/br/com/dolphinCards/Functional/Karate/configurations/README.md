# Treinamento de Testes de API

## Conceitos:
- O que é uma Web Service / API?
- Estratégia de testes de API
	- healthcheck
	- contrato
	- aceitação
	- funcional
- Estrutura basica para criar testes para uma API Rest
- Como manipular diferentes aspectos de requisições
	- GET / POST / PUT / DELETE
	- Headers
	- Query params
	- Payload
- Como garantir diferentes aspectos de respostas
	- Status
	- Response payload
- Como estruturar um projeto
- Como executar os testes desenvolvidos
- Como adicionar a execução destes testes em um CI


## Ferramenta - Karate DSL:

O Karate DSL é uma ferramenta desenvolvida em Java que permite o desenvolvimento de testes de APIs utilizando uma sintaxe semelhante à do Gherkin. 
> Importante: Karate não é BDD!

Sua utilização pode ser feita, basicamente, de duas formas:
- Como biblioteca em um projeto Java (Maven ou Gradle)
- Como arquivo executável (standalone), fazendo download direto no site

> Importante: Neste treinamento estamos utilizando o modo standalone.

Dado que esta ferramenta utiliza uma sintaxe semelhante ao Gherkin, os testes são implementados em um arquivo de *feature*. Logo, podemos utilizar as seguintes palavras chaves:
- Feature
- Background
- Scenario
- Scenario Outline / Examples
- Given / When / Then / And
- \* - *(sim, um asterisco, que serve como coringa)*


Para o desenvolvimento dos testes, a ferramenta dispõe de alguns comandos pré-definidos, que são descritos no formato de *steps*. Os principais comandos que serão utilizados no treinamento são:

- [url 'parametro'](https://intuit.github.io/karate/#url "url 'parametro'")
- [path 'parametro'](https://intuit.github.io/karate/#path "path 'parametro'")
- [request 'payload'](https://intuit.github.io/karate/#request "request 'payload'")
- [method 'método http'](https://intuit.github.io/karate/#method "method 'método http'")
- [status 'status http'](https://intuit.github.io/karate/#status "status 'status http'")
- [param 'nome query param' = 'valor do query param'](https://intuit.github.io/karate/#param "param 'nome query param' = 'valor do query param'")
- [header 'nome header' = 'valor do header'](https://intuit.github.io/karate/#header "header 'nome header' = 'valor do header'")
- [response](https://intuit.github.io/karate/#response "response")
- [match 'expressao'](https://intuit.github.io/karate/#match "match 'expressao'")
- [set payload.valor](https://intuit.github.io/karate/#set "set payload.valor")
- [remove payload.valor](https://intuit.github.io/karate/#remove "remove payload.valor")
- [def 'nome da variavel' = 'valor da variavel'](https://intuit.github.io/karate/#def "def 'nome da variavel' = 'valor da variavel'")
- [print 'conteudo'](https://intuit.github.io/karate/#print "print 'conteudo'")
- [read](https://intuit.github.io/karate/#reading-files "read")
- [call read](https://intuit.github.io/karate/#call "call read")

> Em caso de dúvidas, cada tópico possui link direto para a sua explicação.

Uma vez criadas features com os cenários de testes, podemos executá-las com a ajuda do karate.jar. O comando base para execução dos testes é:

`java -jar karate.jar diretorioComAsFeatures`

Neste formato de execução, podemos utilizar outros comandos em conjunto, para:
- filtrar os testes desejados por tags: `-t` ou `--tags`
- executar os testes em paralelo: `-T` ou `--threads`
- dentre outros (confira o --help ou [aqui](https://intuit.github.io/karate/karate-netty/#standalone-jar "aqui"))


Finalizada a execução dos testes, será gerado um relatório com o resultado dos testes no diretório ***target/cucumber-html-reports***. Por padrão, o nome do arquivo é ***overview-features.html*** e deve ser aberto no navegador.


## Sugestão de estrutura de projeto:
```
.
├── data
│   ├── payload1.json
│   └── payload2.json
├── features
│   ├── MinhaFuncionalidade.feature
│   ├── NossaFuncionalidade.feature
│   ├── VossaFuncionalidade.feature
│   └── BossaFuncionalidade.feature
├── karate.jar
└── utils
    ├── funcionalidadeUtil.feature
    └── funcaoJavascriptUtil.json
```
No diretório ***data*** ficam os arquivos com exemplos de payloads, contratos, e quaisquer outros arquivos que possuam dados que serão utilizados durante o teste.

No diretório ***features*** ficam os arquivos de features, com a implementação dos cenários de testes. Caso necessário, podem ser criados sub-diretórios a partir deste, para facilitar a organização a medida em que o projeto cresce.

No diretório ***utils*** ficam os arquivos com funções utilizadas em mais de um cenário de testes, como features para autenticação, funções para gerar números aleatórios ou capturar data atual em javascript, etc.

Por fim, o arquivo executável ***karate.jar*** é o responsável por executar os testes criados e gerar o relatório com o resultado da execução dos testes. Importante: não é necessário abrir este arquivo, apenas utilizá-lo para executar os testes via linha de comando.

> Existe ainda o diretório ***target***, gerado após a execução dos testes.

### Exercícios aplicando a estrutura, com a [FakeAPI](http://fakerestapi.azurewebsites.net/swagger/ui/index#/ "FakeAPI"):
> Lembre-se que é uma API Fake, ou seja, não possui consistência de dados.

1. Criar um cenário de healthcheck para todos os métodos GET de:
	- Activities, Authors, Books, CoverPhotos & Users;
2. Criar um teste de contrato para os endpoints / métodos:
	- Activities -> GET /api/Activities/{id}
	- Authors -> GET /api/Authors/{id}
3. Criar testes de aceitação para o endpoint /Books, executando os seguintes cenários:
	- Criar um novo livro e verificar se foi cadastrado com sucesso (*POST*)
	- Listar os livros cadastrados e verificar se a quantidade é igual a 200 (*GET*)
	- Excluir um livro já cadastrado (*DELETE*)
	- Editar um livro já cadastrado (*UPDATE*)
4. Criar testes para validar os demais fluxos de exceção, executando os seguintes cenários:
	- Tentar excluir um livro informando uma String como id (POST) (400)
	- Tentar visualizar um livro informando um id não cadastrado (ex.: 201) (GET, 404)


## Informações da API do Treinamento:

Esta é uma API Rest, desenvolvida para este treinamento e *hosteada* na nuvem utilizando o heroku. Para mais informações sobre a API, veja os links abaixo:
- [API para o Desafio (restful-booker)](https://treinamento-api.herokuapp.com/apidoc/index.html "API para o Desafio (restful-booker)")
- [Código fonte da API](https://github.com/samlucax/restful-booker "Código fonte da API")

#### Desafios, utilizando a API:

##### **Implemente os seguintes cenários, adicionando as respectivas tags:**

**Tag** @healthcheck:

- /*HEALTHCHECK*
	- Verificar se API está online

**Tag** @contract :

- /*GET*
	- Garantir o contrato do retorno da lista de reservas
	- Garantir o contrato do retorno de uma reserva específica

**Tag** @acceptance:

- /*DELETE*
	- Excluir um reserva com sucesso 

- /*GET*
	- Listar IDs das reservas
	- Listar uma reserva específica
	- Listar IDs de reservas utilizando o filtro firstname
	- Listar IDs de reservas utilizando o filtro lastname
	- Listar IDs de reservas utilizando o filtro checkin
	- Listar IDs de reservas utilizando o filtro checkout
	- Listar IDs de reservas utilizando o filtro checkout and checkout
	- Listar IDs de reservas utilizando o filtro name, checkin and checkout date

- /*POST*
	- Criar uma nova reserva

- /*PUT*
	- Alterar uma reserva usando o token
	- Alterar uma reserva usando o Basic auth

**Tag** @e2e :

- /*DELETE*
	- Tentar excluir um reserva que não existe
	- Tentar excluir uma reserva sem autorização

- /*GET*
	- Visualizar erro de servidor 500 quando enviar filtro mal formatado

- /*POST*
	- Validar retorno 500 quando o payload da reserva estiver inválido
	- Validar a criacao de mais de um livro em sequencia
	- Criar uma reserva enviando mais parametros no payload da reserva 
	- Validar retorno 418 quando o header Accept for invalido

- /*PUT*
	- Tentar alterar uma reserva quando o token não for enviado
	- Tentar alterar uma reserva quando o token enviado for inválido
	- Tentar alterar uma reserva que não existe


------------

## Integração contínua ✅

Em resumo:
> Integração contínua é um processo para, através de validações automáticas, garantir que novas funcionalidades ou refatorações **não impactarão negativamente** no código/projeto/produto existente.

Para exemplificar o uso de integração contínua no processo de execução de testes automatizados, vamos utilizar o GitlabCI. Para isso, siga os seguintes passos:
1. Crie uma conta no Gitlab
2. Acesse o [projeto-treinamento-api](https://gitlab.com/samuelborgeslucas/projeto-treinamento-api "projeto-treinamento-api")
3. Faça fork deste projeto para sua conta
4. Na raiz do projeto, crie um novo arquivo chamado .gitlab-ci.yml
5. Adicione o seguinte conteúdo ao arquivo, e faça commit / push:

```
image: openjdk

health_check:
  script:
    - java -jar karate.jar -t @health_check features

contract:
  script:
    - java -jar karate.jar -t @contract features

acceptance:
  script:
    - java -jar karate.jar -t @acceptance features

e2e:
  script:
    - java -jar karate.jar -t @e2e features

```
6. Acesse CI/CD -> Pipelines, e veja a execução sendo feita.

------------

##### FAQ:
- [O que é API?](https://vertigo.com.br/o-que-e-api-entenda-de-uma-maneira-simples/ "O que é API?")
- [O que é protocolo HTTP?](https://developer.mozilla.org/pt-BR/docs/Web/HTTP/Overview "O que é protocolo HTTP?")
- [O que é Rest?](https://blog.caelum.com.br/rest-principios-e-boas-praticas/ "O que é Rest?")