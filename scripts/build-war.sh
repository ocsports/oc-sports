#!/usr/bin/env bash

# -_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_
# intended to be executed from the ROOT project directory
# -_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_-_

echo "removing target and war directories..."
rm -rf ./war
rm -rf ./target

echo "executing maven with 'clean package' options..."
mvn clean package

echo "creating war directory..."
mkdir ./war

echo "unzipping WAR file to war directory..."
tar -xf ./target/oc-sports.war -C ./war
