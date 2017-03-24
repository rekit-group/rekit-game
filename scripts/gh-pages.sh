#!/bin/bash

if [ "$TRAVIS_REPO_SLUG" == "fuchss-dominik/rekit-game" ] && [ "$TRAVIS_PULL_REQUEST" == "false" ] && [ "$TRAVIS_BRANCH" == "master" ]; then

  echo -e "Publishing Documentation...\n"
  cd project
  mvn javadoc:aggregate
  cd ..
  
  cp -R "gh-pages" $HOME/doc-latest

  cd $HOME
  git config --global user.email "travis@travis-ci.org"
  git config --global user.name "travis-ci"
  git clone --quiet --branch=gh-pages https://${GH_TOKEN}@github.com/fuchss-dominik/rekit-game gh-pages > /dev/null

  rm -rf gh-pages/*
  cp -Rf $HOME/doc-latest/* ./gh-pages
  
  cd gh-pages
  git add --all -f .
  git commit -m "Latest JavaDocs on successful travis build $TRAVIS_BUILD_NUMBER auto-pushed to gh-pages"
  git push -fq origin gh-pages > /dev/null

  echo -e "Published Javadoc to gh-pages.\n"
  
fi

