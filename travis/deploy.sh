#!/usr/bin/env bash
if [ "$TRAVIS_BRANCH" = 'master' ] && [ "$TRAVIS_PULL_REQUEST" == 'false' ]; then
    mvn clean deploy --settings travis/mvn_settings.xml
fi

if [[ "$TRAVIS_TAG" =~ "*-RELEASE" ]]; then
    mvn clean deploy --settings travis/mvn_settings.xml -P release
fi
