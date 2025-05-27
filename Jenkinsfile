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
                echo 'Analyse statique du code via SonarQube...'
            }
        }
        stage('Upload JWA vers Nexus') {
            steps {
                echo 'Envoi du fichier JWA vers Nexus...'
            }
        }
        stage('Build Image Docker') {
            steps {
                echo 'Construction de l’image Docker...'
            }
        }
        stage('Push vers Docker Hub') {
            steps {
                echo 'Push de l’image dans le registre Docker...'
            }
        }
    }
    post {
        success {
            echo 'Pipeline Dev terminé avec succès.'
            echo 'Notification Slack : Succès.'
        }
        failure {
            echo 'Échec du pipeline Dev.'
            echo 'Notification Slack : Échec.'
        }
    }
}
