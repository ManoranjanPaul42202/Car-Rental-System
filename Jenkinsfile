pipeline {
    agent any

    tools {
        maven 'Maven3'
    }

    stages {

        stage('Build') {
            steps {
                bat 'mvn clean install -DskipTests'
            }
        }

        stage('Start Application') {
            steps {
                bat 'start /B java -jar target\\car-0.0.1-SNAPSHOT.jar'

                // Simple delay (NO timeout, NO curl)
                bat 'ping 127.0.0.1 -n 40'
            }
        }

        stage('Run Selenium Tests') {
            steps {
                bat 'mvn test'
            }
        }

        stage('Stop Application') {
            steps {
                bat 'taskkill /F /IM java.exe || exit 0'
            }
        }
    }
}