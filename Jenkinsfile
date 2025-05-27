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

      
        stage('Récupération de l’image') {
            steps {
                sleep 4
            }
        }
      
        stage('Déploiement dans l’environnement prod ') {
            steps {
                sleep 10
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
