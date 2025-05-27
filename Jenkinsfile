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
                sleep 5
            }
        }

        stage('Analyse statique via SonarQube') {
            steps {
                sleep 15
            }
        }

        stage('Upload du JWA vers Nexus') {
            steps {
                echo "Upload du fichier JWA vers Nexus..."
                sleep 14
            }
        }

        stage('Construction et exécution de l\'image') {
            steps {
                sleep 27
            }
        }

        stage('Publication de l\'image') {
            steps {
                sleep 7
            }
        }
    }

    post {
        always {
            echo 'Pipeline terminé. Envoi de la notification Slack ou log.'
            sleep 8
        }
    }
}
