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

      
         stage('Analyse SonarQube') {
            steps {
                sleep 6
            }
        }
        stage('Upload du JWA vers Nexus') {
            steps {
                sleep 4
            }
        }
        stage('Build Image & Run App for Tests') {
            steps {
                sleep 27
            }
        }
        stage('Push to Docker Registry') {
            steps {
                sleep 7
            }
        }
    }
    post {
        success {
            sleep 8
        }
        failure {
            sleep 8
        }
    }
}
