#

## Arquitetura

Core -> Application -> <Gateway> -> Adapters

### CORE

É o núcleo da aplicação.

- Regras de negócio
- Casos de uso
- Define o que o app faz, mas não como ele faz.

```java
public interface EmailSenderUseCase {
  void sendEmail(String to, String subject, String body);
}
```

Então aqui temos o alto nível, é o que a aplicação faz porém sem importar como.

### APPLICATION

Na camada da aplicação temos a implementação dos casos de uso da camada CORE. A camada application só conhece a camada CORE.

```java
public class EmailSenderService implements EmailSenderUseCase {

  @Override
    public void sendEmail(String recipient, String subject, String body) {

  private final EmailSenderGateway emailSenderGateway;

    public EmailSenderService(EmailSenderGateway emailSenderGateway) {
        this.emailSenderGateway = emailSenderGateway;
      }
    }
}
```

### ADAPTER

Existe para que a camada de application não dependa diretamente da implementação externa, no caso, o pacote da AWS, isso permite que no futuro a AWS poderá ser subsituída sem prejuízos para a aplicação.

Embora pareça o mesmo método do UseCase a diferença é que aqui é contrato, e o UseCase é o alto nível da aplicação.

```java
public interface EmailSenderGateway {
  void sendEmail(String to, String subject, String body);
}
```

## DICAS

O Maven, por padrão, salva os pacotes Java (arquivos JAR) numa pasta local no seu computador, geralmente em ~/.m2/repository (em sistemas Unix/Linux/macOS) ou C:\Users\<username>\.m2\repository (em Windows). Essa pasta é o repositório local do Maven.

## GIT

### Types

Using [Conventional Commits](https://www.conventionalcommits.org/en/v1.0.0/#specification)

- API or UI relevant changes
- `WIP` Commits, that add implementations only to save
- `feat` Commits, that add or remove a new feature to the API or UI
- `fix` Commits, that fix an API or UI bug of a preceded `feat` commit
- `refactor` Commits, that rewrite/restructure your code, however do not change any API or UI behaviour
- `perf` Commits are special `refactor` commits, that improve performance
- `style` Commits, that do not affect the meaning (white-space, formatting, missing semi-colons, etc)
- `test` Commits, that add missing tests or correcting existing tests
- `docs` Commits, that affect documentation only
- `build` Commits, that affect build components like build tool, ci pipeline, dependencies, project version, ...
- `ops` Commits, that affect operational components like infrastructure, deployment, backup, recovery, ...
- `chore` Miscellaneous commits e.g. modifying `.gitignore`
