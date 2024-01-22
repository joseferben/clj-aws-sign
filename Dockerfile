ARG DOCKER_URL
ARG DOCKER_ORG
ARG ARTIFACT_ORG

FROM ${DOCKER_URL}/${DOCKER_ORG}/common-img:latest

# Custom build from here on

ARG ARTIFACT_ORG
ENV ARTIFACT_ORG $ARTIFACT_ORG
ENV PROJECT_NAME clj-aws-sign

COPY --chown=build:build LICENSE LICENSE 
COPY --chown=build:build project.clj project.clj 
COPY --chown=build:build README.md README.md 
COPY --chown=build:build resources resources 
COPY --chown=build:build doc doc 
COPY --chown=build:build src src 
COPY --chown=build:build test test

ARG BUILD_ID

RUN set -e &&\
    sed -i 's/clj-aws-sign/'${ARTIFACT_ORG}'\/clj-aws-sign/g' project.clj &&\
    lein test &&\
    lein pom &&\
    lein jar &&\
    ls -la target &&\
    version=$(grep -oP 'defproject.*"\K[^"]+' project.clj) &&\
    mvn versions:set -DnewVersion=${version}.${BUILD_ID} versions:commit &&\
    cp pom.xml /dist/release-libs/${PROJECT_NAME}-${version}.${BUILD_ID}.jar.pom.xml &&\
    cp target/clj-aws-sign*.jar /dist/release-libs/${PROJECT_NAME}-${version}.${BUILD_ID}.jar &&\
    ls -la target &&\
    rm -rf ~/.m2/repository

RUN cat pom.xml
