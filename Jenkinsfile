pipeline {
    agent any
    
    options {
        buildDiscarder(logRotator(numToKeepStr: '5')) // Keep only the last 5 builds
    }
    
    environment {
        SONAR_HOST_URL = 'http://sonarqube:9000' // SonarQube server URL
        SONAR_TOKEN = 'sqp_b1467b24b33dd569eb9ebccb9d4e24f55680e096' // SonarQube authentication token
    }
    
    stages {
        stage('Checkout') {
            steps {
                git branch: 'master', url: 'https://github.com/ahmedbenameur/hello.git'
                
              
            }
        }


        stage('Prepare SonarQube Scanner') {
    steps {
        script {
            echo "🔍 Checking if SonarQube Scanner is installed..."
        }
        sh '''
        if [ ! -d "sonar-scanner" ]; then
            echo "🚀 Installing SonarQube Scanner..."
            apt-get update && apt-get install -y wget unzip
            wget -q https://binaries.sonarsource.com/Distribution/sonar-scanner-cli/sonar-scanner-cli-5.0.1.3006-linux.zip -O sonar-scanner.zip
            unzip -q sonar-scanner.zip
            mv sonar-scanner-5.0.1.3006-linux sonar-scanner
            chmod +x sonar-scanner/bin/sonar-scanner
        else
            echo "✅ SonarQube Scanner is already installed."
        fi
        '''
    }
}

        
        stage('SonarQube Analysis') {
            steps {
                // Run SonarQube Scanner with the correct environment
                withEnv(["PATH+SCANNER=${WORKSPACE}/sonar-scanner-5.0.1.3006-linux/bin"]) {
                    sh '''
                  
                        sonar-scanner -X \
                          -Dsonar.projectKey=testtest \
                          -Dsonar.projectName=testtest \
                          -Dsonar.host.url=${SONAR_HOST_URL} \
                          -Dsonar.login=${SONAR_TOKEN} \
                          -Dsonar.sources=. \
                          -Dsonar.java.binaries=target/classes
                    '''
                }
            }
        }
    }
}
