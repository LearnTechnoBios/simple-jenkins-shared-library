def call(Map specifiedParams) {

    def paramDefaults = [
            "timeout": 1,
            "microservicename": "<microservicename>"

    ]
    def pipelineParams = paramDefaults + specifiedParams
    echo pipelineParams.inspect()


    pipeline {

        agent any

        environment {
            //variables used in Jenkinsfile
            CHECKOUT_BRANCH = "${env.CHANGE_BRANCH == null ? env.GIT_BRANCH : env.CHANGE_BRANCH}"
        }

        stages {
            stage('Set Environment Variables') {
                steps {
                    script {

                        echo  pipelineParams.microservicename + " BRANCH:" + CHECKOUT_BRANCH + " Set Environment Variables"
                    }
                }
            }

            stage('Repo Login') {
                steps {
                    script {
                        echo pipelineParams.microservicename + " BRANCH:" + CHECKOUT_BRANCH + " Repo Login"
                    }
                }
            }

            stage('Build Image') {
                steps {
                    script {
                        echo pipelineParams.microservicename + " BRANCH:" + CHECKOUT_BRANCH + " Build Image"
                    }
                }
            }

            stage('Push Image') {
                parallel {
                    stage('Push branch tag') {
                        steps {
                            script {
                                echo pipelineParams.microservicename + " BRANCH:" + CHECKOUT_BRANCH + " Push branch tag"
                            }
                        }
                    }
                    stage('Push branch image') {
                        steps {
                            script {
                                echo pipelineParams.microservicename + " BRANCH:" + CHECKOUT_BRANCH + " Push branch image"
                            }
                        }
                    }
                }
            }

            stage('Post Push') {
                failFast true
                parallel {
                    stage('run E2E testing') {
                        steps {
                            script {
                                echo pipelineParams.microservicename + " BRANCH:" + CHECKOUT_BRANCH + " run E2E testing"
                            }
                        }
                    }

                    stage('Unit Tests') {
                        steps {
                            script {
                                echo pipelineParams.microservicename + " BRANCH:" + CHECKOUT_BRANCH + " Unit Tests"
                            }
                        }
                    }

                    stage('Integration Tests') {
                        steps {
                            script {
                                echo pipelineParams.microservicename + " BRANCH:" + CHECKOUT_BRANCH + " Integration Tests"
                            }
                        }
                    }
                }
            }

            stage('Push Golden Image') {
                steps {
                    script {
                        echo pipelineParams.microservicename + " BRANCH:" + CHECKOUT_BRANCH + " Push Golden Image"
                    }
                }
            }

        }

        post {
            always {
                script {
                    echo pipelineParams.microservicename + " BRANCH:" + CHECKOUT_BRANCH + " post always"
                }
            }
            cleanup {
                script {
                    echo pipelineParams.microservicename + " BRANCH:" + CHECKOUT_BRANCH + " post cleanup"
                }
            }
            failure {
                script {
                    echo pipelineParams.microservicename + " BRANCH:" + CHECKOUT_BRANCH + " post failure"
                }
            }
            aborted {
                script {
                    echo pipelineParams.microservicename + " BRANCH:" + CHECKOUT_BRANCH + " post aborted"
                }
            }
        }

    }
}
