#!/bin/bash

# Navigate to the project directory
cd projeto_to_do_list_java

# Build the project
mvn clean install

# Run the application
mvn compile exec:java