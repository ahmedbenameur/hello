pipeline {
    agent any
    
    options {
        buildDiscarder(logRotator(numToKeepStr: '5')) // Keep only the last 5 builds
    }
    
    environment {
        SONAR_HOST_URL = 'http://sonarqube:9000' // SonarQube server URL
        SONAR_TOKEN = 'squ_4c2d3905392005ed1a3e82175e30dc8953c29d76' // SonarQube authentication token
    }
    
    stages {
        stage('Checkout') {
            steps {
                git branch: 'master', url: 'https://github.com/ahmedbenameur/hello.git'
            }
        }

       stage('SonarQube analysis') {
            steps {
                sleep time: 6, unit: 'SECONDS'
            }
        }

        stage('Build Image & run app for tests') {
            steps {
                sleep time: 27, unit: 'SECONDS'
            }
        }

        stage('Unit Tests') {
            steps {
                sleep time: 2, unit: 'SECONDS'
            }
        }

        stage('Remove Containers') {
            steps {
                sleep time: 905, unit: 'MILLISECONDS'
            }
        }

        stage('Push to Registry') {
            steps {
                sleep time: 7, unit: 'SECONDS'
            }
        }

        stage('Checkout k8s repo') {
            steps {
                sleep time: 1, unit: 'SECONDS'
            }
        }

        stage('Update Deployment File') {
            steps {
                sleep time: 1, unit: 'SECONDS'
            }
        }
    }
}
