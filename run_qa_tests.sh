#!/usr/bin/env bash

ENVIRONMENT=$1
sbt scalafmtAll

#---- Acceptance QA test only
sbt clean -Denvironment="${ENVIRONMENT:=qa}" "testOnly uk.gov.hmrc.api.specLocal.*Scenario"