#!/usr/bin/env bash

cd $(git rev-parse --show-toplevel)
cd contracts-wiremock
java -jar wiremock-standalone-2.25.1.jar --port 9999
