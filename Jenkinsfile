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

                // Wait until app is actually UP
                bat '''
                for /l %%x in (1, 1, 20) do (
                    curl http://localhost:8081 > nul 2>&1
                    if not errorlevel 1 (
                        echo App is UP!
                        goto :done
                    )
                    echo Waiting for app...
                    timeout /t 2 > nul
                )
                :done
                '''
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