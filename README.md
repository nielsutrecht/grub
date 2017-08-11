# Grub

A Spring Boot / Angular 4 reference project

# Introduction

Grub is in essence a [MyFitnessPal](https://www.myfitnesspal.com/) clone. Very much a work in progress but it's planned
to be used in my household to track our calorie intake.

On the technical side it is my 'reference' project where I keep track of what I consider the current best practices when
it comes to using Spring to build REST API's, which is what I spend most of my time doing. Since most of what I do is
in the context of micro services I decided to go for a different approach with this application: a modular monolith.

# Tech

The application is Spring Boot REST api that also serves static content; an Angular 4 Single Page Application. Maven is used
for the build / dep management. A full build will also build the front-end and include it in the back-end Jar. This way no
separate HTTP server is needed.

## Back-end

* Spring Boot 1.5.x
* H2 Database

## Front-end

* Angular 4
* Bootstrap 4 Beta

# Directories:

* [/cli] : A Command Line administration interface
* [/modules]: The different modules within the monolith all separated into different Maven modules
* [/util]: Utility libraries
* [/webapp]: The Spring Back-end
* [/ui]: The angular front-end


