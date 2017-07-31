#!/usr/bin/env bash
if [ "$TRAVIS_BRANCH" = 'master' ] && [ "$TRAVIS_PULL_REQUEST" == 'false' ]; then
    echo $OSSRH_JIRA_USERNAME;
    echo $OSSRH_JIRA_PASSWORD;
    echo $GPG_KEY_NAME;
    echo $GPG_PASSPHRASE;
    # mvn clean deploy --settings travis/mvn_settings.xml
fi
