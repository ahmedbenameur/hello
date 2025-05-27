pipeline {
    agent any

    options {
        buildDiscarder(logRotator(numToKeepStr: '5')) // Garder seulement les 5 derniers builds
    }

    environment {
        SONAR_HOST_URL = 'http://sonarqube:9000' // URL du serveur SonarQube
        SONAR_TOKEN = 'squ_4c2d3905392005ed1a3e82175e30dc8953c29d76' // Jeton d'authentification SonarQube
    }

    stages {
        stage('Checkout') {
            steps {
                git branch: 'master', url: 'https://github.com/ahmedbenameur/hello.git'
            }
        }

        stage('Récupération de l\'image') {
            steps {
                sleep 4
            }
        }

        stage('Déploiement dans l\'environnement QA') {
            steps {
                sleep 5
            }
        }

        stage('Tests de charge') {
            steps {
                sleep 10
            }
        }
    }

    post {
        always {
            echo 'Post Actions...'
            sleep 8
        }
    }
}
