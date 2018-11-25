## Integrantes
| Nome | Ra |
| ---- | ---|
|Alex Augusto | 1700072 |
|Cinthia Queiroz | 1700693 |
|Michael da Silva de Souza | 1700381 |
|Fabio Aurelio Abe Nogueira | 1700603 |
|Gabriel Bueno | 1601606 |
|Henrique Borges da Silva | 1700054 |

---

## Usuarios de acesso

* Alunos
    * email: java@email.com.br
    * senha: senha
    * email: cicinth@email.com.br
    * senha: senha
    * email: alan@email.com.br
    * senha: senha
    * email: mestre@email.com.br
    * senha: senha
    * email: gabs@email.com.br
    * senha: senha
    * email: russo@email.com.br
    * senha: senha
* Admin
    * email: admin@email.com.br
    * senha: senha
* Professores
    * email: yuri@email.com.br
    * senha: senha

    * email: fernando@email.com.br
    * senha: senha
---
## Como iniciar
Inicie a aplicacao com o profile de `DEV`
via [IntelliJ](https://www.jetbrains.com/idea/) adicione a propriedade em `VM options`:
```Java
Dspring.profiles.active = dev 
```
---
Com o [Maven](https://maven.apache.org/) devidamente instalado na maquina, rode o seguinte comando:
```Java
mvn spring-boot:run -Dspring-boot.run.profiles=dev
```
