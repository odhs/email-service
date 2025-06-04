# Exemplo de Microsserviço _Amazon Simple Service Email_ em _Java Springboot_

Este projeto é um microsserviço desenvolvido em _Java Spring Boot_ que utiliza o _Amazon Simple Email Service (SES)_ para envio de emails. Ele segue o padrão de arquitetura _Clean Architecture_, permitindo flexibilidade para trocar o provedor de email no futuro.

Essa aplicação recebe um `JSON` por `POST` _APIRest_ com parâmetros para um email e envia email usando o serviço de email.

## 🚀 Como Executar o Projeto

### Pré-requisitos

- **_Java 24_** ou superior instalado. [Java 24 Download](https://jdk.java.net/24/).
- **_Maven_** instalado _ou_ uma IDE como [VSCode](https://code.visualstudio.com/)(com Extensões para _SpringBoot_) ou [IntelliJ IDEA](https://www.jetbrains.com/pt-br/idea/)
- Conta na **AWS** com o serviço _SES_ configurado.
- Configuração das variáveis de ambiente para as credenciais da AWS.

### Passo 1

1. Clone o repositório:

   ```bash
   git clone https://github.com/seu-usuario/email-service.git
   cd email-service
   ```

### Passo 2: _Amazon SES_

Caso queria compilar com outra versão siga abaixo o PULE este passo.

Para descobrir a última versão e configurações da dependência `aws-java-sdk-ses` que está no arquivo `pom.xml`, você pode consultar o repositório oficial do _Maven Central_. Siga os passos abaixo:

1. Acesse o site do _Maven Central_:  
   [https://search.maven.org/](https://search.maven.org/)

2. Pesquise pelo `groupId` e `artifactId` da dependência:
   No campo de busca, insira:

   ```sh
   com.amazonaws aws-java-sdk-ses
   ```

3. Verifique a página do artefato:  
   Na página de resultados, clique no artefato correspondente (`aws-java-sdk-ses`) para ver as versões disponíveis.

4. Escolha a versão mais recente:  
   A versão mais recente estará listada no topo ou destacada como "Latest Version".

5. Atualize seu `pom.xml` com a nova versão.

### Passo 3: No web site da [_AWS_](https://aws.amazon.com/)

#### _Amazon Simple Email Services_

Após logar no AWS você precisa registrar uma identidade no [SES](https://us-east-1.console.aws.amazon.com/ses/home?region=us-east-1#/identities) (Simple Email Service). Adicione o email e verifique.

#### IAM

Criar um novo usuário [IAM User](https://us-east-1.console.aws.amazon.com/iam/home?region=us-east-1#/users) para representar a aplicação. Com única permissão de _AmazonSESFullAccess_. E crie uma chave para esse usuário.
Essa é a opção mais prática para testes. Em produção a aplicação estaria hospedada e rodando em algum lugar, como em um EC2 (Máquina Virtual da Amazon) e então haveria uma regra (role) de permissão de acesso a este usuário.

> **Nota:** Atribua um perfil do IAM a recursos de computação, como instâncias do EC2 ou funções do Lambda, para fornecer automaticamente credenciais temporárias para habilitar o acesso.

### Passo 4: Variáveis de Ambiente

1. **Defina as variáveis de ambiente no sistema operacional:**

   No _Windows_ (via Prompt de Comando ou PowerShell):

   ```cmd
   set AWS_ACCESS_KEY_ID=<SEU_AWS_ACCESS_KEY_ID>
   set AWS_SECRET_KEY=<SEU_SECRET_KEY>
   set AWS_REGION=us-east-1
   set EMAIL_SOURCE=<SEU_EMAIL_CONFIGURADO_NA_AWS>
   ```

   No _Linux/macOS_ (via terminal):

   ```bash
   export AWS_ACCESS_KEY_ID=<SEU_AWS_ACCESS_KEY_ID>
   export AWS_SECRET_KEY=<SEU_SECRET_KEY>
   export AWS_REGION=us-east-1
   export EMAIL_SOURCE=<SEU_EMAIL_CONFIGURADO_NA_AWS>
   ```

Ou coloque as informações no arquivo `application.properties` no lugar das chamadas para variáveis de ambiente:

```sh
aws.accessKeyId=${AWS_ACCESS_KEY_ID}
aws.secretKey=${AWS_SECRET_KEY}
aws.region=${AWS_REGION}
email.source=${EMAIL_SOURCE}
```

Pronto, o projeto está configurado e pronto para rodar.

### Passo 5: Executando a Aplicação

Compile e execute o projeto com o _Maven_ como abaixo:

```bash
mvn clean package
java -jar target/email_service-0.0.1-SNAPSHOT.jar
```

ou use a IDE escolhida para rodar automaticamente.

## 📬 Testando o Envio de Email

É possível testar localmente sem fazer o deploy.
Após iniciar a aplicação localmente envie uma requisição POST.

Eu usei a extenção [REST Client](https://marketplace.visualstudio.com/items/?itemName=humao.rest-client) para VSCode.

Onde você cria um arquivo test.http e colocar o seguinte conteúdo dentro:

```sh
###

POST http://localhost:8080/api/v1/email/send HTTP/1.1
Content-Type: application/json

{
  "to": "seu_email_com_identidade_verificada@gmail.com",
  "subject": "Test Email",
  "body": "This is a test email sent from the API."
}
```

Após isso irá aparecer `Send Request` acima da linha do POST, basta clicar e poderá conferir a resposta que deverá ser `Email sent successfully`.

---

## 🧱 Arquitetura

O projeto segue o padrão _Clean Architecture_, com as seguintes camadas:

1. **Core**: Contém as regras de negócio e casos de uso.
2. **Application**: Implementa os casos de uso definidos no Core.
3. **Adapters**: Define interfaces para comunicação com serviços externos.
4. **Infra**: Implementa as interfaces dos Adapters, como o cliente AWS SES.
5. **Controllers**: Exposição de endpoints REST para interação com o sistema.

Core -> Application -> <Gateway>Adapters -> Infra

### CORE

É o núcleo da aplicação. Aqui temos o alto nível, ou seja, o que a aplicação faz, sem importar como.

- Regras de negócio
- Casos de uso

```java
public interface EmailSenderUseCase {
  void sendEmail(String to, String subject, String body);
}
```

### APPLICATION

Na camada da aplicação, temos a implementação dos casos de uso da camada CORE. A camada application só conhece a camada CORE.

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

Existe para que a camada de application não dependa diretamente da implementação externa, no caso, o pacote da AWS. Isso permite que, no futuro, a AWS possa ser subsituída sem prejuízos para a aplicação.

Embora pareça o mesmo método do UseCase a diferença é que aqui é contrato, e o UseCase é o alto nível da aplicação.

```java
public interface EmailSenderGateway {
  void sendEmail(String to, String subject, String body);
}
```

### INFRA

É responsável por interagir com por interagir com serviços e estruturas externas.
Aqui é implementada a comunicação com o AWS-SES (_Amazon Simple Email Services_).

### EXCEÇÕES

Exceções personalizadas fazem parte do CORE da aplicação, porque fazem parte da regra de negócio.

## Classes

### **1. Classes e Interfaces**

- **Core**

  - `EmailSenderUseCase` (Interface): Define o contrato para envio de emails.
  - `EmailRequest` (Record): Representa a solicitação de envio de email.
  - `EmailServiceException` (Classe): Exceção personalizada para erros no serviço de email.

- **Application**

  - `EmailSenderService` (Classe): Implementa o caso de uso definido em `EmailSenderUseCase`.

- **Adapters**

  - `EmailSenderGateway` (Interface): Contrato para comunicação com serviços externos de envio de email.

- **Infra**

  - `AwsSesEmailSender` (Classe): Implementa `EmailSenderGateway` para enviar emails usando o AWS SES.
  - `AwsSesConfig` (Classe): Configuração do cliente AWS SES.

- **Controllers**

  - `EmailSenderController` (Classe): Controlador REST para receber solicitações de envio de email.

### **2. Relacionamentos**

- `EmailSenderService` implementa `EmailSenderUseCase`.
- `AwsSesEmailSender` implementa `EmailSenderGateway`.
- `EmailSenderService` depende de `EmailSenderGateway`.
- `EmailSenderController` depende de `EmailSenderService`.

### Diagrama de Classes

![Diagrama de Classes](docs/uml/diagrama_classes.png)

Veja também o diagrama de classes feito em:

- [PlantUML](http://www.plantuml.com/plantuml/duml/hLD1pzem3BttLrZV2RI1Tja5JGY4gcbNglq0axfTHffaiJl66Fzz6LLfKUY97NheY_tytekpOS4WRMCoGvM0E0Yw_YPGwa1AjBCP7xNr6B01wqfYyl3nG-PH7R4cUDkvG6zmoG2q5GeLAeAyGMqAiYM5hqmxPBeL0Bm3ZkZjU2Pk1OK4RUltmEtXEoaPW8Chp_tVFBPUuwSCCdVSU_hH4ikTTU3gW37X21spfgG5XPzQKVvBaRyPlsQ3pQttHYlyXqUtG-FWj6lldIw0XgFFA7J2E3fFi5TZUT1OeXSAd5n7CXKk_tFk5ri57AUoFN51F1fx3mKR_ErieHjlQ1pw2hOJ9wyAVTIqSxRITMz_KiviFYqeiHyv9JXZAF34QWmLpxQZZs3SCMK-T8k_UdW6KUoUs3lBb5lHSlxK1R3kfQR4i5p-MJzvfrbwTLrbkhXFI1UBoSeaYQuuTRo9VsrZ_W80)

  - PNG: `docs/uml/diagrama_classes.png`
  - SVG: `docs/uml/diagrama_classes.svg`
  - Source: `docs/uml/diagrama_classes.wsd`

- [Mermaid](https://www.mermaidchart.com/play?utm_source=mermaid_live_editor&utm_medium=toggle#pako:eNqtVNFOwjAU_ZWGJ4juBxZCMuc0JATIhm99KdsFq1uHbVGR-O-2G7Pt2IgJ7qnrPfec03t7exykZQYDf-B5HmZpyTZ062OGkHyGAnzEIdt_ehnhr5hJKnO1FRWE5igBlgHHrM7LiRD3lGw5KTDLKIdU0pKhuxVmWDJSgNiRFFC4iCN01PT1V-XVhDXfk4CQCNAYLBvUjVCxCjSUpY8SySnb3iKxX78oHbOxLrND8zcyIt-dejG87UHIlpLhdw24Uk7MVrVEm0XPYfk7TSH6TGFXFerYztQSyrgpXbBczqZhsJou5g36rHonWnMmD0zwkUj4IAffTjjtNfArCq28unbvg-UqipNerydp4_Vqcb0yBqbzhzhw1YMPkYCwPFiFIgX5KllCi10Odot8FPRE_sd3211YTaBVlT5jwxG65M2id_oSLuareDGbXWqN8iB5medOgeDsmvkdV6-jKLweNN8ZO20-VpZKJiDSD8uh3cnaVbxXweJ8UBRIS43HlEngG3WyyaTjHbmIMZe_ouyYI8_rJtXw88vUQv_S_428D241o5Xh9Lr7WdEZ7RJiNvj-AQAwA_E)
  - Source: `docs/uml/diagrama_classes.nmd`

---

## 📝 TODO

- **Adicionar suporte a múltiplos provedores de email**:
  - Implementar integração com provedores como _SendGrid_, _Mailgun_ e _SparkPost_.
  - Criar uma lógica de fallback para alternar automaticamente entre provedores caso um deles falhe.
  - Garantir que o serviço possa ser transferido rapidamente para outro provedor sem afetar os clientes.

- **Melhorar a cobertura de testes**:
  - Adicionar testes unitários e de integração para os novos provedores.
  - Simular falhas nos provedores para validar o comportamento do fallback.

- **Documentação**:
  - Atualizar a documentação para incluir instruções de configuração e uso dos novos provedores.

Outros provedores de email:

- [SendGrid](https://sendgrid.com/user/signup) - [SendGrid Documentation](https://sendgrid.com/docs/API_Reference/Web_API/mail.html)
- [Mailgun](http://www.mailgun.com) - [MailGun Documentation](http://documentation.mailgun.com/quickstart.html#sending-messages)
- [SparkPost](https://www.sparkpost.com/) - [SparkPost Hub](https://developers.sparkpost.com/)
- [Amazon SES](http://aws.amazon.com/ses/) - [Simple Send Documentation](http://docs.aws.amazon.com/ses/latest/APIReference/API_SendEmail.html)

---

## 📖 Padrões de Commit

Este projeto segue o padrão [_Conventional Commits_](https://www.conventionalcommits.org/):

- `WIP` Commits, que adicionam implementações apenas para salvar progresso
- `feat` Commits, que adicionam ou removem uma nova funcionalidade à API ou UI
- `fix` Commits, que corrigem um bug na API ou UI de um commit `feat` anterior
- `refactor` Commits, que reescrevem/restruturam o código, mas não alteram o comportamento da API ou UI
- `perf` Commits são `refactor` especiais, que melhoram o desempenho
- `style` Commits, que não afetam o significado (espaços em branco, formatação, ponto e vírgula ausente, etc.)
- `test` Commits, que adicionam testes ausentes ou corrigem testes existentes
- `docs` Commits, que afetam apenas a documentação
- `build` Commits, que afetam componentes de build como ferramentas de build, pipeline de CI, dependências, versão do projeto, ...
- `ops` Commits, que afetam componentes operacionais como infraestrutura, implantação, backup, recuperação, ...
- `chore` Commits diversos, como modificar `.gitignore`

## DICAS

O _Maven_, por padrão, salva os pacotes _Java_ (arquivos JAR) numa pasta local no seu computador, geralmente em `~/.m2/repository` (Unix/Linux/macOS) ou `C:\Users\<username>\.m2\repository` (Windows). Essa pasta é o repositório local do _Maven_.

---

## 📝 Licença

Este projeto está sob a licença MIT. Consulte o arquivo `LICENSE` para mais detalhes.
