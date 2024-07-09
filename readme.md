# Create a New Free Account

Scrape the registration page, fill out the form, submit it, and confirm the account using MailSlurp.

## Getting Started

### Prerequisites

- [ChromeDriver](https://googlechromelabs.github.io/chrome-for-testing/) (for Selenium)
- Java 21
- Maven
- MailSlurp API Key (for email verification)

### Installation

1. **Clone the Repository**

   Clone the repository to your local machine using the following command:

   ```sh
   git clone https://github.com/your-username/register-free-account-thermomix.git
   cd register-free-account-thermomix

2. **Configure MailSlurp**

   Add to application.properties your MailSlurp API key:
   ```sh
   api.access.key.mailslurp=your-mailslurp-api-key

3. **Set up url to scrap**

   Add to application.properties:
   ```sh
   site.url=https://www.vorwerk.com/pl/pl/s/shop/login

4. **Build and run**
   ```sh
   mvn clean install
   ```
   ```sh
   mvn spring-boot:run
   ```

### Usage

You can trigger the scraping process and get the email and password of the newly created account by making a GET request
to the `/scrap` endpoint.

Use the following curl command:

```sh
curl --location 'localhost:8080/scrap'
```

The expected response will be in JSON format and will contain the email and password. Example:

```json
{
  "email": "f5a4d0ad-635c-4460-9e92-a138d65ad8e8@mailslurp.xyz",
  "password": "superStrong123password!"
}
```
