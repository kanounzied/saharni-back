# saharni_back

## What is Saharni?


<p align="center">
  <img src="./assets/saharni_writing.png" alt="design" />
</p>

Saharni is a mobile app that connects people and entertainment places!
<br>

Did you ever wish to go out but couldn't decide where to go? Well, __Saharni is the solution!__
<br>

An entertainment place can:
* Sign in.
* Publish a party.
* Check his parties and the people who booked for it.
  
A person can:
* Sign up.
* Check his profile.
* Book for a party.
* Rate a party.
* Check the parties he booked for.

We followed __Scrum Methodology__ while creating this project.
<br>

It was developed using __Flutter__, __SpringBoot__ and __PostgreSQL__.

## Process

To run the backend, go to the __application.properties__ file;

``` properties
#Database coordinates
spring.datasource.url=jdbc:postgresql://localhost:5432/saharni
spring.datasource.username=
spring.datasource.password=

#Mail config
spring.mail.host=smtp.gmail.com
spring.mail.port=587
spring.mail.username=
spring.mail.password=
spring.mail.properties.mail.smtp.auth=true
spring.mail.properties.mail.smtp.starttls.enable=true

```

and fill in the datasource username and password as well as the email coordinates for the JavaMailer.
<br>

## Have fun!